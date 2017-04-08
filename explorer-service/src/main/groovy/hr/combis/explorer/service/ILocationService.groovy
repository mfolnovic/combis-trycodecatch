package hr.combis.explorer.service

import hr.combis.explorer.model.Channel
import hr.combis.explorer.model.Location
import hr.combis.explorer.service.result.ImageResult

interface ILocationService {
  List<Location> findAll()
  List<Location> findNearest(Double latitude, Double longitude, double threshold)
  Location createLocation(ImageResult result, Channel channel)
  Location findByImage(byte[] image)
}
