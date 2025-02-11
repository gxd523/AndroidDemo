package com.gxd.demo.compose.jsbridge

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemGesturesPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random


class JsBridgeActivity : ComponentActivity() {
    private val webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val uri = request?.url ?: return false
            Intent(Intent.ACTION_VIEW, uri).let(::startActivity)
            return true
        }

        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
            val uri = request?.url ?: return null
            if (uri.toString().endsWith(".png")) {
                return WebResourceResponse("image/png", "uft-8", assets.open("test.png"))
            }
            return super.shouldInterceptRequest(view, request)
        }
    }
    private val webView by lazy {
        JsBridgeWebView(this).apply {
            @SuppressLint("SetJavaScriptEnabled")
            settings.javaScriptEnabled = true

            @SuppressLint("JavascriptInterface")
            addJavascriptInterface(JsBridge(this@JsBridgeActivity.lifecycleScope, this), "JsBridge")

            this.webViewClient = this@JsBridgeActivity.webViewClient

            registerNativeMethod("nativeMethodA") { data ->
                withContext(Dispatchers.IO) {
                    delay(5_000)
                    "${data}->${Random.nextInt(1000)}"
                }
            }
            registerNativeMethod("nativeMethodB") { data ->
                "${data}->${Random.nextInt(1000)}"
            }

            loadUrl("file:///android_asset/test_js_bridge.html")
//            loadUrl("https://m.baidu.com")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(Modifier.systemGesturesPadding().fillMaxSize()) {
                Button({
                    webView.evaluateJavascript("javascript:window.jsFunc('Hello')") {
                        Log.d("ggg", "jsFunc return = $it")
                    }
                }) {
                    Text("调用js方法")
                }
                Button({
                    webView.callJsMethod("webMethodA", "aaa") {
                        Log.d("ggg", "jsFunc return = $it")
                    }
                }) {
                    Text("调用JsBridge的webMethodA")
                }
                Button({
                    webView.callJsMethod("webMethodB", "bbb") {
                        Log.d("ggg", "jsFunc return = $it")
                    }
                }) {
                    Text("调用JsBridge的webMethodB")
                }
                AndroidView({ webView })
            }
        }
    }
}