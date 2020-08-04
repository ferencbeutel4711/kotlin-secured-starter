package de.fbeutel.lifeataglance.security.oauth2.service

import de.fbeutel.lifeataglance.common.util.getCookie
import de.fbeutel.lifeataglance.security.oauth2.configuration.OAuth2SecurityProperties
import de.fbeutel.lifeataglance.security.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository
import de.fbeutel.lifeataglance.security.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException
import java.net.URI
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class OAuth2AuthenticationSuccessHandler(private val tokenProvider: TokenProvider, private val properties: OAuth2SecurityProperties, private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val targetUrl = determineTargetUrl(request, response, authentication)

        clearAuthenticationAttributes(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    override fun determineTargetUrl(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication): String {
        val redirectUri = getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)?.value ?: defaultTargetUrl
        if (!isAuthorizedRedirectUri(redirectUri)) {
            throw HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "unauthorized redirect URI", HttpHeaders.EMPTY, ByteArray(0), null)
        }
        val token: String = tokenProvider.createToken(authentication)
        return UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token)
                .build().toUriString()
    }

    protected fun clearAuthenticationAttributes(request: HttpServletRequest, response: HttpServletResponse?) {
        super.clearAuthenticationAttributes(request)
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response!!)
    }

    private fun isAuthorizedRedirectUri(uri: String): Boolean {
        val clientRedirectUri: URI = URI.create(uri)
        return properties.authorizedRedirectUris
                .any(fun(authorizedRedirectUri): Boolean {
                    val authorizedURI: URI = URI.create(authorizedRedirectUri)
                    return authorizedURI.host.equals(clientRedirectUri.host, ignoreCase = true)
                            && authorizedURI.port == clientRedirectUri.port
                })
    }
}
