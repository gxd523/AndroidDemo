package com.gxd.demo.android.architecture.ui.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gxd.demo.android.util.WhileUiSubscribed
import com.gxd.demo.android.util.executeEnsureTime
import com.gxd.demo.lib.dal.repository.GithubRepository
import com.gxd.demo.lib.dal.repository.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(private val githubRepository: GithubRepository) : ViewModel() {
    companion object {
        private const val INPUT_DEBOUNCE_TIMEOUT = 1_200L
        private const val REPO_LIST_REQUEST_MIN_TIME = 300
    }

    private val _inputUsernameState = MutableStateFlow("gxd523")
    private val _uiState = MutableStateFlow(
        RepoListUiState(onItemClick = ::addReadRepoCount)
    )

    // 通过「stateIn」将「冷流」转换成「热流」即「StateFlow」，「StateFlow」也作为了「一级缓存(内存缓存)」
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val inputUsernameState = _inputUsernameState
        .debounce(INPUT_DEBOUNCE_TIMEOUT)
        .distinctUntilChanged()
        .filter { it.isNotBlank() }
        .transformLatest { username ->
            performRefresh(username)
            emit(username)
        }.stateIn(viewModelScope, WhileUiSubscribed, "")

    val uiState: StateFlow<RepoListUiState> = combine(
        _uiState,
        githubRepository.getObservableGithubUser(),
        githubRepository.getObservableRepoList(inputUsernameState.value)
    ) { currentRepoListUiState, newGithubUser, newRepoList ->
        currentRepoListUiState.copy(
            repoList = newRepoList.toImmutableList(),
            githubUser = newGithubUser,
            errorMsg = ""
        )
    }.catch {
        emit(RepoListUiState(onItemClick = ::addReadRepoCount))
    }.stateIn(viewModelScope, WhileUiSubscribed, _uiState.value)

    private fun addReadRepoCount(repo: Repo) {
        val repoListUiState = uiState.value
        val readRepoList = repoListUiState.readRepoList
        if (!readRepoList.any { repoItem -> repo.name == repoItem.name }) {
            val newReadRepoList = readRepoList.toMutableList().also { it.add(repo) }
            _uiState.value = repoListUiState.copy(readRepoList = newReadRepoList.toImmutableList())
        }
    }

    fun pullToRefresh() {
        viewModelScope.launch { performRefresh(inputUsernameState.value) }
    }

    fun updateUsername(newUsername: String) {
        _inputUsernameState.value = newUsername
    }

    private suspend fun performRefresh(username: String) = try {
        _uiState.update { it.copy(isLoading = true) }
        executeEnsureTime(REPO_LIST_REQUEST_MIN_TIME) {// 保证「异步操作」的「最小」执行时间
            githubRepository.updateRepoList(username)
        }
    } finally {
        _uiState.update { it.copy(isLoading = false) }
    }
}