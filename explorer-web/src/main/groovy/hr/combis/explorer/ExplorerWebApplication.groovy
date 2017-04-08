package hr.combis.explorer

import me.ramswaroop.jbot.core.slack.SlackService
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan([ "hr.combis" , "me.ramswaroop"])
class ExplorerWebApplication {
  static void main(String[] args) {
    SpringApplication.run ExplorerWebApplication, args
  }
}
