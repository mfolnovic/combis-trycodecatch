package hr.combis.explorer.service.impl

import hr.combis.explorer.dao.ILocationRepository
import hr.combis.explorer.model.Location
import hr.combis.explorer.service.ILocationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.util.stream.Collectors

@Service
class LocationService implements ILocationService {
  final static double NEAR_THRESHOLD = 20

  final ILocationRepository locationRepository

  @Autowired
  LocationService(ILocationRepository locationRepository) {
    this.locationRepository = locationRepository
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
  List<Location> findNearest(Double latitude, Double longitude) {
    return findNearest(latitude, longitude, NEAR_THRESHOLD)
  }
}
