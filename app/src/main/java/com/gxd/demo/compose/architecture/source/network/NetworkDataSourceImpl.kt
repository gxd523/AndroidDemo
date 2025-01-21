package com.gxd.demo.compose.architecture.source.network

import com.gxd.demo.compose.request.GithubService
import retrofit2.Retrofit
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val retrofit: Retrofit,
) : NetworkDataSource {
    private val githubService by lazy { retrofit.create(GithubService::class.java) }

    override suspend fun getRepositoryList(username: String): List<NetworkRepo> = githubService.requestRepoList(username)
}