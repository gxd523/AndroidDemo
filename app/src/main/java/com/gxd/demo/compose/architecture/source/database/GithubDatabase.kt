package com.gxd.demo.compose.architecture.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gxd.demo.compose.architecture.source.database.table.RepoDao
import com.gxd.demo.compose.architecture.source.database.table.RepoTable

@Database(entities = [RepoTable::class], version = 1, exportSchema = false)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}