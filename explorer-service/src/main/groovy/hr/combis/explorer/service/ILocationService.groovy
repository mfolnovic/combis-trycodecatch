package hr.combis.explorer.service

import hr.combis.explorer.model.Channel
import hr.combis.explorer.model.Location
import hr.combis.explorer.service.result.ImageResult

interface ILocationService {
  List<Location> findAll()
  List<Location> findNearest(Double latitude, Double longitude, double threshold)
  Location createLocation(ImageResult result)
  Location find(String name, Double latitude, Double longitude)
  Location findByChannelId(String channelId)
  Location findByName(String name);
}
