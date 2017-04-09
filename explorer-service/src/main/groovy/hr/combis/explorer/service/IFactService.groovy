package hr.combis.explorer.service

import hr.combis.explorer.model.Amenity
import hr.combis.explorer.model.Fact

interface IFactService {
    List<Fact> findForAmenity(Amenity amenity)
    List<Fact> findAll()
    Fact save(Fact fact)
}
