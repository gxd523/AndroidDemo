package com.gxd.demo.android.compose.wechat.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ChatMessage(val from: User, val text: String, val time: String) {
    var read by mutableStateOf(true)
}