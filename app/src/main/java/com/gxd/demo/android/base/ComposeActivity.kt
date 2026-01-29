package com.gxd.demo.android.base

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.gxd.demo.android.compose.wechat.theme.WechatTheme

abstract class ComposeActivity : ComponentActivity() {
    var selectedTheme by mutableStateOf<WechatTheme.Theme?>(null)
        private set
    private lateinit var composeScreen: @Composable () -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) window.isNavigationBarContrastEnforced = false
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        setContent {
            // 切换系统主题后，activity会销毁重建
            val activityTheme =
                selectedTheme ?: if (isSystemInDarkTheme()) WechatTheme.Theme.Dark else WechatTheme.Theme.Light

            WechatTheme(activityTheme) {
                val isForegroundDark = activityTheme == WechatTheme.Theme.Light
                insetsController.isAppearanceLightStatusBars = isForegroundDark
                insetsController.isAppearanceLightNavigationBars = isForegroundDark
                if (::composeScreen.isInitialized) Box(Modifier.systemBarsPadding().fillMaxSize()) { composeScreen() }
            }
        }
    }

    protected fun setComposeContent(content: @Composable () -> Unit) {
        composeScreen = content
    }

    fun changeTheme(theme: WechatTheme.Theme) {
        selectedTheme = theme
    }
}