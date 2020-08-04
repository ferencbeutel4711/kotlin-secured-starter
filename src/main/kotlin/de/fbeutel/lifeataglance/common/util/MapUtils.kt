package de.fbeutel.lifeataglance.common.util

inline fun <reified T : Any> getTypedOrDefault(map: Map<String, Any>, key: String, default: T): T {
    if (!map.containsKey(key)) {
        return default
    }

    return try {
        T::class.javaObjectType.cast(map[key])
    } catch (ex: ClassCastException) {
        return default
    }
}
