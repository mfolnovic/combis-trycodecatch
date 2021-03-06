package hr.combis.explorer.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
class Location {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id
  String name
  BigDecimal latitude
  BigDecimal longitude

  @OneToOne
  Channel channel

  Location(){
  }

  Location(String name, BigDecimal latitude = null, BigDecimal longitude = null, Channel channel = null) {
    this.name = name
    this.latitude = latitude
    this.longitude = longitude
    this.channel = channel
  }
}
