package de.fbeutel.lifeataglance.security.oauth2.model

import de.fbeutel.lifeataglance.common.util.getTypedOrDefault

class GithubOAuth2UserInfo(override val id: String, override val name: String, override val email: String) : OAuth2UserInfo(id, name, email) {

    constructor(params: Map<String, Any>) : this(getTypedOrDefault(params, idKey, ""), getTypedOrDefault(params, nameKey, ""), getTypedOrDefault(params, emailKey, ""))

    companion object {
        internal const val idKey = "id"
        internal const val nameKey = "name"
        internal const val emailKey = "email"
    }
}

fun validGithubOAuth2UserInfo(params: Map<String, Any>): Boolean {
    return params.containsKey(GithubOAuth2UserInfo.idKey) && params.containsKey(GithubOAuth2UserInfo.nameKey) && params.containsKey(GithubOAuth2UserInfo.emailKey)
}
