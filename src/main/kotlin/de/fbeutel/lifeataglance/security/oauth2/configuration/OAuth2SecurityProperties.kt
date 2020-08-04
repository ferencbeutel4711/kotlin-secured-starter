package de.fbeutel.lifeataglance.security.oauth2.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "security.oauth2")
class OAuth2SecurityProperties(val tokenSecret: String, val tokenExpirationMs: Long, val authorizedRedirectUris: List<String>)
