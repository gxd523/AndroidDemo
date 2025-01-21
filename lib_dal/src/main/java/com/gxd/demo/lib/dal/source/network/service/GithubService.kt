package com.gxd.demo.lib.dal.source.network.service

import com.gxd.demo.lib.dal.source.network.model.NetworkRepo
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {
    @GET("users/{user}/repos")
    suspend fun requestRepoList(@Path("user") username: String): List<NetworkRepo>
}