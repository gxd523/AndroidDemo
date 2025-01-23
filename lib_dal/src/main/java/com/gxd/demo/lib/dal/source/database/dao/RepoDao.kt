package com.gxd.demo.lib.dal.source.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.gxd.demo.lib.dal.source.database.model.RepoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDao {
    @Query("SELECT * FROM repo WHERE username = :username")
    fun observeRepoList(username: String): Flow<List<RepoEntity>>

    @Query("SELECT * FROM repo")
    fun observeAllRepoList(): Flow<List<RepoEntity>>

    @Upsert
    suspend fun upsertRepoList(repoList: List<RepoEntity>)

    @Query("DELETE FROM repo")
    suspend fun deleteAllRepoList()

    @Insert
    suspend fun insertAllRepoList(repoList: List<RepoEntity>)

    @Transaction
    suspend fun deleteAndInsertRepoList(repoList: List<RepoEntity>) {
        deleteAllRepoList()
        insertAllRepoList(repoList)
    }
}