package hr.combis.explorer.dao

import hr.combis.explorer.model.Fact
import hr.combis.explorer.model.Location
import org.springframework.data.repository.CrudRepository

interface IFactRepository extends CrudRepository<Fact, Long> {
    List<Fact> findByLocation(Long location_id)
}