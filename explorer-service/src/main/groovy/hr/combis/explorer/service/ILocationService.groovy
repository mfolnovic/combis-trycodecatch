package hr.combis.explorer.service

import hr.combis.explorer.model.Location

interface ILocationService {
  List<Location> findAll()
  List<Location> findNearest(Double latitude, Double longitude, double threshold)
}