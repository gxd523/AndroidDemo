package com.gxd.demo.android.architecture.ui.oauth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gxd.demo.lib.dal.repository.GithubRepository
import com.gxd.demo.lib.dal.source.network.model.GithubUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OauthCallbackViewModel @Inject constructor(private val githubRepository: GithubRepository) : ViewModel() {
    private val _uiState by lazy { MutableStateFlow<GithubUser?>(null) }
    val uiState = _uiState

    fun getAccessToken(authorizationCode: String, redirectUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = githubRepository.requestGithubUser(authorizationCode, redirectUrl)
        }
    }
}