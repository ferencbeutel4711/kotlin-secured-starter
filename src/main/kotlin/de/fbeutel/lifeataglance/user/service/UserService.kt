package de.fbeutel.lifeataglance.user.service

import de.fbeutel.lifeataglance.user.model.User
import de.fbeutel.lifeataglance.user.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class UserService(
        private val userRepository: UserRepository
) {
    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun findById(id: String): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun save(user: User): User {
        return userRepository.save(user.copy(id = ObjectId.get().toString()))
    }
}
