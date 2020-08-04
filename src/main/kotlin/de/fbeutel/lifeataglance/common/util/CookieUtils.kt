package de.fbeutel.lifeataglance.common.util

import org.springframework.util.SerializationUtils
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


fun getCookie(request: HttpServletRequest, name: String): Cookie? {
    return request.cookies.find { cookie -> cookie.name == name }
}

fun addCookie(response: HttpServletResponse, name: String, value: String, maxAge: Int) {
    val cookie = Cookie(name, value)
    cookie.path = "/"
    cookie.isHttpOnly = true
    cookie.maxAge = maxAge

    response.addCookie(cookie)
}

fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String) {
    if (request.cookies.any { cookie: Cookie -> cookie.name == name }) {
        val annulledCookie = Cookie(name, "")
        annulledCookie.path = "/"
        annulledCookie.maxAge = 0

        response.addCookie(annulledCookie)
    }
}

fun serializeCookieValue(value: Any?): String {
    return Base64.getUrlEncoder()
            .encodeToString(SerializationUtils.serialize(value))
}

inline fun <reified T: Any> deserializeCookieValue(cookie: Cookie): T {
    return T::class.javaObjectType.cast(SerializationUtils.deserialize(
            Base64.getUrlDecoder().decode(cookie.value)))
}
