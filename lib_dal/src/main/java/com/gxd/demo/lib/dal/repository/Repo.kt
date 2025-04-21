package com.gxd.demo.lib.dal.repository

import com.gxd.demo.lib.dal.source.database.model.RepoEntity
import com.gxd.demo.lib.dal.source.network.model.NetworkRepo

data class Repo(
    val id: Int = 0,
    val name: String = "",
    val url: String = "",
    val description: String = "",
)

fun RepoEntity.toRepo(): Repo = Repo(id, name, url, description)

fun NetworkRepo.toRepo(): Repo = Repo(id ?: 0, name ?: "", url ?: "", description ?: "")
