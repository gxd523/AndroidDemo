package com.gxd.demo.compose.jsbridge

fun interface NativeMethodHandler {
    suspend fun handle(data: String): String
}