package hr.combis.explorer.service

import hr.combis.explorer.model.User

interface IUserService {
  User findByUid(String uid)
}
