package hr.combis.explorer.controller

import hr.combis.explorer.service.IChannelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/channel")
class ChannelController {
  private IChannelService channelService

  @Autowired
  ChannelController(IChannelService channelService) {
    this.channelService = channelService
  }
}
