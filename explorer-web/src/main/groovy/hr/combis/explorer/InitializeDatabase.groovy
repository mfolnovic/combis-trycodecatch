package hr.combis.explorer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
class InitializeDatabase {

  @Autowired
  InitializeDatabase() {
  }

  @PostConstruct
  void setUp() {
  }
}
