package com.gxd.demo.lib.dal.source.network

import com.gxd.demo.lib.dal.di.qualifier.GithubClientId
import com.gxd.demo.lib.dal.source.cache.model.OAuthResult
import com.gxd.demo.lib.dal.source.network.model.GithubUser
import com.gxd.demo.lib.dal.source.network.model.GithubSecret
import com.gxd.demo.lib.dal.source.network.model.NetworkRepo
import com.gxd.demo.lib.dal.source.network.service.GithubService
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val githubService: GithubService,
) : NetworkDataSource {
    override suspend fun getRepositoryList(username: String): List<NetworkRepo> = githubService.requestRepoList(username)

    @Inject
    @GithubClientId
    lateinit var githubClientId: String

    override suspend fun requestGithubUser(accessToken: String): GithubUser? = githubService.getGithubUser(accessToken)

    override suspend fun requestGithubSecret(): GithubSecret? = githubService.getGithubSecret()

    override suspend fun requestAccessToken(authorizationCode: String, githubSecret: String, redirectUrl: String): String? {
        val response = githubService.getAccessToken(githubClientId, githubSecret, authorizationCode, redirectUrl)
        if (!response.isSuccessful) return null
        val oAuthResult = response.body()?.parseTokenResponse() ?: return null
        return oAuthResult.accessToken
    }

    private fun String.parseTokenResponse(): OAuthResult {
        val params = split("&").associate {
            val pair = it.split("=")
            pair[0] to (pair.getOrNull(1) ?: "")
        }
        return OAuthResult(
            accessToken = params["access_token"] ?: "",
            scope = params["scope"] ?: "",
            tokenType = params["token_type"] ?: ""
        )
    }
}