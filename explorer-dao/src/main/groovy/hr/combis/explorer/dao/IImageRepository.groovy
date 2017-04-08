package hr.combis.explorer.dao

import hr.combis.explorer.model.Image
import org.springframework.data.repository.CrudRepository

interface IImageRepository extends CrudRepository<Image, Long> {
}
