package hr.combis.explorer.service

import hr.combis.explorer.model.Location

interface ILocationService {
  List<Location> findAll()
}