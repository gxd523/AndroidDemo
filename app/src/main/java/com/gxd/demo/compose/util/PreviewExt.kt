package com.gxd.demo.compose.util

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.halfScreenHeight(percent: Int = 50): Modifier {
    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp * percent / 100
    return this.height(height.dp)
}

@Composable
fun Modifier.screenSizePercent(widthPercent: Int = 50, heightPercent: Int = 50): Modifier {
    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp * widthPercent / 100
    val height = configuration.screenHeightDp * heightPercent / 100
    return size(width.dp, height.dp)
}