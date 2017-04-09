package hr.combis.explorer.controller.slack

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.ling.CoreLabel
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.util.CoreMap
import hr.combis.explorer.controller.slack.nlpUtils.Document
import hr.combis.explorer.controller.slack.nlpUtils.Sentence
import hr.combis.explorer.controller.slack.nlpUtils.Word
import hr.combis.explorer.model.Amenity
import hr.combis.explorer.model.Fact
import hr.combis.explorer.model.Location
import hr.combis.explorer.model.User
import hr.combis.explorer.service.IAmenityService
import hr.combis.explorer.service.IFactService
import hr.combis.explorer.service.IImageService
import hr.combis.explorer.service.ILocationService
import hr.combis.explorer.service.ISlackService
import hr.combis.explorer.service.IUserService
import me.ramswaroop.jbot.core.slack.Bot
import me.ramswaroop.jbot.core.slack.Controller
import me.ramswaroop.jbot.core.slack.EventType
import me.ramswaroop.jbot.core.slack.models.Event
import me.ramswaroop.jbot.core.slack.models.File
import me.ramswaroop.jbot.core.slack.models.Message
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.socket.WebSocketSession

import java.util.regex.Matcher
import java.util.regex.Pattern

@Component
public class SlackBot extends Bot {
    @Value("image-dir-path")
    private String imageDirPath

    @Value("\${slack.bot.enabled}")
    private boolean enabled

    private IImageService imageService

    private ILocationService locationService

    private IAmenityService amenityService

    private IUserService userService

    private IFactService factService

    private ISlackService slackService

    private StanfordCoreNLP pipeline

    private HashMap<String, Amenity> userAmenities

    private static final Logger logger = LoggerFactory.getLogger(SlackBot.class)

    private List<String> stopWords = []

    /**
     * Slack token from application.properties file. You can get your slack token
     * next <a href="https://my.slack.com/services/new/bot">creating a new bot</a>.
     */
    @Value("\${slackBotToken}")
    private String slackToken

    private Double startTimestamp

    private double rankTreshold = 1.1

    @Autowired
    public SlackBot(IImageService imageService, ILocationService locationService, IUserService userService, IFactService factService,
                    ISlackService slackService, IAmenityService amenityService) {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties()
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma") // , ner, parse, dcoref

        this.userAmenities = new HashMap<>()
        this.startTimestamp = new Double(System.currentTimeMillis()/1000)
        this.pipeline = new StanfordCoreNLP(props)
        this.imageService = imageService
        this.userService = userService
        this.locationService = locationService
        this.factService = factService
        this.amenityService = amenityService
        this.slackService = slackService
        def reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/stopword.txt")))
        this.stopWords = reader.readLines()
    }

    @Override
    public String getSlackToken() {
        return slackToken
    }

    @Override
    public Bot getSlackBot() {
        return this
    }


    public void onMentionMessage(WebSocketSession session, Event event) {
        String username = slackService.fetchUser(event.getUserId()).username
        String new_fact = event.getText().substring(event.getText().indexOf('>')+1)
        Amenity amenity = userAmenities.get(event.getUserId())
        if(amenity != null){
            factService.save(new Fact(new_fact, amenity))
            reply(session, event, new Message("@" + username + " Thanks! :)"))
        }
    }

    private void processMessage(WebSocketSession session, Event event) throws IOException {
        Double eventTS = Double.parseDouble(event.getTs())

        if (eventTS < this.startTimestamp){
            return
        }

        User user = userService.findByUid(event.userId)

        if (user.username == "explorer") {
            return
        }


        if (event.getFile() != null) {
            Amenity amenity = processFile(event.getFile(), event.channelId)
            if (amenity.channel.slackId != event.channel.id) {
                reply(session, event, new Message("You can see more about it on following channel: " +
                        "https://trycodecatch-explorer.slack.com/messages/"+amenity.channel.slackId+"/"))
            }else{
                userService.increaseUploadedPhotos(user)
                this.userAmenities.put(event.getUserId(), amenity)
                reply(session, event, new Message(amenity.summary))
            }
        } else if (event.getText() != null) {
            if (event.getText().startsWith("<@")){
                onMentionMessage(session, event)
                return
            }
            Amenity amenity = this.userAmenities.getOrDefault(event.getUserId(), null)
            if (amenity == null) {
                reply(session, event, new Message(getDontKnowMessage(user.username, event.getText())))
            } else {
                Map<Fact, Double> rankings = new HashMap<>()

                List<Fact> facts = factService.findForAmenity(amenity)
                for(Fact fact : facts){
                    rankings.put(fact, getRanking(fact.sentence, event.getText()))
                }

                Fact fact = getBestFact(rankings)
                if (rankings.get(fact) < rankTreshold){
                    reply(session, event, new Message(getDontKnowMessage(user.username, event.getText())))
                }else{
                    reply(session, event, new Message(fact.sentence))
                }
                // vraca odgovor iz cinjenica
            }
        }

    }

    String getDontKnowMessage(String username, String question) {
        return "@channel Can anyone answer @"+username+" 's question? \""+question+"\""
    }

    private Fact getBestFact(HashMap<Fact, Double> facts) {
        Map.Entry<Fact, Double> maxEntry = null;

        for (Map.Entry<Fact, Double> entry : facts.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry
            }
        }
        return maxEntry.getKey()
    }

    private Double getRanking(String fact, String query) {
        Document factDoc = preprocessDocument(fact)
        if (factDoc.size() != 1){
            return 0
        }
        Document queryDoc = preprocessDocument(query)
        return getFactSimilarity(factDoc.getSentence(0), queryDoc)
    }

    private Double getFactSimilarity(Sentence fact, Document queryDoc) {
        double score = 0
        for (Sentence sentence: queryDoc.getSentences()){
            score += getSimilarity(fact, sentence)
        }
        return score
    }

    private Double getSimilarity(Sentence fact, Sentence query) {
        double score = 0
        for (Word word: query.getWords()){
            if (this.stopWords.contains(word.token)) {
                continue
            }
            score += fact.wordScore(word)
        }
        return score
    }

    private Document preprocessDocument(String text) {
        Annotation document = new Annotation(text)

        // run all Annotators on this text
        pipeline.annotate(document)

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class)
        Document doc = new Document()
        for(CoreMap sentence: sentences) {
            Sentence sen = new Sentence()
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the
                // list of lemmas
                String token1 = token.originalText()
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class)
                String ner = token.get(CoreAnnotations.NERIDAnnotation.class)
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class)
                Word word = new Word(token1, pos, lemma, ner)
                sen.addWord(word)
            }
            doc.addSentence(sen)
        }
        return doc
    }

    private Amenity processFile(File file, String channelId) {
        String url = file.getUrlPrivateDownload()


        RestTemplate restTemplate = new RestTemplate()
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter())
        HttpHeaders headers = new HttpHeaders()
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM))
        headers.add("Authorization", String.format("Bearer %s", slackToken))
        HttpEntity<String> entity = new HttpEntity<>(headers)

        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class, "1")

        byte[] body = response.getBody()


        Location location = locationService.findByChannelId(channelId)
        return amenityService.findByImage(body, location)
    }

    /**
     * Invoked when the bot receives a direct mention (@botname: message)
     * or a direct message. NOTE: These two event types are added by jbot
     * to make your task easier, Slack doesn't have any direct way to
     * determine these type of events.
     *
     * @param session
     * @param event
     */
    @Controller(events = [EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE])
    public void onReceiveDM(WebSocketSession session, Event event) {
        if (!enabled) {
            return
        }
        try {
            processMessage(session, event)
        } catch (IOException e) {
            e.printStackTrace()
        }
    }


    @Controller(events = [EventType.MESSAGE])
    public void onReceiveM(WebSocketSession session, Event event) {
        if (!enabled) {
            return
        }
        try {
            processMessage(session, event)
        } catch (IOException e) {
            e.printStackTrace()
        }
    }

    /**
     * Invoked when an item is pinned in the channel.
     *
     * @param session
     * @param event
     */
//    @Controller(events = EventType.PIN_ADDED)
    public void onPinAdded(WebSocketSession session, Event event) {
        reply(session, event, new Message("Thanks for the pin! You can find all pinned items under channel details."))
    }

    /**
     * Invoked when bot receives an event of type file shared.
     * NOTE: You can't reply to this event as slack doesn't send
     * a channel id for this event type. You can learn more about
     * <a href="https://api.slack.com/events/file_shared">file_shared</a>
     * event from Slack's Api documentation.
     *
     * @param session
     * @param event
     */
//    @Controller(events = EventType.FILE_SHARED)
    public void onFileShared(WebSocketSession session, Event event) {
        logger.info("File shared: {}", event)
    }


//    @Controller(pattern = "(setup meeting)", next = "confirmTiming")
    public void setupMeeting(WebSocketSession session, Event event) {
        startConversation(event, "confirmTiming")   // start conversation
        reply(session, event, new Message("Cool! At what time (ex. 15:30) do you want me to set up the meeting?"))
    }

    /**
     * This method is chained with {@link SlackBot#setupMeeting(WebSocketSession, Event)}.
     *
     * @param session
     * @param event
     */
//    @Controller(next = "askTimeForMeeting")
    public void confirmTiming(WebSocketSession session, Event event) {
        reply(session, event, new Message("Your meeting is set at " + event.getText() +
                ". Would you like to repeat it tomorrow?"))
        nextConversation(event)    // jump to next question in conversation
    }

    /**
     * This method is chained with {@link SlackBot#confirmTiming(WebSocketSession, Event)}.
     *
     * @param session
     * @param event
     */
//    @Controller(next = "askWhetherToRepeat")
    public void askTimeForMeeting(WebSocketSession session, Event event) {
        if (event.getText().contains("yes")) {
            reply(session, event, new Message("Okay. Would you like me to set a reminder for you?"))
            nextConversation(event)    // jump to next question in conversation  
        } else {
            reply(session, event, new Message("No problem. You can always schedule one with 'setup meeting' command."))
            stopConversation(event)    // stop conversation only if user says no
        }
    }

    /**
     * This method is chained with {@link SlackBot#askTimeForMeeting(WebSocketSession, Event)}.
     *
     * @param session
     * @param event
     */
//    @Controller
    public void askWhetherToRepeat(WebSocketSession session, Event event) {
        if (event.getText().contains("yes")) {
            reply(session, event, new Message("Great! I will remind you tomorrow before the meeting."))
        } else {
            reply(session, event, new Message("Oh! my boss is smart enough to remind himself :)"))
        }
        stopConversation(event)    // stop conversation
    }
}
