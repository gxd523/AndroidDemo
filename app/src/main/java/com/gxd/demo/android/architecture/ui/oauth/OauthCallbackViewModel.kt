package com.gxd.demo.android.architecture.ui.oauth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gxd.demo.lib.dal.repository.GithubRepository
import com.gxd.demo.lib.dal.source.network.model.GithubUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OauthCallbackViewModel @Inject constructor(private val githubRepository: GithubRepository) : ViewModel() {
    val uiState: StateFlow<GithubUser?>
        field = MutableStateFlow<GithubUser?>(null)

    fun getAccessToken(authorizationCode: String, redirectUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value = githubRepository.requestGithubUser(authorizationCode, redirectUrl)
        }
    }
}