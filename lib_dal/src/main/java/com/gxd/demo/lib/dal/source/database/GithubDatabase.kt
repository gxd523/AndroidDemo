package com.gxd.demo.lib.dal.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gxd.demo.lib.dal.source.database.dao.RepoDao
import com.gxd.demo.lib.dal.source.database.model.RepoEntity

@Database(entities = [RepoEntity::class], version = 1, exportSchema = false)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}