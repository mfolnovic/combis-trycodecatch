package hr.combis.explorer.dao

import hr.combis.explorer.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface IUserRepository extends CrudRepository<User, Long> {
  User findByUid(String uid)

  User findByUsername(String username)

  @Query("select u from User u order by u.totalScore desc")
  List<User> loadRank(Pageable pageable)
}
