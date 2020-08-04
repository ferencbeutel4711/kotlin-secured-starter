package de.fbeutel.lifeataglance.security.oauth2.model

import de.fbeutel.lifeataglance.user.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User


class UserPrincipal(val id: String?, private val email: String, private val password: String?, private val authorities: List<GrantedAuthority>, private val attributes: Map<String, Any>) : OAuth2User, UserDetails {

    constructor(user: User) : this(user.id, user.email, user.password, listOf(SimpleGrantedAuthority("ROLE_USER")), HashMap())
    constructor(user: User, attributes: Map<String, Any>) : this(user.id, user.email, user.password, listOf(SimpleGrantedAuthority("ROLE_USER")), attributes)

    override fun getName(): String {
        return email
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return attributes.toMutableMap()
    }

    override fun getPassword(): String {
        return password ?: ""
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities.toMutableList()
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getUsername(): String {
        return email
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }
}
