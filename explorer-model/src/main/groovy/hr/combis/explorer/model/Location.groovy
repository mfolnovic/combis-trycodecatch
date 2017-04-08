package hr.combis.explorer.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Location {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id
  String name
  BigDecimal latitude
  BigDecimal longitude

  Location(){
  }

  Location(String name, Double latitude, Double longitude) {
    this.name = name
    this.latitude = latitude
    this.longitude = longitude
  }
}