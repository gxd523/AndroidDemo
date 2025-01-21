package com.gxd.demo.lib.dal.repository

import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    fun getObservableRepoList(username: String): Flow<List<Repo>>

    suspend fun updateRepoList(username: String)
}