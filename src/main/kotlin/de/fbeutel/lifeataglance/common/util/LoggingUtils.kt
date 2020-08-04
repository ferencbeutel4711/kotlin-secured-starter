package de.fbeutel.lifeataglance.common.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

inline fun <reified T : Any> T.customLogger(): Logger =
        getLogger(T::class.java)
