package hr.combis.explorer.service.impl

import hr.combis.explorer.dao.IFactRepository
import hr.combis.explorer.dao.ILocationRepository
import hr.combis.explorer.model.Channel
import hr.combis.explorer.model.Location
import hr.combis.explorer.service.IChannelService
import hr.combis.explorer.service.ILocationService
import hr.combis.explorer.service.ISlackService
import hr.combis.explorer.service.IWikipediaService
import hr.combis.explorer.service.result.ImageResult
import hr.combis.explorer.service.result.SlackChannel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.util.stream.Collectors

@Service
class LocationService implements ILocationService {
  final ILocationRepository locationRepository
  final IFactRepository factRepository
  final IWikipediaService wikipediaService
  final ISlackService slackService
  final IChannelService channelService

  @Autowired
  LocationService(ILocationRepository locationRepository, IFactRepository factRepository, IWikipediaService wikipediaService,
                  ISlackService slackService, IChannelService channelService) {
    this.locationRepository = locationRepository
    this.factRepository = factRepository
    this.wikipediaService = wikipediaService
    this.slackService = slackService
    this.channelService = channelService
  }

  @Override
  List<Location> findAll() {
    return locationRepository.findAll() as List<Location>
  }

  @Override
  List<Location> findNearest(Double latitude, Double longitude, double threshold) {
    List<Location> locations = locationRepository.findAll() as List<Location>
    return locations.stream()
            .filter({
      it -> Math.sqrt(Math.pow(it.latitude - latitude, 2) + Math.pow(it.longitude - longitude, 2)) < threshold
    })
            .collect(Collectors.toList())
  }

  @Override
  Location find(String name, Double latitude, Double longitude) {
    Location location = locationRepository.findByName(name)
    if (location != null) {
      return location
    }

    SlackChannel slackChannel = slackService.createChannel(name.split(',')[0])
    Channel channel = channelService.save(new Channel(slackChannel.id, slackChannel.name))
    return locationRepository.save(new Location(name, latitude, longitude, channel))
  }

  @Override
  Location createLocation(ImageResult result, Channel channel = null) {
    if (result == null) {
      return null
    }

    Location location = locationRepository.findByName(result.name)

    if (location != null) {
      return location
    }

    Location forDb = new Location(result.name, result.latitude, result.longitude, channel)
    Location savedLocation = locationRepository.save(forDb)
    return savedLocation
  }

  @Override
  Location findByChannelId(String channelId) {
    return locationRepository.findByChannelId(channelId)
  }

  @Override
  Location findByName(String name) {
    return locationRepository.findByName(name)
  }
}
