package com.gxd.demo.compose.wechat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gxd.demo.compose.R
import com.gxd.demo.compose.ui.theme.MyTheme

@Composable
fun DiscoveryPage() {
    Column(Modifier.fillMaxSize()) {
        TopBar("发现")
        Box(Modifier.background(MyTheme.colorScheme.background).fillMaxSize()) {
            Column(Modifier.background(MyTheme.colorScheme.listItem).fillMaxWidth()) {
                MeItem(R.drawable.ic_moments, "朋友圈", badge = {
                    Box(
                        Modifier
                            .padding(8.dp)
                            .clip(RoundedCornerShape(50))
                            .size(18.dp)
                            .background(MyTheme.colorScheme.badge)
                    ) {
                        Text(
                            "3",
                            Modifier.align(Alignment.Center),
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                            color = MyTheme.colorScheme.onBadge
                        )
                    }
                }, endBadge = {
                    Image(
                        painterResource(R.drawable.avatar_gaolaoshi), "avatar",
                        Modifier
                            .padding(8.dp, 0.dp)
                            .size(32.dp)
                            .unread(false, MyTheme.colorScheme.badge)
                            .clip(RoundedCornerShape(4.dp))
                    )
                })
                MeItem(
                    R.drawable.ic_channels,
                    "视频号",
                    endBadge = {
                        Image(
                            painterResource(R.drawable.avatar_diuwuxian), "avatar", Modifier
                                .padding(8.dp, 0.dp)
                                .size(32.dp)
                                .unread(false, MyTheme.colorScheme.badge)
                                .clip(RoundedCornerShape(4.dp))
                        )
                        Text(
                            "赞过", Modifier.padding(0.dp, 0.dp, 4.dp, 0.dp),
                            fontSize = 14.sp, color = MyTheme.colorScheme.textSecondary
                        )
                    },
                    modifier = Modifier
                        .background(MyTheme.colorScheme.background)
                        .padding(top = 8.dp)
                        .background(MyTheme.colorScheme.listItem)
                )
                MeItem(
                    R.drawable.ic_ilook, "看一看",
                    Modifier.background(MyTheme.colorScheme.background).padding(top = 8.dp).background(MyTheme.colorScheme.listItem)
                )
                HorizontalDivider(Modifier.padding(start = 56.dp), color = MyTheme.colorScheme.divider, thickness = 0.8f.dp)
                MeItem(R.drawable.ic_isearch, "搜一搜")
                MeItem(
                    R.drawable.ic_nearby, "直播和附近",
                    Modifier.background(MyTheme.colorScheme.background).padding(top = 8.dp).background(MyTheme.colorScheme.listItem)
                )
            }
        }
    }
}

@Preview
@Composable
private fun DiscoveryPagePreview() {
    DiscoveryPage()
}