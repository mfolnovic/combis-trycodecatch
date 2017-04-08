package hr.combis.explorer

import hr.combis.explorer.dao.IChannelRepository
import hr.combis.explorer.dao.ILocationRepository
import hr.combis.explorer.model.Channel
import hr.combis.explorer.model.Location
import hr.combis.explorer.model.User
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

  @Autowired
  InitializeDatabase(IChannelRepository channelRepository, ILocationService locationService, IUserService userService) {
    this.channelRepository = channelRepository
    this.locationService = locationService
    this.userService = userService
  }

  @PostConstruct
  void setUp() {
    User user1 = userService.save(new User("U4VPY8000", "smikulic", 10))
    User user2 = userService.save(new User("U4WFA0AKC", "mfolnovic", 5))
    User user3 = userService.save(new User("U4WGF36EQ", "jtomic", 7))

    Channel channel1 = new Channel("C4X62CPNJ", "gornji-grad-zagreb")
    Channel channel2 = new Channel("C4WJTQ7D3", "gornji-grad-test")
    channelRepository.save(channel1)
    channelRepository.save(channel2)
    locationService.createLocation(new ImageResult("Ban Jelačić Square",  45.8129051, 15.9772896), channel1)
    locationService.createLocation(new ImageResult("Croatian National Theatre", 45.8096498, 15.9700533), channel2)
  }
}
