package hr.combis.explorer.dao

import hr.combis.explorer.model.User
import org.springframework.data.repository.CrudRepository

interface IUserRepository extends CrudRepository<User, Long> {
  User findByUid(String uid)
}
