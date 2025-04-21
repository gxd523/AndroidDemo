package com.gxd.demo.lib.dal.repository

import com.gxd.demo.lib.dal.source.cache.CacheDataSource
import com.gxd.demo.lib.dal.source.database.DatabaseDataSource
import com.gxd.demo.lib.dal.source.database.model.RepoEntity
import com.gxd.demo.lib.dal.source.network.NetworkDataSource
import com.gxd.demo.lib.dal.source.network.model.GithubUser
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val databaseDataSource: DatabaseDataSource,
    private val cacheDataSource: CacheDataSource,
) : GithubRepository {
    override fun getObservableRepoList(username: String): Flow<List<Repo>> = if (username.isEmpty()) {
        databaseDataSource.observeAllRepoList()
    } else {
        databaseDataSource.observeRepoList(username)
    }.map { repoList ->
        repoList.map { Repo(it.id, it.name, it.url, it.description) }
    }.flowOn(Dispatchers.IO)

    private var githubUserFlow: MutableStateFlow<GithubUser?>? = null

    override fun getObservableGithubUser(): Flow<GithubUser?> = MutableStateFlow<GithubUser?>(null).also {
        githubUserFlow = it.also aaa@{
            val githubUser = cacheDataSource[CacheDataSource.GITHUB_USER] ?: return@aaa
            it.value = githubUser as GithubUser
        }
    }

    override suspend fun updateRepoList(username: String) = withContext(Dispatchers.IO) {
        val networkRepoList = try {
            networkDataSource.requestRepositoryList(username)
        } catch (e: Exception) {
            if (e is CancellationException) throw e else emptyList()
        }
        val repoList = networkRepoList.map {
            RepoEntity(it.id ?: 0, username, it.name ?: "", it.htmlUrl ?: "", it.description ?: "")
        }
        databaseDataSource.deleteAndInsertRepoList(repoList)
    }

    override suspend fun requestGithubUser(authorizationCode: String, redirectUrl: String): GithubUser? {
        var githubSecret = cacheDataSource[CacheDataSource.GITHUB_SECRET] as? String
        if (githubSecret.isNullOrEmpty()) {
            githubSecret = networkDataSource.requestGithubSecret()?.githubOauthSecret ?: return null
            cacheDataSource["githubSecret"] = githubSecret
        }
        var accessToken = cacheDataSource[CacheDataSource.ACCESS_TOKEN] as? String
        if (accessToken.isNullOrEmpty()) {
            accessToken = networkDataSource.requestAccessToken(authorizationCode, githubSecret, redirectUrl) ?: return null
            cacheDataSource["accessToken"] = accessToken
        }

        return networkDataSource.requestGithubUser("Bearer $accessToken")?.also {
            cacheDataSource[CacheDataSource.GITHUB_USER] = it
            githubUserFlow?.value = it
        }
    }

    override fun getRepoList(
        username: String, type: String?, sort: String?, page: Int?, perPage: Int?,
    ): Flow<List<Repo>> = channelFlow {
        val databaseJob = launch(Dispatchers.IO) {
            val databaseRepoList = databaseDataSource.getRepoList(username).map { it.toRepo() }
            send(databaseRepoList)
        }
        launch(Dispatchers.IO) {
            val networkRepoList = networkDataSource.requestRepositoryList(
                username, type, sort, page, perPage
            ).map { it.toRepo() }
            send(networkRepoList)
            databaseJob.cancel()
        }
    }
}