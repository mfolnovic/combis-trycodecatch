package hr.combis.explorer.dao

import hr.combis.explorer.model.Location
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ILocationRepository extends CrudRepository<Location, Long> {
  Location findByName(String name)

  @Query("select l from Location l where l.channel.slackId = ?1")
  Location findByChannelId(String channelId)

}
