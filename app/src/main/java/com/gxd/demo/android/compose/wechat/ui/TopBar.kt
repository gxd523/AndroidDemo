package com.gxd.demo.android.compose.wechat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gxd.demo.android.R
import com.gxd.demo.android.compose.wechat.HomeViewModel
import com.gxd.demo.android.compose.wechat.theme.WechatTheme

@Composable
fun TopBar(title: String? = null, onBackClick: (() -> Unit)? = null) {
    Row(Modifier.fillMaxWidth().background(WechatTheme.colorScheme.background).statusBarsPadding().height(48.dp)) {
        if (onBackClick == null) {
            Spacer(Modifier.size(36.dp))
        } else {
            Icon(
                painterResource(R.drawable.ic_back), "返回",
                Modifier.size(36.dp).align(Alignment.CenterVertically).clickable(onClick = onBackClick).padding(8.dp),
                tint = WechatTheme.colorScheme.icon
            )
        }
        Spacer(Modifier.weight(1f))
        if (!title.isNullOrEmpty()) {
            Text(
                title,
                Modifier.align(Alignment.CenterVertically),
                fontSize = 15.sp,
                color = WechatTheme.colorScheme.textPrimary,
                fontWeight = FontWeight.W600
            )
            Spacer(Modifier.weight(1f))
        }
        val viewModel: HomeViewModel = viewModel()
        Icon(
            painterResource(R.drawable.ic_palette), "切换主题",
            Modifier
                .size(36.dp)
                .align(Alignment.CenterVertically)
                .clickable {
                    viewModel.theme = when (viewModel.theme) {
                        WechatTheme.Theme.Light -> WechatTheme.Theme.Dark
                        WechatTheme.Theme.Dark -> WechatTheme.Theme.Red
                        WechatTheme.Theme.Red -> WechatTheme.Theme.Light
                    }
                }
                .padding(8.dp),
            tint = WechatTheme.colorScheme.icon
        )
    }
}

@Preview
@Composable
private fun TopBarPreview() {
    TopBar("微信") {}
}