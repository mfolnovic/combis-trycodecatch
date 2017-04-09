package hr.combis.explorer.service.impl

import hr.combis.explorer.dao.IUserRepository
import hr.combis.explorer.model.User
import hr.combis.explorer.service.ISlackService
import hr.combis.explorer.service.IUserService
import hr.combis.explorer.service.result.SlackUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

import java.util.concurrent.locks.ReentrantLock

@Service
class UserService implements IUserService {
  private ISlackService slackService
  private IUserRepository userRepository

  private ReentrantLock lock = new ReentrantLock()

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

  @Override
  User findByUsername(String username) {
    return userRepository.findByUsername(username)
  }

  @Override
  List<User> loadRank() {
    return userRepository.loadRank(new PageRequest(0, 5))
  }

  @Override
  User save(User user) {
    user.totalScore = recalculatePoints(user)
    return userRepository.save(user)
  }

  @Override
  void increaseUploadedPhotos(User user) {
    lock.lock()

    User reloaded = userRepository.findOne(user.id)
    reloaded.uploadedPhotos += 1
    reloaded.totalScore = recalculatePoints(reloaded)
    userRepository.save(reloaded)

    lock.unlock()
  }

  private static int recalculatePoints(User user) {
    return user.uploadedPhotos * 100
  }
}
