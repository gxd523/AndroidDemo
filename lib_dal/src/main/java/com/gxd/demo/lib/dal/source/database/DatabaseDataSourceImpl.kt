package com.gxd.demo.lib.dal.source.database

import com.gxd.demo.lib.dal.source.database.dao.RepoDao
import com.gxd.demo.lib.dal.source.database.model.RepoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// TODO: 这里能用委托吗？
class DatabaseDataSourceImpl @Inject constructor(
    private val repoDao: RepoDao,
) : DatabaseDataSource {
    override fun observe(username: String): Flow<List<RepoEntity>> = repoDao.observe(username)

    override suspend fun upsert(repoList: List<RepoEntity>) {
        repoDao.upsert(repoList)
    }

    override suspend fun deleteAll() {
        repoDao.deleteAll()
    }

    override suspend fun insertAll(repoList: List<RepoEntity>) {
        repoDao.insertAll(repoList)
    }
}