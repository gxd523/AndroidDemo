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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(private val githubRepository: GithubRepository) : ViewModel() {
    companion object {
        private const val INPUT_DEBOUNCE_TIMEOUT = 1_000L
        private const val REPO_LIST_REQUEST_MIN_TIME = 300
    }

    private val _uiState = MutableStateFlow(RepoListUiState(onItemClick = ::addReadRepoCount))
    val uiState: StateFlow<RepoListUiState> = _uiState

    private val _inputUsernameFlow = MutableStateFlow("")
    val inputUsernameState: StateFlow<String> = _inputUsernameFlow

    @OptIn(FlowPreview::class)
    private val debouncedInputUsernameFlow = _inputUsernameFlow.debounce(
        INPUT_DEBOUNCE_TIMEOUT
    ).stateIn(viewModelScope, WhileUiSubscribed, "")

    private val _isLoading = MutableStateFlow(false)
    val loadingUiState: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch(Dispatchers.IO) {
            debouncedInputUsernameFlow.collect { debouncedNewUsername ->
                if (debouncedNewUsername.isEmpty()) return@collect
                _isLoading.value = true
                executeEnsureTime(REPO_LIST_REQUEST_MIN_TIME) {
                    githubRepository.updateRepoList(debouncedNewUsername)
                    _isLoading.value = false
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            @OptIn(ExperimentalCoroutinesApi::class)
            val flatMapLatest = debouncedInputUsernameFlow.flatMapLatest { newUsername ->
                githubRepository.getObservableRepoList(newUsername)
            }
            combine(githubRepository.getObservableGithubUser(), flatMapLatest) { githubUser, repoList ->
                handleTask(githubUser, repoList)
            }.catch {
                emit(Result.Error(Exception("异常了")))
            }.collect { result ->
                _uiState.update {
                    when (result) {
                        is Result.Error -> it.copy(
                            repoList = emptyList(),
                            readRepoList = emptyList(),
                            errorMsg = result.exception.message ?: "没有异常信息"
                        )

                        Result.Loading -> it
                        is Result.Success -> it.copy(
                            repoList = result.data.second,
                            githubUser = result.data.first,
                            readRepoList = readRepoList.value,
                            errorMsg = ""
                        )
                    }
                }
            }
        }
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    private val observableRepoList = debouncedInputUsernameFlow.flatMapLatest { newUsername ->
//        githubRepository.getObservableRepoList(newUsername).map { handleTask(it) }
//    }.catch {
//        emit(Result.Error(Exception("异常了")))
//    }

    private val readRepoList = MutableStateFlow(mutableListOf<Repo>())
    private fun addReadRepoCount(repo: Repo) {
        readRepoList.update {
            it.toMutableList().also { newReadRepoList ->
                if (!newReadRepoList.any { repo.name == it.name }) {
                    newReadRepoList.add(repo)
                    _uiState.update { it.copy(readRepoList = newReadRepoList) }
                }
            }
        }
    }

    /**
     * 不相关、更新频率不同的数据项，也可以拆分成多个「uiState」
     * 尤其是当其中某个状态的更新频率高于其他状态的更新频率时
     */
//    val uiStateX = combine(
//        debouncedInputUsernameFlow, observableRepoList, readRepoList
//    ) { username, repoListResult, readRepoList ->
//        when (repoListResult) {
//            is Result.Error -> RepoListUiState(
//                username = username,
//                errorMsg = repoListResult.exception.message ?: "没有异常信息",
//                onItemClick = ::addReadRepoCount,
//                readRepoList = readRepoList
//            )
//
//            Result.Loading -> RepoListUiState(onItemClick = ::addReadRepoCount, readRepoList = readRepoList)
//
//            is Result.Success -> RepoListUiState(
//                repoListResult.data,
//                username,
//                errorMsg = "",
//                onItemClick = ::addReadRepoCount,
//                readRepoList = readRepoList
//            )
//        }
//    }.distinctUntilChanged { old, new ->
//        old.repoList == new.repoList && old.errorMsg == new.errorMsg && old.readRepoList == new.readRepoList
//    }.stateIn(
//        viewModelScope, WhileUiSubscribed, RepoListUiState()
//    )

    fun pullToRefresh() {
        _isLoading.value = true
        viewModelScope.launch {
            executeEnsureTime(REPO_LIST_REQUEST_MIN_TIME) {
                githubRepository.updateRepoList(debouncedInputUsernameFlow.value)
                _isLoading.value = false
            }
        }
    }

    fun updateUsername(newUsername: String) {
        _inputUsernameFlow.value = newUsername
    }

    private fun handleTask(githubUser: GithubUser?, repoList: List<Repo>): Result<Pair<GithubUser?, List<Repo>>> {
        if (repoList.isEmpty()) return Result.Error(Exception("repo empty"))
        return Result.Success(githubUser to repoList)
    }
}