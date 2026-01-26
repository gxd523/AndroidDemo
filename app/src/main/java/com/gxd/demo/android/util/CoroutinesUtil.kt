package com.gxd.demo.android.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.time.Duration

private const val StopTimeoutMillis: Long = 5000

val WhileUiSubscribed = SharingStarted.WhileSubscribed(StopTimeoutMillis)

suspend fun <T> executeEnsureTime(millisTime: Int, block: suspend () -> T): T {
    val start = System.currentTimeMillis()
    val result = block.invoke()
    val executeTime = System.currentTimeMillis() - start
    if (executeTime >= millisTime) return result
    delay(millisTime - executeTime)
    return result
}

/**
 * 防止按钮频繁点击
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
 * 测试方法，让「Thread」的「sleep」支持「挂起」
 */
suspend fun threadSleep(millis: Long) = suspendCancellableCoroutine<Unit> { continuation ->
    val sleepThread = thread {
        try {
            Thread.sleep(millis)
            continuation.resume(Unit)
        } catch (e: InterruptedException) {
            // 当协程被取消后，任何后续的异常（如 InterruptedException）会被视为取消的结果
            // 所以以下代码无意义
            // continuation.resumeWithException(e)
        }
    }

    continuation.invokeOnCancellation { sleepThread.interrupt() }
}