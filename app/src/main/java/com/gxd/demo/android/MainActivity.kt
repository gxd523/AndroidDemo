package com.gxd.demo.android

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.gxd.demo.android.plugin.MyPlugin
import com.gxd.demo.android.util.screenHeightPercent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : ComponentActivity() {
    private val channelFlowBtn by lazy {
        Button(this@MainActivity).apply {
            text = "测试channelFlow"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(Modifier.statusBarsPadding()) {
                AndroidView({ context ->
                    WebView(context).also {
                        it.loadUrl("file:///android_asset/test_app_links.html")
                    }
                }, Modifier.screenHeightPercent(30))
                AndroidView({ channelFlowBtn })
            }
        }

        lifecycleScope.launch { pluginTest() }

        channelFlowTest()
    }

    private fun channelFlowTest() {
        val channelFlowJob = lifecycleScope.launch {
            val flow = channelFlowBtn.buttonClickFlow().throttle(500.milliseconds)
            flow.collect {
                Log.d("ggg", "channelFlow Btn click event")
            }
        }

        lifecycleScope.launch {
            delay(10_000)
            channelFlowJob.cancel()
        }
    }

    /**
     * 自定义Flow操作符实现按钮防抖
     * 「debounce」更适合「搜索」场景
     */
    fun <T> Flow<T>.throttle(time: Duration): Flow<T> = flow {
        var lastTime = 0L
        this@throttle.collect {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTime > time.inWholeMilliseconds) {
                emit(it)
                lastTime = currentTime
            }
        }
    }

    /**
     * 「callbackFlow」是「channelFlow」实现的
     */
    fun Button.buttonClickFlow(): Flow<Unit> = callbackFlow {
        val listener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                trySend(Unit)
            }
        }

        this@buttonClickFlow.setOnClickListener(listener)
        awaitClose {
            Log.d("ggg", "awaitClose")
            this@buttonClickFlow.setOnClickListener(null)
        }
    }

    /**
     * 测试「热修复」、「插件化」
     * 创建「MyPlugin」对象的地方不能和「mergeDex」在同一个类中
     * 否则「MyPlugin」会被提前加载
     */
    private suspend fun pluginTest() = withContext(Dispatchers.IO) {
        val myPlugin = MyPlugin()
        Log.d("ggg", "${Thread.currentThread().name}插件化 = ${myPlugin.name}")
    }
}