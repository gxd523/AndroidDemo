package com.gxd.demo.compose.request

import com.gxd.demo.compose.architecture.source.network.NetworkRepo
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {
    @GET("users/{user}/repos")
    suspend fun requestRepoList(@Path("user") username: String): List<NetworkRepo>
}