package com.gxd.demo.compose.wechat

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.core.view.WindowCompat
import com.gxd.demo.compose.ui.theme.WechatTheme
import com.gxd.demo.compose.wechat.ui.ChatPage
import com.gxd.demo.compose.wechat.ui.HomePage

class HomeActivity : ComponentActivity() {
    private val viewModel by viewModels<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) window.isNavigationBarContrastEnforced = false
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        setContent {
            WechatTheme(viewModel.theme) {
                val isForegroundDark = viewModel.theme == WechatTheme.Theme.Light
                insetsController.isAppearanceLightStatusBars = isForegroundDark
                insetsController.isAppearanceLightNavigationBars = isForegroundDark
                Box {
                    HomePage()
                    ChatPage()
                }
                BackHandler { if (!viewModel.endChat()) finish() }
            }
        }
    }
}