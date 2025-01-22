package com.gxd.demo.compose.architecture.ui.repo

import com.gxd.demo.lib.dal.repository.Repo

data class RepoListUiState(
    val repoList: List<Repo> = emptyList<Repo>(),
    val errorMsg: String = "",
    val onItemClick: (Repo) -> Unit,
    val readRepoList: List<Repo> = emptyList<Repo>(),
)
