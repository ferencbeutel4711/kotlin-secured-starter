package de.fbeutel.lifeataglance.security.oauth2.service

import de.fbeutel.lifeataglance.common.exception.NotFoundException
import de.fbeutel.lifeataglance.security.oauth2.model.UserPrincipal
import de.fbeutel.lifeataglance.user.model.User
import de.fbeutel.lifeataglance.user.service.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class CustomUserDetailsService(val userService: UserService) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        val user: User = userService.findByEmail(email)
                ?: throw UsernameNotFoundException("User with email $email not found")

        return UserPrincipal(user)
    }

    fun loadUserById(id: String): UserDetails {
        val user: User = userService.findById(id) ?: throw NotFoundException()

        return UserPrincipal(user)
    }
}
