package com.gxd.demo.android.jsbridge

fun interface NativeMethodHandler {
    suspend fun handle(data: String): String
}