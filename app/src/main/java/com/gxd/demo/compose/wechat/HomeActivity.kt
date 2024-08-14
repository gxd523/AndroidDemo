package com.gxd.demo.compose.wechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import com.gxd.demo.compose.ui.theme.MyTheme
import com.gxd.demo.compose.wechat.ui.ChatPage
import com.gxd.demo.compose.wechat.ui.HomePage

class HomeActivity : ComponentActivity() {
    private val viewModel by viewModels<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyTheme(viewModel.theme) {
                Box {
                    HomePage()
                    ChatPage()
                }
                BackHandler { if (!viewModel.endChat()) finish() }
            }
        }
    }
}