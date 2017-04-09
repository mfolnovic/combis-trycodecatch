package hr.combis.explorer.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.ManyToOne
import javax.persistence.OneToOne

@Entity
class Amenity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id
  String name
  @Lob
  String summary
  BigDecimal latitude
  BigDecimal longitude

  @OneToOne
  Channel channel

  @ManyToOne
  Location location

  Amenity(){
  }

  Amenity(String name, String summary, BigDecimal latitude = null, BigDecimal longitude = null, Channel channel = null, Location location = null) {
    this.name = name
    this.summary = summary
    this.latitude = latitude
    this.longitude = longitude
    this.channel = channel
    this.location = location
  }
}
