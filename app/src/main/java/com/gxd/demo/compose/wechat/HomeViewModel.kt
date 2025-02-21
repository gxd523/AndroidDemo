package com.gxd.demo.compose.wechat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gxd.demo.compose.ui.theme.WechatTheme
import com.gxd.demo.compose.util.format
import com.gxd.demo.compose.wechat.data.Chat
import com.gxd.demo.compose.wechat.data.ChatMessage
import com.gxd.demo.compose.wechat.data.User
import com.gxd.demo.compose.wechat.data.mock.Mock
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _chatList by lazy { MutableStateFlow(emptyList<Chat>()) }
    val chatList: StateFlow<List<Chat>> by lazy { _chatList }
    val contactList by mutableStateOf(Mock.contactList)
    var selectedTab by mutableIntStateOf(0)
    var theme by mutableStateOf(WechatTheme.Theme.Light)
    var currentChat by mutableStateOf<Chat?>(null)
    var inChatPage by mutableStateOf(false)
    var isReady by mutableStateOf(false)

    fun startChat(chat: Chat) {
        currentChat = chat
        inChatPage = true
    }

    fun endChat(): Boolean {
        if (inChatPage) {
            inChatPage = false
            return true
        } else {
            return false
        }
    }

    fun boom(chat: Chat) {
        sendMsg("\uD83D\uDCA3", chat)
    }

    fun sendMsg(newMsg: String, chat: Chat) {
        val time = System.currentTimeMillis().format("hh:mm")
        val newMessage = ChatMessage(User.Me, newMsg, time).apply { read = true }
        chat.messageList.add(0, newMessage)
    }

    fun requestChatList() {
        viewModelScope.launch {
            delay(1_000)
            _chatList.value = Mock.chatList
            isReady = true
        }
    }
}