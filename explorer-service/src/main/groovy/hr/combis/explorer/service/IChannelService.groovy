package hr.combis.explorer.service

import hr.combis.explorer.model.Channel
import hr.combis.explorer.model.Location

interface IChannelService {
  Channel findForLocation(Location location)
  List<Channel> find
}