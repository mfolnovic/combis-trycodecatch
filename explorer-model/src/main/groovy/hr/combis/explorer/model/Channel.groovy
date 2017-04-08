package hr.combis.explorer.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Channel {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id

  String slackId

  String name

  Channel() {
  }

  Channel(String slackId, String name) {
    this.slackId = slackId
    this.name = name
  }
}
