package hr.combis.explorer.service

import hr.combis.explorer.model.Amenity
import hr.combis.explorer.model.Channel
import hr.combis.explorer.model.Location
import hr.combis.explorer.service.result.ImageResult

interface IAmenityService {
  List<Amenity> findAll()
  List<Amenity> findNearest(Double latitude, Double longitude, double threshold)
  Amenity createAmenity(ImageResult result, Location location, Channel channel)
  Amenity findByImage(byte[] image, Location location)
}