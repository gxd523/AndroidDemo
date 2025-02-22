package com.gxd.demo.android.wechat.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gxd.demo.android.R
import com.gxd.demo.android.ui.theme.WechatTheme
import com.gxd.demo.android.wechat.data.User

@Composable
fun MePage() {
    val itemList = remember {
        listOf(
            R.drawable.ic_collections to "收藏",
            R.drawable.ic_photos to "朋友圈",
            R.drawable.ic_cards to "卡包",
            R.drawable.ic_stickers to "表情"
        )
    }
    Box(Modifier.fillMaxSize().background(WechatTheme.colorScheme.background)) {
        LazyColumn(Modifier.background(WechatTheme.colorScheme.listItem).fillMaxWidth()) {
            item {
                MeHeader()
                MeItem(
                    R.drawable.ic_pay, "支付",
                    Modifier
                        .background(WechatTheme.colorScheme.background)
                        .padding(vertical = 8.dp)
                        .background(WechatTheme.colorScheme.listItem)
                )
            }
            itemsIndexed(itemList) { index, item ->
                MeItem(item.first, item.second)
                if (index >= itemList.lastIndex) return@itemsIndexed
                HorizontalDivider(Modifier.padding(start = 56.dp), color = WechatTheme.colorScheme.divider, thickness = 0.8f.dp)
            }
            item {
                MeItem(
                    R.drawable.ic_settings, "设置",
                    Modifier.background(WechatTheme.colorScheme.background).padding(top = 8.dp).background(WechatTheme.colorScheme.listItem)
                )
            }
        }
    }
}

@Composable
fun MeItem(
    @DrawableRes icon: Int,
    title: String,
    modifier: Modifier = Modifier,
    badge: @Composable (() -> Unit)? = null,
    endBadge: @Composable (() -> Unit)? = null
) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painterResource(icon), "title",
            Modifier.padding(12.dp, 8.dp, 8.dp, 8.dp).size(36.dp).padding(8.dp)
        )
        Text(title, fontSize = 17.sp, lineHeight = 17.sp, color = WechatTheme.colorScheme.textPrimary)
        badge?.invoke()
        Spacer(Modifier.weight(1f))
        endBadge?.invoke()
        Icon(
            painterResource(R.drawable.ic_arrow_more), contentDescription = "更多",
            Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp).size(16.dp),
            tint = WechatTheme.colorScheme.more
        )
    }
}

@Composable
fun MeHeader() {
    Row(Modifier.background(WechatTheme.colorScheme.listItem).fillMaxWidth().statusBarsPadding().height(224.dp)) {
        Image(
            painterResource(id = R.drawable.avatar_rengwuxian), contentDescription = "Me",
            Modifier.align(Alignment.CenterVertically).padding(start = 24.dp).clip(RoundedCornerShape(6.dp)).size(64.dp)
        )
        Column(Modifier.weight(1f).padding(start = 12.dp)) {
            Text(
                User.Me.name,
                Modifier.padding(top = 64.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = WechatTheme.colorScheme.textPrimary
            )
            Text(
                "微信号：${User.Me.id}",
                Modifier.padding(top = 16.dp),
                fontSize = 14.sp,
                color = WechatTheme.colorScheme.textSecondary
            )
            Text(
                "+ 状态",
                Modifier
                    .padding(top = 16.dp)
                    .border(1.dp, WechatTheme.colorScheme.onBackground, RoundedCornerShape(50))
                    .padding(8.dp, 2.dp),
                fontSize = 16.sp,
                color = WechatTheme.colorScheme.onBackground
            )
        }
        Icon(
            painterResource(id = R.drawable.ic_qrcode), contentDescription = "qrcode",
            Modifier.align(Alignment.CenterVertically).padding(end = 20.dp).size(14.dp),
            tint = WechatTheme.colorScheme.onBackground
        )
        Icon(
            painterResource(R.drawable.ic_arrow_more), contentDescription = "更多",
            Modifier.align(Alignment.CenterVertically).padding(end = 16.dp).size(16.dp),
            tint = WechatTheme.colorScheme.more
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MeHeaderPreview() {
    MeHeader()
}

@Preview(showBackground = true)
@Composable
private fun MeItemPreview() {
    MeItem(R.drawable.ic_pay, "支付")
}

@Preview(showBackground = true)
@Composable
private fun MePagePreview() {
    MePage()
}