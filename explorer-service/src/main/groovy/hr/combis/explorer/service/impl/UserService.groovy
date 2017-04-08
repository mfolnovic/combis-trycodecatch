package hr.combis.explorer.service.impl

import hr.combis.explorer.dao.IUserRepository
import hr.combis.explorer.model.User
import hr.combis.explorer.service.ISlackService
import hr.combis.explorer.service.IUserService
import hr.combis.explorer.service.result.SlackUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService implements IUserService {
  private ISlackService slackService
  private IUserRepository userRepository

  @Autowired
  UserService(ISlackService slackService, IUserRepository userRepository) {
    this.slackService = slackService
    this.userRepository = userRepository
  }

  @Override
  User findByUid(String uid) {
    User existing = userRepository.findByUid(uid)

    if (existing != null) {
      return existing
    }

    SlackUser slackUser = slackService.fetchUser(uid)
    return userRepository.save(new User(uid, slackUser.username))
  }
}
