package de.fbeutel.lifeataglance.user.web

import de.fbeutel.lifeataglance.common.exception.BadRequestException
import de.fbeutel.lifeataglance.common.exception.NotFoundException
import de.fbeutel.lifeataglance.security.oauth2.model.UserPrincipal
import de.fbeutel.lifeataglance.security.oauth2.service.TokenProvider
import de.fbeutel.lifeataglance.user.annotation.CurrentUser
import de.fbeutel.lifeataglance.user.model.LoginRequest
import de.fbeutel.lifeataglance.user.model.RegistrationRequest
import de.fbeutel.lifeataglance.user.model.User
import de.fbeutel.lifeataglance.user.service.UserService
import org.jetbrains.annotations.NotNull
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/user")
class UserRestController(
        private val authenticationManager: AuthenticationManager,
        private val userService: UserService,
        private val passwordEncoder: PasswordEncoder,
        private val tokenProvider: TokenProvider
) {

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    fun getLoggedInUser(@CurrentUser userPrincipal: UserPrincipal): ResponseEntity<User> {
        return ResponseEntity
                .ok(userService.findById(userPrincipal.id ?: throw BadRequestException()) ?: throw NotFoundException())
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<String> {
        val auth = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password))
        SecurityContextHolder.getContext().authentication = auth

        val token = tokenProvider.createToken(auth)
        return ResponseEntity.ok(token)
    }

    @PostMapping("/registration")
    fun register(@Valid @RequestBody registrationRequest: RegistrationRequest): ResponseEntity<String> {
        if (userService.findByEmail(registrationRequest.email) != null) {
            throw BadRequestException()
        }

        userService.save(User(registrationRequest.name, registrationRequest.email, "local", null, passwordEncoder.encode(registrationRequest.password)))

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user/me")
                .build()
                .toUri())
                .body("user created successfully")
    }
}
