package com.gxd.demo.lib.dal.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.gxd.demo.lib.dal.source.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun observeAllUserList(): Flow<List<UserEntity>>
}