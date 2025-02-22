package com.gxd.demo.android

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.gxd.demo.android.util.screenHeightPercent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text("郭晓东")
            AndroidView({
                WebView(this).also {
                    it.loadUrl("file:///android_asset/test_app_links.html")
                }
            }, Modifier.statusBarsPadding().screenHeightPercent(50)) {}
        }
    }
}