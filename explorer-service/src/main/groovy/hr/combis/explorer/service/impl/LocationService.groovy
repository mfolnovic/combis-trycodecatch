package hr.combis.explorer.service.impl

import hr.combis.explorer.dao.IFactRepository
import hr.combis.explorer.dao.ILocationRepository
import hr.combis.explorer.model.Channel
import hr.combis.explorer.model.Fact
import hr.combis.explorer.model.Location
import hr.combis.explorer.service.IImageService
import hr.combis.explorer.service.ILocationService
import hr.combis.explorer.service.IWikipediaService
import hr.combis.explorer.service.result.ImageResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.util.stream.Collectors

@Service
class LocationService implements ILocationService {
  final ILocationRepository locationRepository
  final IFactRepository factRepository
  final IImageService imageService
  final IWikipediaService wikipediaService

  @Autowired
  LocationService(ILocationRepository locationRepository, IFactRepository factRepository, IImageService imageService,
                  IWikipediaService wikipediaService) {
    this.locationRepository = locationRepository
    this.factRepository = factRepository
    this.imageService = imageService
    this.wikipediaService = wikipediaService
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
  Location findByImage(byte[] image) {
//    ImageResult result = imageService.searchImage(image)
    ImageResult result = new ImageResult("Ban Jelačić Square", 45.81312381458756, 15.977297)

    return createLocation(result)
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

    def summary = wikipediaService.getSummary(result.name)

    def facts = wikipediaService.readFacts(result.name)

    Location forDb = new Location(result.name, summary, result.latitude, result.longitude, channel)
    Location savedLocation = locationRepository.save(forDb)

    facts.each {
      factRepository.save(new Fact(it, savedLocation))
    }

    return savedLocation
  }
}
