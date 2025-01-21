package com.gxd.demo.compose.architecture.source.database.table

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDao {
    @Query("SELECT * FROM repo WHERE username = :username")
    fun observe(username: String): Flow<List<RepoTable>>

    @Upsert
    suspend fun upsert(repoList: List<RepoTable>)

    @Query("DELETE FROM repo")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(repoList: List<RepoTable>)

    @Transaction
    suspend fun deleteAndInsert(repoList: List<RepoTable>) {
        deleteAll()
        insertAll(repoList)
    }
}