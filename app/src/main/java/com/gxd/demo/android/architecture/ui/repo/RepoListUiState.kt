package com.gxd.demo.android.architecture.ui.repo

import com.gxd.demo.lib.dal.repository.Repo
import com.gxd.demo.lib.dal.source.network.model.GithubUser

data class RepoListUiState(
    val repoList: List<Repo> = emptyList(),
    val errorMsg: String = "",
    val onItemClick: (Repo) -> Unit,
    val readRepoList: List<Repo> = emptyList(),
    val githubUser: GithubUser? = null,
)
