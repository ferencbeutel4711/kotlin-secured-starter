package de.fbeutel.lifeataglance.user.model

import org.springframework.data.annotation.Id

data class User(@Id val id: String?, val name: String, val email: String, val authProvider: String, val providerId: String?, val password: String?) {
    constructor(name: String, email: String, authProvider: String, providerId: String?, password: String?) : this(null, name, email, authProvider, providerId, password)
}