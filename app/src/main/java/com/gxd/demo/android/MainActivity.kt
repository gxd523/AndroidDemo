package com.gxd.demo.android

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.gxd.demo.android.plugin.MyPlugin
import com.gxd.demo.android.util.screenHeightPercent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        lifecycleScope.launch { testPlugin() }
    }

    /**
     * 测试「热修复」、「插件化」
     * 创建「MyPlugin」对象的地方不能和「mergeDex」在同一个类中
     * 否则「MyPlugin」会被提前加载
     */
    private suspend fun testPlugin() = withContext(Dispatchers.IO) {
        val myPlugin = MyPlugin()
        Log.d("ggg", "${Thread.currentThread().name}插件化 = ${myPlugin.name}")
    }
}