package hr.combis.explorer.service

import hr.combis.explorer.model.User

interface IUserService {
  User findByUid(String uid)

  User findByUsername(String username)

  void increaseUploadedPhotos(User user)

  List<User> loadRank()

  User save(User user)
}
