package hr.combis.explorer.service

import hr.combis.explorer.service.result.Flight

interface IFlightsService {
  List<Flight> getFlights()
}
