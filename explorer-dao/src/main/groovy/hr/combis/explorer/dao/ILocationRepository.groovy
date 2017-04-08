package hr.combis.explorer.dao

import hr.combis.explorer.model.Location
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ILocationRepository extends CrudRepository<Location, Long> {
  Location findByName(String name)
}
