package hr.combis.explorer.service

import hr.combis.explorer.service.result.SlackUser

interface ISlackService {
  SlackUser fetchUser(String id)
}
