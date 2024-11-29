package com.gxd.demo.compose.request

import com.gxd.demo.compose.data.Repo
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {
    @GET("users/{user}/repos")
    suspend fun listRepos(@Path("user") user: String): List<Repo>
}