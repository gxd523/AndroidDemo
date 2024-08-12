package com.gxd.demo.compose.wechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.gxd.demo.compose.ui.theme.MyTheme
import com.gxd.demo.compose.wechat.ui.HomeScreen

class HomeActivity : ComponentActivity() {
    private val viewModel by viewModels<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyTheme(viewModel.theme) {
                HomeScreen(viewModel)
            }
        }
    }
}