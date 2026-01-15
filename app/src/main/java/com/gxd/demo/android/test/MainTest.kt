package com.gxd.demo.android.test

import com.gxd.demo.android.util.cLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        "start".cLog()
        test()
        "end".cLog()
    }
}

private suspend fun CoroutineScope.test() {

}

@OptIn(DelicateCoroutinesApi::class)
val dispatcherA = newFixedThreadPoolContext(3, "aaa")

@OptIn(DelicateCoroutinesApi::class)
val dispatcherB = newFixedThreadPoolContext(3, "bbb")