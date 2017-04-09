package hr.combis.explorer.service.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import hr.combis.explorer.service.ISlackService
import hr.combis.explorer.service.result.SlackChannel
import hr.combis.explorer.service.result.SlackUser
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class OurSlackService implements ISlackService {
  final def FETCH_USER_URL = "https://slack.com/api/users.info?token=%s&user=%s"
  final def CREATE_CHANNEL_URL = "https://slack.com/api/channels.create?token=%s&name=%s"
  final def LIST_CHANNELS_URL = "https://slack.com/api/channels.list?token=%s"

  @Value("\${slackBotToken}")
  String slackToken

  @Value("\${slackChannelToken}")
  String slackChannelToken

  private def restTemplate = new RestTemplate()

  @Override
  SlackUser fetchUser(String id) {
    def url = String.format(FETCH_USER_URL, slackToken, id)
    String content = restTemplate.getForObject(url, String.class)

    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> map = new HashMap<String, Object>()
    map = mapper.readValue(content, new TypeReference<Map<String, Object>>(){})

    def user = (Map)map.getOrDefault("user", new HashMap())
    def username = (String) user.getOrDefault("name", id)

    return new SlackUser(id, username)
  }

  @Override
  SlackChannel createChannel(String name) {
    def url = String.format(CREATE_CHANNEL_URL, slackChannelToken, name)
    String content = restTemplate.getForObject(url, String.class)

    ObjectMapper mapper = new ObjectMapper()
    Map<String, Object> map = new HashMap<String, Object>()
    map = mapper.readValue(content, new TypeReference<Map<String, Object>>(){})

    def channel = (Map)map.getOrDefault("channel", new HashMap())
    def id = (String) channel.get("id")
    def createdName = (String) channel.getOrDefault("name", name)

    return new SlackChannel(id, createdName)
  }

  @Override
  SlackChannel findChannel(String name) {
    def url = String.format(LIST_CHANNELS_URL, slackChannelToken)
    String content = restTemplate.getForObject(url, String.class)

    ObjectMapper mapper = new ObjectMapper()
    Map<String, Object> map = new HashMap<String, Object>()
    map = mapper.readValue(content, new TypeReference<Map<String, Object>>(){})

    def channels = (List)map.getOrDefault("channels", new ArrayList())
    String formatedName = formatName(name)
    for (Object channel : channels) {
      channel = (Map) channel
      def channelName = (String) channel.get("name")
      if (channelName == formatedName) {
        def id = (String) channel.get("id")
        return new SlackChannel(id, channelName)
      }
    }
    return null
  }

  private static String formatName(String name) {
    return name.toLowerCase().replace(" ", "-").substring(0, name.length() <= 21 ? name.length() : 21)
  }
}
