package hr.combis.explorer.service.impl

import hr.combis.explorer.dao.IAmenityRepository
import hr.combis.explorer.dao.IFactRepository
import hr.combis.explorer.model.Amenity
import hr.combis.explorer.model.Channel
import hr.combis.explorer.model.Fact
import hr.combis.explorer.model.Location
import hr.combis.explorer.service.IAmenityService
import hr.combis.explorer.service.IImageService
import hr.combis.explorer.service.ISlackService
import hr.combis.explorer.service.IWikipediaService
import hr.combis.explorer.service.result.ImageResult
import hr.combis.explorer.service.result.SlackChannel
import org.springframework.beans.factory.annotation.Autowired

import java.util.stream.Collectors

class AmenityService implements IAmenityService {
  final IAmenityRepository amenityRepository
  final IWikipediaService wikipediaService
  final IFactRepository factRepository
  final IImageService imageService
  final ISlackService slackService

  @Autowired
  AmenityService(IAmenityRepository amenityRepository, IWikipediaService wikipediaService, IFactRepository factRepository,
                 IImageService imageService, ISlackService slackService) {
    this.amenityRepository = amenityRepository
    this.wikipediaService = wikipediaService
    this.factRepository = factRepository
    this.imageService = imageService
    this.slackService = slackService
  }

  @Override
  Amenity createAmenity(ImageResult result, Location location, Channel channel = null) {
    if (result == null) {
      return null
    }

    Amenity amenity = amenityRepository.findByName(result.name)

    if (amenity != null) {
      return amenity
    }

    SlackChannel slackChannel = slackService.createChannel(result.name)
    channel = channelService.save(new Channel(slackChannel.id, slackChannel.name))

    def summary = wikipediaService.getSummary(result.name)
    def facts = wikipediaService.readFacts(result.name)

    Amenity forDb = new Amenity(result.name, summary, result.latitude, result.longitude, channel, location)
    Amenity savedAmenity = amenityRepository.save(forDb)

    facts.each {
      factRepository.save(new Fact(it, savedAmenity))
    }

    return savedAmenity
  }

  @Override
  Amenity findByImage(byte[] image, Location location) {
    //ImageResult result = imageService.searchImage(image)
    ImageResult result = new ImageResult("Ban Jelačić Square", 45.81312381458756, 15.977297)

    return createAmenity(result, location)
  }

  @Override
  List<Amenity> findAll() {
    return amenityRepository.findAll() as List<Amenity>
  }

  @Override
  List<Amenity> findNearest(Double latitude, Double longitude, double threshold) {
    List<Amenity> amenities = amenityRepository.findAll() as List<Amenity>
    return amenities.stream()
      .filter({
      it -> Math.sqrt(Math.pow(it.latitude - latitude, 2) + Math.pow(it.longitude - longitude, 2)) < threshold
    })
      .collect(Collectors.toList())
  }
}
