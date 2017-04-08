package hr.combis.explorer.controller

import hr.combis.explorer.model.User
import hr.combis.explorer.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class UserController {
  private IUserService userService

  @Autowired
  UserController(IUserService userService) {
    this.userService = userService
  }

  @GetMapping("/users/rank")
  @ResponseBody
  List<User> rank() {
    return userService.loadRank()
  }
}
