package hr.combis.explorer.service.impl

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.ling.CoreLabel
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.util.CoreMap
import hr.combis.explorer.service.IWikipediaService
import org.apache.commons.lang.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathFactory

@Service
class WikipediaService implements IWikipediaService {
  final URL_FORMAT = "https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=xml&exintro=&titles=%s"
  final FULL_URL_FORMAT = "https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=xml&titles=%s"

  private final RestTemplate restTemplate = new RestTemplate()

  private StanfordCoreNLP pipeline

  WikipediaService() {
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize, ssplit");
    this.pipeline = new StanfordCoreNLP(props)
  }

  @Override
  String getSummary(String query) {
    def url = String.format(URL_FORMAT, query)
    return getContent(url).replaceAll("<(.|\n)*?>", '')
  }

  @Override
  List<String> readFacts(String query) {
    def url = String.format(FULL_URL_FORMAT, query)
    def content = getContent(url)
    return facts(clean(content))
  }

  String getContent(String url) {
    def response = restTemplate.getForObject(url, String.class)

    def xpath = XPathFactory.newInstance().newXPath()
    def builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    def inputStream = new ByteArrayInputStream(response.bytes)
    def records = builder.parse(inputStream).documentElement
    return xpath.evaluate("//api/query/pages/page/extract", records)
  }

  static String removeTag(String content, String tag) {
    return content.replaceAll("<$tag[^>]*>[^<]*</$tag>", "")
  }

  static String clean(String content) {
    def blacklist = ["h2", "ul", "li"]

    def current = content
    blacklist.each {
      current = current.replaceAll("(?i)<$it.*?</$it>", "")
    }

    return current.replaceAll("<(.|\n)*?>", '').replaceAll('\\s{2,}', '')
  }

  List<String> facts(String content) {
    Annotation document = new Annotation(content)

    // run all Annotators on this text
    pipeline.annotate(document)

    List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class)

    def result = []
    sentences.each {
      def tokens = []
      for (CoreLabel token : it.get(CoreAnnotations.TokensAnnotation.class)) {
        tokens += token.originalText()
      }
      result += tokens.join(' ')
    }

    return result
  }
}
