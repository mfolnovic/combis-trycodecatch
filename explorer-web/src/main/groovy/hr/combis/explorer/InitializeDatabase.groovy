package hr.combis.explorer

import hr.combis.explorer.dao.IChannelRepository
import hr.combis.explorer.dao.ILocationRepository
import hr.combis.explorer.model.Channel
import hr.combis.explorer.model.Location
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
class InitializeDatabase {
  private IChannelRepository channelRepository
  private ILocationRepository locationRepository

  @Autowired
  InitializeDatabase(IChannelRepository channelRepository, ILocationRepository locationRepository) {
    this.channelRepository = channelRepository
    this.locationRepository = locationRepository
  }

  @PostConstruct
  void setUp() {
    Channel channel1 = new Channel("dsfljnfdsg8498", "Trg bana Jelačića")
    Channel channel2 = new Channel("adsf8dsaf7604334", "HNK")
    channelRepository.save(channel1)
    channelRepository.save(channel2)
    locationRepository.save(new Location("Trg bana Jelačića",  "", 45.8129051, 15.9772896, channel1))
    locationRepository.save(new Location("HNK", "", 45.8096498, 15.9700533, channel2))
  }
}
