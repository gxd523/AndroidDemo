package com.gxd.demo.android.wechat.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gxd.demo.android.R
import com.gxd.demo.android.ui.theme.WechatTheme

@Composable
fun BottomBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    Row(Modifier.background(WechatTheme.colorScheme.bottomBar).navigationBarsPadding()) {
        listOf(
            R.drawable.ic_chat_outlined to R.drawable.ic_chat_filled to "聊天",
            R.drawable.ic_contacts_outlined to R.drawable.ic_contacts_filled to "通讯录",
            R.drawable.ic_discovery_outlined to R.drawable.ic_discovery_filled to "发现",
            R.drawable.ic_me_outlined to R.drawable.ic_me_filled to "我"
        ).mapIndexed { index, (iconIdPair, title) ->
            val isSelected = selectedItem == index
            val iconId = if (isSelected) iconIdPair.second else iconIdPair.first
            val tintColor = if (isSelected) WechatTheme.colorScheme.iconCurrent else WechatTheme.colorScheme.icon
            TabItem(iconId, title, tintColor, Modifier.weight(1f).clickable { onItemSelected(index) })
        }
    }
}

@Composable
fun TabItem(@DrawableRes iconId: Int, title: String, tint: Color, modifier: Modifier = Modifier) {
    Column(modifier.padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(painterResource(iconId), title, Modifier.size(24.dp), tint)
        Text(title, fontSize = 11.sp, color = tint)
    }
}

@Preview(showBackground = true)
@Composable
private fun TabItemPreview() {
    TabItem(iconId = R.drawable.ic_chat_outlined, title = "聊天", tint = WechatTheme.colorScheme.icon)
}

@Preview(showBackground = true)
@Composable
private fun BottomBarPreview() {
    WechatTheme(WechatTheme.Theme.Dark) {
        var selectedTab by remember { mutableIntStateOf(0) }
        BottomBar(selectedTab) { selectedTab = it }
    }
}