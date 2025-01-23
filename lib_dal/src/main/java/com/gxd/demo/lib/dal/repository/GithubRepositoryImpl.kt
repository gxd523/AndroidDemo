package com.gxd.demo.lib.dal.repository

import com.gxd.demo.lib.dal.source.database.DatabaseDataSource
import com.gxd.demo.lib.dal.source.database.model.RepoEntity
import com.gxd.demo.lib.dal.source.network.NetworkDataSource
import com.gxd.demo.lib.dal.source.network.model.NetworkRepo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val databaseDataSource: DatabaseDataSource,
) : GithubRepository {
    override fun getObservableRepoList(username: String): Flow<List<Repo>> = if (username.isEmpty()) {
        databaseDataSource.observeAllRepoList()
    } else {
        databaseDataSource.observeRepoList(username)
    }.map {
        it.map { Repo(it.id, it.name, it.url, it.description) }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateRepoList(username: String) = withContext(Dispatchers.IO) {
        val networkRepoList = try {
            networkDataSource.getRepositoryList(username)
        } catch (e: Exception) {
            if (e is CancellationException) throw e else emptyList<NetworkRepo>()
        }
        val repoList = networkRepoList.map {
            RepoEntity(it.id ?: 0, username, it.name ?: "", it.htmlUrl ?: "", it.description ?: "")
        }
        databaseDataSource.deleteAndInsertRepoList(repoList)
    }
}