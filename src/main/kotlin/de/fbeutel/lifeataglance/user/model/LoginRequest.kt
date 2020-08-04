package de.fbeutel.lifeataglance.user.model

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class LoginRequest(@field:Email val email: String, @field:NotBlank val password: String)
