package com.gxd.demo.lib.dal.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gxd.demo.lib.dal.source.database.dao.RepoDao
import com.gxd.demo.lib.dal.source.database.dao.UserDao
import com.gxd.demo.lib.dal.source.database.model.RepoEntity
import com.gxd.demo.lib.dal.source.database.model.UserEntity

@Database(entities = [RepoEntity::class, UserEntity::class], version = 1, exportSchema = false)
abstract class GxdDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao

    abstract fun userDao(): UserDao
}