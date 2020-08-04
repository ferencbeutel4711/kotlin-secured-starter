package de.fbeutel.lifeataglance.user.annotation

import org.springframework.security.core.annotation.AuthenticationPrincipal

@AuthenticationPrincipal
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
annotation class CurrentUser
