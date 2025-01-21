package com.gxd.demo.compose.architecture.source.database.table

import androidx.room.Entity

@Entity(tableName = "repo", primaryKeys = ["id", "username"])
data class RepoTable(
    val id: Int = 0,
    val username: String = "",
    val name: String = "",
    val url: String = "",
    val description: String = "",
)
