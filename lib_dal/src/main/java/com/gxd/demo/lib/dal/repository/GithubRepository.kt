package com.gxd.demo.lib.dal.repository

import com.gxd.demo.lib.dal.source.network.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    fun getObservableRepoList(username: String): Flow<List<Repo>>

    fun getObservableGithubUser(): Flow<GithubUser?>

    suspend fun updateRepoList(username: String)

    suspend fun requestGithubUser(authorizationCode: String, redirectUrl: String): GithubUser?

    fun getRepoList(
        username: String, type: String? = null, sort: String? = null, page: Int? = null, perPage: Int? = null,
    ): Flow<List<Repo>>
}