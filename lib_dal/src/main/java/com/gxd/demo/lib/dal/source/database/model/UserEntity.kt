package com.gxd.demo.lib.dal.source.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id: Int = 0,
    val name: String = "",
)
