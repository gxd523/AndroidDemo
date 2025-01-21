package com.gxd.demo.lib.dal.source.database.model

import androidx.room.Entity

@Entity(tableName = "repo", primaryKeys = ["id", "username"])
data class RepoEntity(
    val id: Int = 0,
    val username: String = "",
    val name: String = "",
    val url: String = "",
    val description: String = "",
)
