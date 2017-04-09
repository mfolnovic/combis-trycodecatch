package hr.combis.explorer.service.impl

import hr.combis.explorer.dao.IChannelRepository
import hr.combis.explorer.dao.ILocationRepository
import hr.combis.explorer.model.Channel
import hr.combis.explorer.model.Location
import hr.combis.explorer.service.IChannelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChannelService implements IChannelService {
  private IChannelRepository channelRepository
  private ILocationRepository locationRepository

  @Autowired
  ChannelService(IChannelRepository channelRepository, ILocationRepository locationRepository) {
    this.channelRepository = channelRepository
    this.locationRepository = locationRepository
  }

  @Override
  Channel findForLocation(Location location) {
    return locationRepository.findOne(location.id).channel
  }

  @Override
  Channel save(Channel channel) {
    return channelRepository.save(channel)
  }
}
