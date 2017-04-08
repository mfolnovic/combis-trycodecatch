package hr.combis.explorer.service.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import hr.combis.explorer.service.ISlackService
import hr.combis.explorer.service.result.SlackUser
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class OurSlackService implements ISlackService {
  final def URL_FORMAT = "https://slack.com/api/users.info?token=%s&user=%s"

  @Value("\${slackBotToken}")
  String slackToken

  private def restTemplate = new RestTemplate()

  @Override
  SlackUser fetchUser(String id) {
    def url = String.format(URL_FORMAT, slackToken, id)
    String content = restTemplate.getForObject(url, String.class)

    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> map = new HashMap<String, Object>()
    map = mapper.readValue(content, new TypeReference<Map<String, Object>>(){})

    def user = (Map)map.getOrDefault("user", new HashMap())
    def username = (String) user.getOrDefault("name", id)

    return new SlackUser(id, username)
  }
}
