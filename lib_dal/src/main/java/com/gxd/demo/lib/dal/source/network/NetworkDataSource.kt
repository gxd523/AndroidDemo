package com.gxd.demo.lib.dal.source.network

import com.gxd.demo.lib.dal.source.network.model.GithubSecret
import com.gxd.demo.lib.dal.source.network.model.GithubUser
import com.gxd.demo.lib.dal.source.network.model.NetworkRepo

interface NetworkDataSource {
    suspend fun requestRepositoryList(
        username: String, type: String? = null, sort: String? = null, page: Int? = null, perPage: Int? = null,
    ): List<NetworkRepo>

    suspend fun requestAccessToken(authorizationCode: String, githubSecret: String, redirectUrl: String): String?

    suspend fun requestGithubUser(accessToken: String): GithubUser?

    suspend fun requestGithubSecret(): GithubSecret?
}