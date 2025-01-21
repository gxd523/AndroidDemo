package com.gxd.demo.lib.dal.source.network

import com.gxd.demo.lib.dal.source.network.model.NetworkRepo
import com.gxd.demo.lib.dal.source.network.service.GithubService
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val githubService: GithubService,
) : NetworkDataSource {
    override suspend fun getRepositoryList(username: String): List<NetworkRepo> = githubService.requestRepoList(username)
}