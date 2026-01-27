package com.gxd.demo.android.architecture.ui.repo

import com.gxd.demo.lib.dal.repository.Repo
import com.gxd.demo.lib.dal.source.network.model.GithubUser
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RepoListUiState(
    // 「ImmutableList」让「RepoListUiState」变为「稳定对象」
    val repoList: ImmutableList<Repo> = persistentListOf(),
    val isLoading: Boolean = false,
    val errorMsg: String = "",

    val onItemClick: (Repo) -> Unit,
    val readRepoList: ImmutableList<Repo> = persistentListOf(),
    val githubUser: GithubUser? = null,
)
