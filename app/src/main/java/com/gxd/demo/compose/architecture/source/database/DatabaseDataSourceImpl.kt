package com.gxd.demo.compose.architecture.source.database

import com.gxd.demo.compose.architecture.source.database.table.RepoDao
import com.gxd.demo.compose.architecture.source.database.table.RepoTable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// TODO: 这里能用委托吗？
class DatabaseDataSourceImpl @Inject constructor(
    private val repoDao: RepoDao,
) : DatabaseDataSource {
    override fun observe(username: String): Flow<List<RepoTable>> = repoDao.observe(username)

    override suspend fun upsert(repoList: List<RepoTable>) {
        repoDao.upsert(repoList)
    }

    override suspend fun deleteAll() {
        repoDao.deleteAll()
    }

    override suspend fun insertAll(repoList: List<RepoTable>) {
        repoDao.insertAll(repoList)
    }
}