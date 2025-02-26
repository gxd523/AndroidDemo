package com.gxd.demo.lib.dal.source.cache

class CacheDataSource {
    companion object {
        const val ACCESS_TOKEN = "access_token"
        const val GITHUB_SECRET = "github_secret"
        const val GITHUB_USER = "github_user"
    }

    private val map = mutableMapOf<String, Any?>()

    operator fun get(key: String): Any? = map[key]

    operator fun set(key: String, value: Any) {
        map[key] = value
    }
}