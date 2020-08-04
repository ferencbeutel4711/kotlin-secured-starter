package de.fbeutel.lifeataglance.security.oauth2.configuration

import de.fbeutel.lifeataglance.common.util.customLogger
import de.fbeutel.lifeataglance.security.oauth2.service.CustomUserDetailsService
import de.fbeutel.lifeataglance.security.oauth2.service.TokenProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class TokenAuthenticationFilter(private val tokenProvider: TokenProvider, private val customUserDetailsService: CustomUserDetailsService) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            val jwt = getJwtFromRequest(request)
            if (jwt != null && jwt.isNotEmpty() && tokenProvider.validateToken(jwt)) {
                val userDetails = customUserDetailsService.loadUserById(tokenProvider.getUserIdFromToken(jwt))
                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (ex: Exception) {
            customLogger().error("Could not set user authentication in security context", ex)
        }

        filterChain.doFilter(request, response)
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7, authHeader.length)
        }

        return null
    }
}
