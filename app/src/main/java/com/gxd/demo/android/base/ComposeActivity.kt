package com.gxd.demo.android.base

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.gxd.demo.android.compose.wechat.theme.WechatTheme

abstract class ComposeActivity : ComponentActivity() {
    protected open val composeTheme: WechatTheme.Theme = WechatTheme.Theme.Light
    private lateinit var composeScreen: @Composable () -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) window.isNavigationBarContrastEnforced = false
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        setContent {
            WechatTheme(composeTheme) {
                val isForegroundDark = composeTheme == WechatTheme.Theme.Light
                insetsController.isAppearanceLightStatusBars = isForegroundDark
                insetsController.isAppearanceLightNavigationBars = isForegroundDark
                if (::composeScreen.isInitialized) Box(Modifier.systemBarsPadding().fillMaxSize()) { composeScreen() }
            }
        }
    }

    protected fun setComposeContent(content: @Composable () -> Unit) {
        composeScreen = content
    }
}