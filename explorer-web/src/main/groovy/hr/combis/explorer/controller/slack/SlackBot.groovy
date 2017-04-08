package hr.combis.explorer.controller.slack

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.ling.CoreLabel
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.util.CoreMap
import hr.combis.explorer.controller.slack.nlpUtils.Document
import hr.combis.explorer.controller.slack.nlpUtils.Sentence
import hr.combis.explorer.controller.slack.nlpUtils.Word
import hr.combis.explorer.model.Fact
import hr.combis.explorer.model.Location
import hr.combis.explorer.model.User
import hr.combis.explorer.service.IFactService
import hr.combis.explorer.service.IImageService
import hr.combis.explorer.service.ILocationService
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

import javax.print.Doc
import java.time.LocalDateTime
import java.util.regex.Matcher

@Component
public class SlackBot extends Bot {
    @Value("image-dir-path")
    private String imageDirPath

    private IImageService imageService

    private ILocationService locationService

    private IUserService userService

    private IFactService factService

    private StanfordCoreNLP pipeline

    private HashMap<String, Location> userLocations

    private static final Logger logger = LoggerFactory.getLogger(SlackBot.class)

    private List<String> stopWords = []

    /**
     * Slack token from application.properties file. You can get your slack token
     * next <a href="https://my.slack.com/services/new/bot">creating a new bot</a>.
     */
    @Value("\${slackBotToken}")
    private String slackToken

    private Double startTimestamp

    @Autowired
    public SlackBot(IImageService imageService, ILocationService locationService, IUserService userService, IFactService factService){
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties()
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma") // , ner, parse, dcoref

        this.userLocations = new HashMap<>()
        this.startTimestamp = new Double(System.currentTimeMillis()/1000)
        this.pipeline = new StanfordCoreNLP(props)
        this.imageService = imageService
        this.userService = userService
        this.locationService = locationService
        this.factService = factService

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


    private void processMessage(WebSocketSession session, Event event) throws IOException {
        Double eventTS = Double.parseDouble(event.getTs())

        if (eventTS < this.startTimestamp){
            return
        }

        User user = userService.findByUid(event.userId)

        if (user.username == "explorer") {
            return
        }

        if(event.getFile() != null){
            Location location = processFile(event.getFile())
            userService.increaseUploadedPhotos(user)
            this.userLocations.put(event.getUserId(), location)
            reply(session, event, new Message(location.summary))
        }else if(event.getText() != null) {
            Location location = this.userLocations.getOrDefault(event.getUserId(), null)
            if (location == null){
                String userId = event.getUserId()
                reply(session, event, new Message("<@all> Can anyone answer <@"+userId+">'s question?"))
            }else{
                Map<Fact, Double> rankings = new HashMap<>()

                List<Fact> facts = factService.findForLocation(location)
                for(Fact fact : facts){
                    rankings.put(fact, getRanking(fact.sentence, event.getText()))
                }

                String fact = getBestFact(rankings)
                // vraca odgovor iz cinjenica
                reply(session, event, new Message(fact))
            }
        }

    }

    private String getBestFact(HashMap<Fact, Double> facts) {
        Map.Entry<Fact, Double> maxEntry = null;

        for (Map.Entry<Fact, Double> entry : facts.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry
            }
        }
        return maxEntry.getKey().sentence
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
                println(word.token)
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

    private Location processFile(File file) {
        String url = file.getUrlPrivateDownload()

        RestTemplate restTemplate = new RestTemplate()
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter())
        HttpHeaders headers = new HttpHeaders()
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM))
        headers.add("Authorization", String.format("Bearer %s", slackToken))
        HttpEntity<String> entity = new HttpEntity<>(headers)

        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class, "1")

        byte[] body = response.getBody()

        return locationService.findByImage(body)
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
        try {
            processMessage(session, event)
        } catch (IOException e) {
            e.printStackTrace()
        }
    }


    @Controller(events = [EventType.MESSAGE])
    public void onReceiveM(WebSocketSession session, Event event) {
        try {
            processMessage(session, event)
        } catch (IOException e) {
            e.printStackTrace()
        }
    }
    /**
     * Invoked when bot receives an event of type message with text satisfying
     * the pattern {@code ([a-z ]{2})(\d+)([a-z ]{2})}. For example,
     * messages like "ab12xy" or "ab2bc" etc will invoke this method.
     *
     * @param session
     * @param event
     */
//    @Controller(events = EventType.MESSAGE, pattern = "^([a-z ]{2})(\\d+)([a-z ]{2})$")
    public void onReceiveMessage(WebSocketSession session, Event event, Matcher matcher) {
        reply(session, event, new Message("First group: " + matcher.group(0) + "\n" +
                "Second group: " + matcher.group(1) + "\n" +
                "Third group: " + matcher.group(2) + "\n" +
                "Fourth group: " + matcher.group(3)))
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
