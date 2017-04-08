package hr.combis.explorer.service

import hr.combis.explorer.model.Fact
import hr.combis.explorer.model.Location

interface IFactService {
    List<Fact> findForLocation(Location location)
    List<Fact> findAll()
}
