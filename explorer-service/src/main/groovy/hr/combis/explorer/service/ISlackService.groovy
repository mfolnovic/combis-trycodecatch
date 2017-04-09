package hr.combis.explorer.service

import hr.combis.explorer.service.result.SlackChannel
import hr.combis.explorer.service.result.SlackUser

interface ISlackService {
  SlackUser fetchUser(String id)
  SlackChannel createChannel(String name)
  SlackChannel findChannel(String name)
}
