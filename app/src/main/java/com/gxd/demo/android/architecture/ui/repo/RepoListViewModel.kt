package com.gxd.demo.android.architecture.ui.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gxd.demo.android.architecture.Result
import com.gxd.demo.android.util.WhileUiSubscribed
import com.gxd.demo.android.util.executeEnsureTime
import com.gxd.demo.lib.dal.repository.GithubRepository
import com.gxd.demo.lib.dal.repository.Repo
import com.gxd.demo.lib.dal.source.network.model.GithubUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(private val githubRepository: GithubRepository) : ViewModel() {
    companion object {
        private const val INPUT_DEBOUNCE_TIMEOUT = 1_500L
        private const val REPO_LIST_REQUEST_MIN_TIME = 300
    }

    private val _inputUsernameState = MutableStateFlow("gxd523")
    private val _uiState = MutableStateFlow(
        RepoListUiState(onItemClick = ::addReadRepoCount)
    )

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val inputUsernameState = _inputUsernameState.debounce(
        INPUT_DEBOUNCE_TIMEOUT
    ).distinctUntilChanged(
    ).filter {
        it.isNotBlank()
    }.transformLatest { username ->
        updateRepoList(username)
        emit(username)
    }.stateIn(// 通过「stateIn」将「冷流」转换成「热流」即「StateFlow」，「StateFlow」也作为了「一级缓存(内存缓存)」
        viewModelScope, WhileUiSubscribed, ""
    )

    val uiState: StateFlow<RepoListUiState> = combine(
        githubRepository.getObservableGithubUser(),
        githubRepository.getObservableRepoList(inputUsernameState.value)
    ) { githubUser, repoList ->
        combineToResult(githubUser, repoList)
    }.catch {
        emit(Result.Error(Exception("异常了")))
    }.map { result ->
        val currentRepoListUiState = _uiState.value
        when (result) {
            is Result.Error -> currentRepoListUiState.copy(
                repoList = persistentListOf(),
                readRepoList = persistentListOf(),
                errorMsg = result.exception.message ?: "没有异常信息"
            )

            Result.Loading -> currentRepoListUiState
            is Result.Success -> {
                val (newGithubUser, newRepoList) = result.data
                currentRepoListUiState.copy(
                    repoList = newRepoList.toImmutableList(),
                    githubUser = newGithubUser,
                    errorMsg = ""
                )
            }
        }
    }.stateIn(viewModelScope, WhileUiSubscribed, _uiState.value)


    private fun addReadRepoCount(repo: Repo): Unit = _uiState.update { repoListUiState ->
        repoListUiState.copy(readRepoList = repoListUiState.readRepoList.toMutableList().also { newReadRepoList ->
            if (!newReadRepoList.any { repoItem -> repo.name == repoItem.name }) newReadRepoList.add(repo)
        }.toImmutableList())
    }

    fun pullToRefresh() {
        viewModelScope.launch { updateRepoList(inputUsernameState.value) }
    }

    fun updateUsername(newUsername: String) {
        _inputUsernameState.value = newUsername
    }

    private suspend fun updateRepoList(username: String) = try {
        _uiState.update { it.copy(isLoading = true) }
        executeEnsureTime(REPO_LIST_REQUEST_MIN_TIME) {// 保证「异步操作」的「最小」执行时间
            githubRepository.updateRepoList(username)
        }
    } finally {
        _uiState.update { it.copy(isLoading = false) }
    }

    private fun combineToResult(githubUser: GithubUser?, repoList: List<Repo>): Result<Pair<GithubUser?, List<Repo>>> {
        if (repoList.isEmpty()) return Result.Error(Exception("repo empty"))
        return Result.Success(githubUser to repoList)
    }
}