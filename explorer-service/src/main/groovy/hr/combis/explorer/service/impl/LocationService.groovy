package hr.combis.explorer.service.impl

import hr.combis.explorer.dao.ILocationRepository
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
  final IImageService imageService
  final IWikipediaService wikipediaService

  @Autowired
  LocationService(ILocationRepository locationRepository, IImageService imageService,
                  IWikipediaService wikipediaService) {
    this.locationRepository = locationRepository
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

    if (result == null) {
      return null
    }

    Location location = locationRepository.findByName(result.name)

    if (location != null) {
      return location
    }

    def summary = wikipediaService.getSummary(result.name)
    Location forDb = new Location(result.name, summary, result.latitude, result.longitude)

    return locationRepository.save(forDb)
  }
}
