package hr.combis.explorer

import hr.combis.explorer.dao.IChannelRepository
import hr.combis.explorer.dao.ILocationRepository
import hr.combis.explorer.model.Amenity
import hr.combis.explorer.model.Channel
import hr.combis.explorer.model.Location
import hr.combis.explorer.model.User
import hr.combis.explorer.service.IAmenityService
import hr.combis.explorer.service.ILocationService
import hr.combis.explorer.service.IUserService
import hr.combis.explorer.service.result.ImageResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
class InitializeDatabase {
  private IChannelRepository channelRepository
  private ILocationService locationService
  private IUserService userService
  private IAmenityService amenityService

  @Autowired
  InitializeDatabase(IChannelRepository channelRepository, ILocationService locationService, IUserService userService, IAmenityService amenityService) {
    this.channelRepository = channelRepository
    this.locationService = locationService
    this.userService = userService
    this.amenityService = amenityService
  }

  @PostConstruct
  void setUp() {
    User user1 = userService.save(new User("U4VPY8000", "smikulic", 10))
    User user2 = userService.save(new User("U4WFA0AKC", "mfolnovic", 5))
    User user3 = userService.save(new User("U4WGF36EQ", "jtomic", 7))

    Location zagreb = locationService.createLocation(new ImageResult("Zagreb, Croatia", 45.8401104, 15.8242485))
    Location split = locationService.createLocation(new ImageResult("Split, Croatia", 43.5081323, 16.4401934))
    Location zadar = locationService.createLocation(new ImageResult("Zadar, Croatia", 44.119371, 15.23136479))
    amenityService.createAmenity(new ImageResult("Ban Jelačić Square",  45.8129051, 15.9772896), zagreb)
    amenityService.createAmenity(new ImageResult("Croatian National Theatre in Zagreb", 45.8096498, 15.9700533), zagreb)
    amenityService.createAmenity(new ImageResult("Diocletian's Palace", 43.508013, 16.44001007), split)
    amenityService.createAmenity(new ImageResult("Church of St. Donatus", 44.115533, 15.224513411), zadar)}
}
