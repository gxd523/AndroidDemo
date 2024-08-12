package com.gxd.demo.compose.wechat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.gxd.demo.compose.wechat.data.mock.Mock

class HomeViewModel : ViewModel() {
    var chatList by mutableStateOf(Mock.chatList)
    val contactList by mutableStateOf(Mock.contactList)
    var selectedTab by mutableStateOf(0)
}