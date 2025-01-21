package com.gxd.demo.compose.architecture.ui.repo

import com.gxd.demo.compose.architecture.repository.Repo

data class RepoListUiState(
    val repoList: List<Repo> = emptyList<Repo>(),
    val username: String = "",
    val isLoading: Boolean = false,
    val errorMsg: String = "",
)
