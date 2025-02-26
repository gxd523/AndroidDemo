package com.gxd.demo.android.architecture.ui.oauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OauthLoginActivity : ComponentActivity() {
    private val viewModel by viewModels<OauthCallbackViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.handleOauthCallback()

        lifecycleScope.launch {
            viewModel.uiState.collect {
                if (it == null) return@collect
                finish()
            }
        }
    }

    private fun Intent.handleOauthCallback() {
        val uri = data ?: return
        val authorizationCode = data?.getQueryParameter("code") ?: run {
            Log.e("ggg", "Authorization failed: ${uri.getQueryParameter("error")}")
            return
        }
        viewModel.getAccessToken(authorizationCode, "${uri.scheme}://${uri.host}${uri.path}")
    }
}