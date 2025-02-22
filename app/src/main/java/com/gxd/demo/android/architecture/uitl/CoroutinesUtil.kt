/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gxd.demo.android.architecture.uitl

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration

private const val StopTimeoutMillis: Long = 5000

/**
 * A [SharingStarted] meant to be used with a [StateFlow] to expose data to the UI.
 *
 * When the UI stops observing, upstream flows stay active for some time to allow the system to
 * come back from a short-lived configuration change (such as rotations). If the UI stops
 * observing for longer, the cache is kept but the upstream flows are stopped. When the UI comes
 * back, the latest value is replayed and the upstream flows are executed again. This is done to
 */
val WhileUiSubscribed: SharingStarted = SharingStarted.Companion.WhileSubscribed(StopTimeoutMillis)

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