package hr.combis.explorer.dao

import hr.combis.explorer.model.Location
import org.springframework.data.repository.CrudRepository

interface ILocationRepository extends CrudRepository<Location, Long> {
}
