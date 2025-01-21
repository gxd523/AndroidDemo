package com.gxd.demo.compose.architecture.repository

import com.gxd.demo.compose.architecture.source.database.DatabaseDataSource
import com.gxd.demo.compose.architecture.source.database.table.RepoTable
import com.gxd.demo.compose.architecture.source.network.NetworkDataSource
import com.gxd.demo.compose.data.NetworkRepo
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
    override fun getObservableRepoList(username: String): Flow<List<Repo>> = databaseDataSource.observe(username).map {
        it.map { Repo(it.id, it.name, it.url, it.description) }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateRepoList(username: String) = withContext(Dispatchers.IO) {
        val networkRepoList = try {
            networkDataSource.getRepositoryList(username)
        } catch (e: Exception) {
            if (e is CancellationException) throw e else emptyList<NetworkRepo>()
        }
        val repoList = networkRepoList.map {
            RepoTable(it.id ?: 0, username, it.name ?: "", it.url ?: "", it.description ?: "")
        }
        databaseDataSource.deleteAndInsert(repoList)
    }
}