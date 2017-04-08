package hr.combis.explorer.dao

import hr.combis.explorer.model.Channel
import org.springframework.data.repository.CrudRepository

interface IChannelRepository extends CrudRepository<Channel, Long> {
}
