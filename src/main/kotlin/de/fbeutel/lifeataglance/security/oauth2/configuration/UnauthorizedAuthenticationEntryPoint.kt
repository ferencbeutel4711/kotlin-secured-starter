package de.fbeutel.lifeataglance.security.oauth2.configuration

import de.fbeutel.lifeataglance.common.util.customLogger
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class UnauthorizedAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        customLogger().error("Unauthorized access - ${authException.message}")

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
    }
}
