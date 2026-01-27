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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(private val githubRepository: GithubRepository) : ViewModel() {
    companion object {
        private const val INPUT_DEBOUNCE_TIMEOUT = 1_200L
        private const val REPO_LIST_REQUEST_MIN_TIME = 300
        private const val DEFAULT_INPUT_USERNAME = "gxd523"
    }

    private val _inputUsernameState = MutableStateFlow(DEFAULT_INPUT_USERNAME)
    private val _localState = MutableStateFlow(LocalState())

    val rawInputUsername = _inputUsernameState.asStateFlow()

    // 通过「stateIn」将「冷流」转换成「热流」即「StateFlow」，「StateFlow」也作为了「一级缓存(内存缓存)」
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val inputUsernameState = _inputUsernameState
        .debounce(INPUT_DEBOUNCE_TIMEOUT)
        .distinctUntilChanged()
        .filter { it.isNotBlank() }
        .onEach { username -> performRefresh(username) }
        .stateIn(viewModelScope, WhileUiSubscribed, _inputUsernameState.value)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<RepoListUiState> = combine(
        _localState,
        githubRepository.getObservableGithubUser(),
        inputUsernameState.flatMapLatest { githubRepository.getObservableRepoList(it) }
    ) { localState, githubUser, repoList ->
        RepoListUiState(
            repoList = repoList.toImmutableList(),
            githubUser = githubUser,
            isLoading = localState.isLoading,
            readRepoList = localState.readRepoList.toImmutableList(),
            onItemClick = ::addReadRepoCount,
            errorMsg = if (repoList.isEmpty() && !localState.isLoading) "未找到仓库" else ""
        )
    }.catch { exception ->
        emit(RepoListUiState(onItemClick = ::addReadRepoCount, errorMsg = exception.message ?: "未知异常"))
    }.stateIn(viewModelScope, WhileUiSubscribed, RepoListUiState(onItemClick = ::addReadRepoCount))

    private fun addReadRepoCount(newReadRepo: Repo) = _localState.update { current ->
        if (current.readRepoList.any { it.name == newReadRepo.name }) {
            current
        } else {
            current.copy(readRepoList = current.readRepoList + newReadRepo)
        }
    }

    fun pullToRefresh() {
        viewModelScope.launch { performRefresh(inputUsernameState.value) }
    }

    fun updateUsername(newUsername: String) {
        _inputUsernameState.value = newUsername
    }

    private suspend fun performRefresh(username: String) = try {
        _localState.update { it.copy(isLoading = true) }
        executeEnsureTime(REPO_LIST_REQUEST_MIN_TIME) {// 保证「异步操作」的「最小」执行时间
            githubRepository.updateRepoList(username)
        }
    } finally {
        _localState.update { it.copy(isLoading = false) }
    }

    private data class LocalState(val isLoading: Boolean = false, val readRepoList: List<Repo> = emptyList())
}