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
    fun observe(username: String): Flow<List<RepoEntity>>

    @Upsert
    suspend fun upsert(repoList: List<RepoEntity>)

    @Query("DELETE FROM repo")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(repoList: List<RepoEntity>)

    @Transaction
    suspend fun deleteAndInsert(repoList: List<RepoEntity>) {
        deleteAll()
        insertAll(repoList)
    }
}