package de.fbeutel.lifeataglance.security.oauth2.model

abstract class OAuth2UserInfo(open val id: String, open val name: String, open val email: String) {
}
