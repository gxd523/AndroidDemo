package com.gxd.demo.lib.dal.source.network.service

import com.gxd.demo.lib.dal.source.network.model.GithubSecret
import com.gxd.demo.lib.dal.source.network.model.GithubUser
import com.gxd.demo.lib.dal.source.network.model.NetworkRepo
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("users/{user}/repos")
    suspend fun requestRepoList(
        @Path("user") username: String,
        /**
         * all（用户所有仓库，包括协作的）、 owner（仅用户自己创建的，默认值）、 member（仅用户参与协作的）
         */
        @Query("type") type: String? = null,
        /**
         * created（创建时间）、 updated（更新时间）、 pushed（最后推送时间）、 full_name（仓库全名，默认值）
         */
        @Query("sort") sort: String? = null,
        /**
         * 分页页码（默认 1）
         */
        @Query("page") page: Int? = null,
        /**
         * 每页条目数（默认 30，最大 100）
         */
        @Query("per_page") perPage: Int? = null,
    ): List<NetworkRepo>

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