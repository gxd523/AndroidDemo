package com.gxd.demo.compose.architecture.ui.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gxd.demo.compose.architecture.Result
import com.gxd.demo.compose.architecture.repository.GithubRepository
import com.gxd.demo.compose.architecture.repository.Repo
import com.gxd.demo.compose.architecture.uitl.WhileUiSubscribed
import com.gxd.demo.compose.architecture.uitl.executeEnsureTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val githubRepository: GithubRepository,
) : ViewModel() {
    companion object {
        private const val INPUT_DEBOUNCE_TIMEOUT = 1_000L
    }

    private val inputUsernameFlow = MutableStateFlow("gxd523")

    @OptIn(FlowPreview::class)
    private val debouncedInputUsernameFlow = inputUsernameFlow.debounce(
        INPUT_DEBOUNCE_TIMEOUT
    ).stateIn(viewModelScope, WhileUiSubscribed, "")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            debouncedInputUsernameFlow.collect { debouncedNewUsername ->
                githubRepository.updateRepoList(debouncedNewUsername)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val observableRepoList = debouncedInputUsernameFlow.flatMapLatest { newUsername ->
        githubRepository.getObservableRepoList(newUsername).map { handleTask(it) }
    }.catch {
        emit(Result.Error(Exception("异常了")))
    }
    private val isLoading by lazy { MutableStateFlow(false) }
    val uiState = combine(
        isLoading, debouncedInputUsernameFlow, observableRepoList
    ) { isLoading, username, repoListResult ->
        when (repoListResult) {
            is Result.Error -> RepoListUiState(username = username, errorMsg = repoListResult.exception.message ?: "没有异常信息")
            Result.Loading -> RepoListUiState(isLoading = true)
            is Result.Success -> RepoListUiState(repoListResult.data, username, isLoading, errorMsg = "")
        }
    }.stateIn(
        viewModelScope,
        WhileUiSubscribed,
        RepoListUiState(isLoading = false)
    )

    fun pullToRefresh() {
        isLoading.value = true
        viewModelScope.launch {
            executeEnsureTime(500) {
                githubRepository.updateRepoList(debouncedInputUsernameFlow.value)
                isLoading.value = false
            }
        }
    }

    fun updateUsername(newUsername: String) {
        inputUsernameFlow.value = newUsername
    }

    private fun handleTask(repoList: List<Repo>): Result<List<Repo>> {
        if (repoList.isEmpty()) return Result.Error(Exception("repo empty"))
        return Result.Success(repoList)
    }
}