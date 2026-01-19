package com.gxd.demo.android.compose.case

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessAlarm
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.gxd.demo.android.R

@Preview(name = "Night Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(group = "Font Scale", name = "250% font", fontScale = 2.5f)
@Preview(group = "Font Scale", name = "80% font", fontScale = 0.8f)
@Preview(name = "English", locale = "en")
@Preview(group = "background", backgroundColor = 0xFFFF00FF, showBackground = true, name = "Default")
@Preview(group = "background", name = "size", showBackground = true, widthDp = 130, heightDp = 40)
@Preview(group = "background", name = "wallpaper", wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
@DevicesPreview
@Preview(apiLevel = 30, name = "API Level")
@Composable
fun PreviewCase1(modifier: Modifier = Modifier, buttonColor: Color = Color.DarkGray) = Button(
    {}, colors = ButtonDefaults.buttonColors(buttonColor)
) {
    val textStyle = remember {
        TextStyle(
            platformStyle = PlatformTextStyle(includeFontPadding = false),
            lineHeightStyle = LineHeightStyle(LineHeightStyle.Alignment.Center, LineHeightStyle.Trim.Both)
        )
    }
    Icon(Icons.Outlined.AccessAlarm, "")
    Text(
        stringResource(R.string.schedule),
        Modifier.padding(horizontal = 5.dp)/*.offset(y = (-2.5).dp)*/,
        style = textStyle
    )
    Image(
        painter = painterResource(R.drawable.avatar_rengwuxian),
        "",
        Modifier.size(20.dp).clip(CircleShape).blur(2.dp)/*blur仅支持API31及以上*/,
        contentScale = ContentScale.Crop
    )
}

@Preview(
    group = "Device",
    showSystemUi = true,
    device = "spec:width=180dp,height=180dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait,cutout=punch_hole,navigation=gesture",
    name = "一般用在界面上"
)
@Preview(group = "Device", showSystemUi = false, device = Devices.TABLET, name = "一般用在界面上")
annotation class DevicesPreview