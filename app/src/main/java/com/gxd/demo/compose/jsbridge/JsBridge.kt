package com.gxd.demo.compose.jsbridge

import android.util.Log
import android.webkit.JavascriptInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class JsBridge(private val lifecycleScope: CoroutineScope, private val webView: JsBridgeWebView) {
    @JavascriptInterface
    fun nativeFunc(message: String): String {
        Log.d("ggg", "web call nativeMethod, param = $message")
        return "NativeReturnValue"
    }

    /**
     * 1、「web端」调用「native方法」时回到这里
     * 2、调用「js方法」，「web端」回调时回到这里
     */
    @JavascriptInterface
    fun handleJsMessage(message: String) {
        val jsonObject = JSONObject(message)
        val callbackId = jsonObject.optInt("callbackId")
        val data = jsonObject.optString("data")

        val callJsMethodCallback = webView.getCallJsMethodCallback(callbackId)
        if (callJsMethodCallback != null) {
            callJsMethodCallback.invoke(data)// 调用「js方法」后，执行回调
        } else {
            val methodName = jsonObject.optString("methodName")
            val nativeMethodHandler = webView.getNativeMethodHandler(methodName)

            lifecycleScope.launch {
                val callbackJsonObj = JSONObject()
                callbackJsonObj.put("callbackId", callbackId)
                val returnData = withContext(Dispatchers.IO) { nativeMethodHandler.handle(data) }
                callbackJsonObj.put("data", returnData)
                val json = callbackJsonObj.toString()
                webView.callJsMethod(json)
            }
        }
    }
}