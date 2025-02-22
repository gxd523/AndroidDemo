package com.gxd.demo.android.architecture.ui.repo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.gxd.demo.android.compose.wechat.theme.WechatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepoListActivity : ComponentActivity() {
    private val viewModel by viewModels<RepoListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WechatTheme(WechatTheme.Theme.Dark) {
                RepoListScreen()
            }
        }
    }
}