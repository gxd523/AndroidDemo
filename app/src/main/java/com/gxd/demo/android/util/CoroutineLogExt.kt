package com.gxd.demo.android.util

import android.util.Log
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Job.cLog(suffix: String) {
    "isActive = $isActive, isCancelled = $isCancelled, isCompleted = $isCompleted".tableFormat(16).cLog(suffix)
}


fun Any.cLog(result: Any? = null) {
    assembleMsg(result).also(::println)
}

fun Any.aLog(result: Any? = null) {
    val message = assembleMsg(result)
    Log.d("ggg", message)
}

private fun Any.assembleMsg(result: Any?): String {
    val time = SimpleDateFormat("mm:ss:SSS", Locale.getDefault()).format(Date())

    val threadInfo = Thread.currentThread().name
    val lastIndex = threadInfo.lastIndexOf('-')
    val formatThreadInfo = if (lastIndex == -1) {
        threadInfo
    } else {
        val secondLastIndex = threadInfo.lastIndexOf('-', lastIndex - 1)
        threadInfo.substring(secondLastIndex + 1)
    }

    val formatResult = result ?: "null"
    val timeFormat = time.tableFormat(3)
    val threadInfoFormat = formatThreadInfo.tableFormat(9)
    val logFormat = this.toString().tableFormat(4)
    val message = "$timeFormat$threadInfoFormat$logFormat$formatResult"
    return message
}

fun String.tableFormat(tableCount: Int = 5): String {
    val repeatCount = tableCount - this.length / 4
    val separator = "\t".accumulate(if (repeatCount < 0) 0 else repeatCount)
    return "$this$separator"
}

fun String.accumulate(repeatCount: Int): String = if (repeatCount == 0) "" else this + accumulate(repeatCount - 1)