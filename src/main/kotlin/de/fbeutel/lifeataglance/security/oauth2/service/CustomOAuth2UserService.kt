package de.fbeutel.lifeataglance.security.oauth2.service

import de.fbeutel.lifeataglance.common.util.customLogger
import de.fbeutel.lifeataglance.security.oauth2.model.GithubOAuth2UserInfo
import de.fbeutel.lifeataglance.security.oauth2.model.OAuth2UserInfo
import de.fbeutel.lifeataglance.security.oauth2.model.UserPrincipal
import de.fbeutel.lifeataglance.security.oauth2.model.validGithubOAuth2UserInfo
import de.fbeutel.lifeataglance.user.model.User
import de.fbeutel.lifeataglance.user.repository.UserRepository
import de.fbeutel.lifeataglance.user.service.UserService
import org.bson.types.ObjectId
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils.isEmpty


@Service
class CustomOAuth2UserService(val userService: UserService) : DefaultOAuth2UserService() {

    override fun loadUser(oAuth2UserRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(oAuth2UserRequest)

        return try {
            processOAuth2User(oAuth2UserRequest, oAuth2User)
        } catch (ex: AuthenticationException) {
            throw ex
        } catch (ex: Exception) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }
    }

    private fun processOAuth2User(oAuth2UserRequest: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2User {
        val oAuth2UserInfo: OAuth2UserInfo = mapToSpecificUserInfo(oAuth2UserRequest.clientRegistration.registrationId, oAuth2User.attributes)
        if (isEmpty(oAuth2UserInfo.email)) {
            throw OAuth2AuthenticationException(OAuth2Error("400"), "Email not found from OAuth2 provider")
        }
        userService.findByEmail(oAuth2UserInfo.email)?.let { foundUser ->
            if (foundUser.authProvider == oAuth2UserRequest.clientRegistration.registrationId) {
                return UserPrincipal(updateExistingUser(foundUser, oAuth2UserInfo), oAuth2User.attributes)
            } else {
                throw OAuth2AuthenticationException(OAuth2Error("400"), "Your account is already bound to ${foundUser.authProvider}")
            }
        } ?: return UserPrincipal(registerNewUser(oAuth2UserRequest, oAuth2UserInfo), oAuth2User.attributes)
    }

    private fun mapToSpecificUserInfo(registrationId: String, params: Map<String, Any>): OAuth2UserInfo {
        when (registrationId) {
            "github" -> {
                if (validGithubOAuth2UserInfo(params)) {
                    return GithubOAuth2UserInfo(params)
                }
                customLogger().error("invalid github credentials", params)
            }
        }

        throw OAuth2AuthenticationException(OAuth2Error("400"), "invalid provider or invalid payload from provider")
    }

    private fun registerNewUser(oAuth2UserRequest: OAuth2UserRequest, oAuth2UserInfo: OAuth2UserInfo): User {
        return userService.save(User(oAuth2UserInfo.name, oAuth2UserInfo.email, oAuth2UserRequest.clientRegistration.registrationId, oAuth2UserInfo.id, null))
    }

    private fun updateExistingUser(existingUser: User, oAuth2UserInfo: OAuth2UserInfo): User {
        return userService.save(existingUser.copy(name = oAuth2UserInfo.name))
    }
}
