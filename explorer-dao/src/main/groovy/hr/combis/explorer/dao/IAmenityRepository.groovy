package hr.combis.explorer.dao

import hr.combis.explorer.model.Amenity
import org.springframework.data.repository.CrudRepository

interface IAmenityRepository extends CrudRepository<Amenity, Long> {
  Amenity findByName(String name)
}