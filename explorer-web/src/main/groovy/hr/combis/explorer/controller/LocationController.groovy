package hr.combis.explorer.controller

import hr.combis.explorer.model.Location
import hr.combis.explorer.service.ILocationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/location")
class LocationController {
  static final DEFAULT_THRESHOLD = 1

  private ILocationService locationService

  @Autowired
  LocationController(ILocationService locationService) {
    this.locationService = locationService
  }

  @GetMapping
  @ResponseBody
  List<Location> getNear(
          @RequestParam("lat") double latitude,
          @RequestParam("long") double longitude, @RequestParam(name = "thresh", required = false) Double threshold) {
    if (threshold == null) {
      threshold = DEFAULT_THRESHOLD
    }
    return locationService.findNearest(latitude, longitude, threshold)
  }

  @GetMapping("/name")
  @ResponseBody
  Location byName(@RequestParam String name, @RequestParam("lat") double latitude, @RequestParam("long") double longitude) {
    return locationService.find(name, latitude, longitude)
  }
}
