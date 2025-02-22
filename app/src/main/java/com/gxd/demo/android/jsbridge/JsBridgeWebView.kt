package com.gxd.demo.android.jsbridge

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.webkit.WebView
import org.json.JSONObject

class JsBridgeWebView(context: Context, attrs: AttributeSet? = null) : WebView(context, attrs) {
    /**
     * 「native方法」通过注册「函数名」、「callback」实现
     */
    private val nativeMethodMap = mutableMapOf<String, NativeMethodHandler>()

    /**
     * 调用「js方法」时存储的「id」和「callback」
     */
    private val callJsMethodCallbackMap = mutableMapOf<Int, (String) -> Unit>()
    private var callJsMethodCallbackId = 9000

    fun registerNativeMethod(handlerName: String, handler: NativeMethodHandler) {
        nativeMethodMap[handlerName] = handler
    }

    fun getNativeMethodHandler(methodName: String) = nativeMethodMap.getOrElse(methodName) {
        throw Exception("native未注册${methodName}方法")
    }

    /**
     * 调用「js方法」
     */
    fun callJsMethod(methodName: String, data: String, callback: (String) -> Unit) {
        val json = JSONObject().apply {
            put("data", data)
            put("methodName", methodName)
            put("callbackId", --callJsMethodCallbackId)
        }.toString()
        Log.d("ggg", "「native端」调用「js方法」时的数据 = $json")
        callJsMethodCallbackMap[callJsMethodCallbackId] = callback
        callJsMethod(json)
    }

    /**
     * 1、「web端」调用「native方法」后回调回「web端」
     * 2、调用「js方法」
     */
    fun callJsMethod(json: String) {
        Log.d("ggg", "JsBridgeWebView.callJsMethod...${Thread.currentThread().name}")
        // 要执行在主线程
        evaluateJavascript("javascript:window.handleNativeMessage('$json')") {
            Log.e("JsBridge", "receive from web: $it")
        }
    }

    /**
     * 获取调用「js方法」的回调
     */
    fun getCallJsMethodCallback(callbackId: Int): ((String) -> Unit)? {
        val callback = callJsMethodCallbackMap[callbackId] ?: return null
        callJsMethodCallbackMap.remove(callbackId)
        return callback
    }
}