package com.gxd.demo.compose.wechat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gxd.demo.compose.ui.theme.MyTheme
import com.gxd.demo.compose.wechat.HomeViewModel
import com.gxd.demo.compose.wechat.data.Chat
import com.gxd.demo.compose.wechat.data.mock.Mock

@Composable
fun ChatList(chatList: List<Chat>) {
    Column(Modifier.fillMaxSize().background(MyTheme.colorScheme.background)) {
        TopBar("微信")
        LazyColumn(Modifier.background(MyTheme.colorScheme.listItem)) {
            itemsIndexed(chatList) { index, chat ->
                ChatItem(chat)
                if (index >= chatList.lastIndex) return@itemsIndexed
                HorizontalDivider(Modifier.padding(start = 68.dp), 0.8f.dp, MyTheme.colorScheme.divider)
            }
        }
    }
}

@Composable
private fun ChatItem(chat: Chat) {
    val viewModel: HomeViewModel = viewModel()
    Row(Modifier.clickable { viewModel.startChat(chat) }) {
        Image(
            painterResource(chat.friend.avatar), chat.friend.name,
            Modifier
                .padding(8.dp)
                .size(48.dp)
                .unread(!(chat.messageList.firstOrNull()?.read ?: true), MyTheme.colorScheme.badge)
                .clip(RoundedCornerShape(4.dp))
        )
        val newestMessage = chat.messageList.firstOrNull() ?: return
        Column(Modifier.weight(1f).align(Alignment.CenterVertically)) {
            Text(chat.friend.name, fontSize = 17.sp, color = MyTheme.colorScheme.textPrimary)
            Text(newestMessage.text, fontSize = 14.sp, color = MyTheme.colorScheme.textSecondary)
        }
        Text(
            newestMessage.time,
            Modifier.padding(8.dp, 8.dp, 12.dp, 8.dp),
            fontSize = 11.sp, color = MyTheme.colorScheme.textSecondary
        )
    }
}

fun Modifier.unread(show: Boolean, color: Color): Modifier = this.drawWithContent {
    drawContent()
    if (!show) return@drawWithContent
    val offset = Offset(size.width - 1.dp.toPx(), 1.dp.toPx())
    drawCircle(color, 5.dp.toPx(), offset)
}

@Preview(showBackground = true)
@Composable
private fun ChatListPreview() {
    ChatList(Mock.chatList)
}
