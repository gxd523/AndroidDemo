package com.gxd.demo.compose.util

import java.text.SimpleDateFormat
import java.util.Locale

private object DateFormatObj {
    val defaultDateFormat by lazy { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) }
}

fun Long.format(pattern: String? = null): String = if (pattern.isNullOrEmpty()) {
    DateFormatObj.defaultDateFormat
} else {
    SimpleDateFormat(pattern, Locale.getDefault())
}.format(this)