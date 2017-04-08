package hr.combis.explorer.service

interface IWikipediaService {
  String getSummary(String query)

  List<String> readFacts(String query);
}
