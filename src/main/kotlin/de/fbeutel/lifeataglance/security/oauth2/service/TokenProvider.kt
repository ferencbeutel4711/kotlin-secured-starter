package de.fbeutel.lifeataglance.security.oauth2.service

import de.fbeutel.lifeataglance.common.util.customLogger
import de.fbeutel.lifeataglance.security.oauth2.configuration.OAuth2SecurityProperties
import de.fbeutel.lifeataglance.security.oauth2.model.UserPrincipal
import io.jsonwebtoken.*
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.security.SignatureException
import java.util.*

@Service
class TokenProvider(private val properties: OAuth2SecurityProperties) {
    fun createToken(authentication: Authentication): String {
        val userPrincipal: UserPrincipal = authentication.principal as UserPrincipal
        val now = Date()
        val expiryDate = Date(now.time + properties.tokenExpirationMs)
        return Jwts.builder()
                .setSubject(userPrincipal.id)
                .setIssuedAt(Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, properties.tokenSecret)
                .compact()
    }

    fun getUserIdFromToken(token: String): String {
        return Jwts.parser()
                .setSigningKey(properties.tokenSecret)
                .parseClaimsJws(token)
                .body
                .subject
    }

    fun validateToken(authToken: String): Boolean {
        try {
            Jwts.parser().setSigningKey(properties.tokenSecret).parseClaimsJws(authToken)
            return true
        } catch (ex: SignatureException) {
            customLogger().error("Invalid JWT signature for token $authToken")
        } catch (ex: MalformedJwtException) {
            customLogger().error("Invalid JWT token for token $authToken")
        } catch (ex: ExpiredJwtException) {
            customLogger().error("Expired JWT token for token $authToken")
        } catch (ex: UnsupportedJwtException) {
            customLogger().error("Unsupported JWT token for token $authToken")
        } catch (ex: IllegalArgumentException) {
            customLogger().error("JWT claims string is empty for token $authToken")
        }

        return false
    }
}
