package com.gxd.demo.android.jsbridge

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
     * 1、「web端」调用「native方法」时会先到「handleJsMessage」，再调用「web端」的「handleNativeMessage」回调回去
     * 2、「native端」调用「web端」时先到「web端」的「handleNativeMessage」，再调用「native端」的「handleJsMessage」回调回去
     */
    @JavascriptInterface
    fun handleJsMessage(message: String) {
        val jsonObject = JSONObject(message)
        val callbackId = jsonObject.optInt("callbackId")
        val data = jsonObject.optString("data")

        val callJsMethodCallback = webView.getCallJsMethodCallback(callbackId)
        if (callJsMethodCallback != null) {// 「native端」调用「js方法」后，执行回调
            callJsMethodCallback.invoke(data)
        } else {// 「web端」调用「native方法」
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