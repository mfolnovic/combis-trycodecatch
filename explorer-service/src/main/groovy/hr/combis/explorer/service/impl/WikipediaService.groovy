package hr.combis.explorer.service.impl

import hr.combis.explorer.service.IWikipediaService
import org.springframework.web.client.RestTemplate

import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathFactory

class WikipediaService implements IWikipediaService {
  final URL_FORMAT = "https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=xml&exintro=&titles=%s"

  private final RestTemplate restTemplate = new RestTemplate()

  @Override
  String getSummary(String query) {
    def url = String.format(URL_FORMAT, query)
    def response = restTemplate.getForObject(url, String.class)

    def xpath = XPathFactory.newInstance().newXPath()
    def builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    def inputStream = new ByteArrayInputStream(response.bytes)
    def records = builder.parse(inputStream).documentElement
    def content = xpath.evaluate("//api/query/pages/page/extract", records)

    return content.replaceAll("<(.|\n)*?>", '')
  }
}
