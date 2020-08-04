package de.fbeutel.lifeataglance

import de.fbeutel.lifeataglance.security.oauth2.configuration.OAuth2SecurityProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(OAuth2SecurityProperties::class)
class LifeAtAGlanceApplication

fun main(args: Array<String>) {
    runApplication<LifeAtAGlanceApplication>(*args)
}
