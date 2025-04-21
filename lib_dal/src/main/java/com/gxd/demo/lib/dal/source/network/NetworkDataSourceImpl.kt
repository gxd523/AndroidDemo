package com.gxd.demo.lib.dal.source.network

import com.gxd.demo.lib.dal.di.qualifier.GithubClientId
import com.gxd.demo.lib.dal.source.cache.model.OAuthResult
import com.gxd.demo.lib.dal.source.network.model.GithubSecret
import com.gxd.demo.lib.dal.source.network.model.GithubUser
import com.gxd.demo.lib.dal.source.network.model.NetworkRepo
import com.gxd.demo.lib.dal.source.network.service.GithubApiService
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(private val githubApiService: GithubApiService) : NetworkDataSource {
    override suspend fun requestRepositoryList(
        username: String, type: String?, sort: String?, page: Int?, perPage: Int?,
    ): List<NetworkRepo> = githubApiService.requestRepoList(username, type, sort, page, perPage)

    @Inject
    @GithubClientId
    lateinit var githubClientId: String

    override suspend fun requestGithubUser(accessToken: String): GithubUser? = githubApiService.getGithubUser(accessToken)

    override suspend fun requestGithubSecret(): GithubSecret? = githubApiService.getGithubSecret()

    override suspend fun requestAccessToken(authorizationCode: String, githubSecret: String, redirectUrl: String): String? {
        val response = githubApiService.getAccessToken(githubClientId, githubSecret, authorizationCode, redirectUrl)
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