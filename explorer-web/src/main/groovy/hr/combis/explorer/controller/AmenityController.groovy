package hr.combis.explorer.controller

import hr.combis.explorer.model.Amenity
import hr.combis.explorer.model.Location
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
  static final DEFAULT_THRESHOLD = 1

  private IAmenityService amenityService

  @Autowired
  AmenityController(IAmenityService amenityService) {
    this.amenityService = amenityService
  }

  @GetMapping("/near")
  @ResponseBody
  List<Amenity> getNear(
          @RequestParam("lat") double latitude,
          @RequestParam("long") double longitude, @RequestParam(name = "thresh", required = false) Double threshold) {
    if (threshold == null) {
      threshold = DEFAULT_THRESHOLD
    }
    return amenityService.findNearest(latitude, longitude, threshold)
  }


  @GetMapping("/boundingBox")
  @ResponseBody
  List<Amenity> getInBox(@RequestParam("minLat") Double minLat, @RequestParam("minLng") Double minLng,
                         @RequestParam("maxLat") Double maxLat, @RequestParam("maxLng") Double maxLng) {
    return amenityService.findInBoundingBox(minLat, minLng, maxLat, maxLng)
  }
}
