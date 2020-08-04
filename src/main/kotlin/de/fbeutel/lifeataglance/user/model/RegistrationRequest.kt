package de.fbeutel.lifeataglance.user.model

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class RegistrationRequest(@field:NotBlank val name: String, @field:Email val email: String, @field:NotBlank val password: String)
