package com.gxd.demo.lib.dal.source.network.service

import com.gxd.demo.lib.dal.source.network.model.GithubUser
import com.gxd.demo.lib.dal.source.network.model.GithubSecret
import com.gxd.demo.lib.dal.source.network.model.NetworkRepo
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface GithubService {
    @GET("users/{user}/repos")
    suspend fun requestRepoList(@Path("user") username: String): List<NetworkRepo>

    @POST("https://github.com/login/oauth/access_token")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String,
    ): Response<String>

    @GET("user")
    suspend fun getGithubUser(@Header("Authorization") authorization: String): GithubUser?

    @GET("https://gxd523.github.io/api/oauth.json")
    suspend fun getGithubSecret(): GithubSecret?
}