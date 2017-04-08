package hr.combis.explorer.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id

  String uid

  String username

  int uploadedPhotos = 0

  User(String uid, String username) {
    this.username = username
  }
}
