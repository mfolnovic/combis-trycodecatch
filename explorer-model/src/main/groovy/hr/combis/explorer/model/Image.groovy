package hr.combis.explorer.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class Image {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id

  String path

  @ManyToOne
  Location location

  Image() {
  }

  Image(String path, Location location) {
    this.path = path
    this.location = location
  }
}
