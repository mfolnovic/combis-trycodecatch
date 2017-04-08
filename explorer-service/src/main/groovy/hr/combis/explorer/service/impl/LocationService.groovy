package hr.combis.explorer.service.impl

import hr.combis.explorer.dao.ILocationRepository
import hr.combis.explorer.model.Location
import hr.combis.explorer.service.ILocationService
import org.springframework.beans.factory.annotation.Autowired

class LocationService implements ILocationService {
  final ILocationRepository locationRepository

  @Autowired
  LocationService(ILocationRepository locationRepository) {
    this.locationRepository = locationRepository
  }

  @Override
  List<Location> findAll() {
    return locationRepository.findAll() as List<Location>
  }
}
