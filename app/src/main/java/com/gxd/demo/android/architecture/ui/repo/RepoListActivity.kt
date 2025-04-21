package com.gxd.demo.android.architecture.ui.repo

import android.os.Bundle
import androidx.activity.viewModels
import com.gxd.demo.android.base.ComposeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepoListActivity : ComposeActivity() {
    private val viewModel by viewModels<RepoListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setComposeContent { RepoListScreen() }
    }
}