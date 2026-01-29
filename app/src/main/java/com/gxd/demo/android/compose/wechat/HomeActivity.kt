package com.gxd.demo.android.compose.wechat

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import androidx.activity.compose.BackHandler
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.core.animation.doOnEnd
import com.gxd.demo.android.base.ComposeActivity
import com.gxd.demo.android.compose.wechat.ui.ChatPage
import com.gxd.demo.android.compose.wechat.ui.HomePage

class HomeActivity : ComposeActivity() {
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setComposeContent {
            Box {
                HomePage()
                ChatPage()
            }
            BackHandler { if (!viewModel.endChat()) finish() }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) handleSplashScreen()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun handleSplashScreen() {
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_Y,
                0f,
                -splashScreenView.height.toFloat()
            )
            slideUp.interpolator = AnticipateInterpolator()
            slideUp.duration = 800L

            // Call SplashScreenView.remove at the end of your custom animation.
            slideUp.doOnEnd { splashScreenView.remove() }

            // Run your animation.
            slideUp.start()
        }

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean = viewModel.isReady.also { isReady ->
                if (isReady) content.viewTreeObserver.removeOnPreDrawListener(this)
            }
        })
    }
}