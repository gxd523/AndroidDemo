package com.gxd.demo.compose.architecture.repository

import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    fun getObservableRepoList(username: String): Flow<List<Repo>>

    suspend fun updateRepoList(username: String)
}