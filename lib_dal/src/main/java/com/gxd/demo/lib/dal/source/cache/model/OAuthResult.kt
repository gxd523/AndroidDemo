package com.gxd.demo.lib.dal.source.cache.model

import com.google.gson.annotations.SerializedName

data class OAuthResult(
    @SerializedName("access_token")
    val accessToken: String? = null,
    @SerializedName("refresh_token")
    val refreshToken: String? = null,
    @SerializedName("expires_in")
    val expireTime: String? = null,
    val scope: String? = null,
    val tokenType: String? = null,
)
