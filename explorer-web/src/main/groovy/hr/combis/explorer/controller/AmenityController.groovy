package hr.combis.explorer.controller

import hr.combis.explorer.model.Amenity
import hr.combis.explorer.service.IAmenityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/amenity")
class AmenityController {

  private IAmenityService amenityService

  @Autowired
  AmenityController(IAmenityService amenityService) {
    this.amenityService = amenityService
  }

  @GetMapping
  @ResponseBody
  List<Amenity> getInBox(@RequestParam("minLat") double minLat, @RequestParam("minLng") double minLng,
                         @RequestParam("maxLat") double maxLat, @RequestParam("maxLng") double maxLng) {

  }
}
