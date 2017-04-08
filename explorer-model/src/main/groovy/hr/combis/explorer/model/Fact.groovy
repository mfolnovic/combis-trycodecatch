package hr.combis.explorer.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.ManyToOne

@Entity
class Fact {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id
  @Lob
  String sentence
  @ManyToOne
  Location location

  Fact(){
  }

  Fact(String sentence, Location location) {
    this.sentence = sentence
    this.location = location
  }

  @Override
  int hashCode() {
    return this.id
  }

  @Override
  boolean equals(o) {
    if (this.is(o)) return true
    if (getClass() != o.class) return false

    Fact fact = (Fact) o

    if (id != fact.id) return false

    return true
  }
}
