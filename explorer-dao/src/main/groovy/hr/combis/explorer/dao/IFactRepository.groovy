package hr.combis.explorer.dao

import hr.combis.explorer.model.Amenity
import hr.combis.explorer.model.Fact
import org.springframework.data.repository.CrudRepository

interface IFactRepository extends CrudRepository<Fact, Long> {
    List<Fact> findByAmenity(Amenity amenity)
}