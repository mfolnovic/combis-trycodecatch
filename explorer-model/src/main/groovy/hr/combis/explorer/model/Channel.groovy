package hr.combis.explorer.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
class Channel {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id

  Long slackId

  String name

  @OneToOne
  Location location

  Channel() {
  }

  Channel(Long slackId, String name, Location location) {
    this.slackId = slackId
    this.name = name
    this.location = location
  }
}
