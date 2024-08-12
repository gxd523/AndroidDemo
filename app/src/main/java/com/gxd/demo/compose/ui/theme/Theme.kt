package com.gxd.demo.compose.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Composable
fun MyTheme(theme: MyTheme.Theme = MyTheme.Theme.Light, content: @Composable () -> Unit) {
    val colorScheme = when (theme) {
        MyTheme.Theme.Light -> LightColorScheme
        MyTheme.Theme.Dark -> DarkColorScheme
        MyTheme.Theme.NewYear -> NewYearColorScheme
    }
    val animatedColorScheme = CustomColorScheme(
        bottomBar = animateColorAsStateValue(colorScheme.bottomBar),
        background = animateColorAsStateValue(colorScheme.background),
        listItem = animateColorAsStateValue(colorScheme.listItem),
        divider = animateColorAsStateValue(colorScheme.divider),
        chatPage = animateColorAsStateValue(colorScheme.chatPage),
        textPrimary = animateColorAsStateValue(colorScheme.textPrimary),
        textPrimaryMe = animateColorAsStateValue(colorScheme.textPrimaryMe),
        textSecondary = animateColorAsStateValue(colorScheme.textSecondary),
        onBackground = animateColorAsStateValue(colorScheme.onBackground),
        icon = animateColorAsStateValue(colorScheme.icon),
        iconCurrent = animateColorAsStateValue(colorScheme.iconCurrent),
        badge = animateColorAsStateValue(colorScheme.badge),
        onBadge = animateColorAsStateValue(colorScheme.onBadge),
        bubbleMe = animateColorAsStateValue(colorScheme.bubbleMe),
        bubbleOthers = animateColorAsStateValue(colorScheme.bubbleOthers),
        textFieldBackground = animateColorAsStateValue(colorScheme.textFieldBackground),
        more = animateColorAsStateValue(colorScheme.more),
        chatPageBgAlpha = animateFloatAsState(colorScheme.chatPageBgAlpha, TweenSpec(600), label = "").value,
    )

    CompositionLocalProvider(LocalCustomColorScheme provides animatedColorScheme) {
        MaterialTheme(content = content)
    }
}

@Composable
private fun animateColorAsStateValue(color: Color) = animateColorAsState(color, TweenSpec(600), label = "").value

object MyTheme {
    val colorScheme: CustomColorScheme
        @Composable
        get() = LocalCustomColorScheme.current

    enum class Theme {
        Light, Dark, NewYear
    }
}

private val LocalCustomColorScheme = staticCompositionLocalOf { LightColorScheme }

private val LightColorScheme = CustomColorScheme(
    bottomBar = white1,
    background = white2,
    listItem = white,
    divider = white3,
    chatPage = white2,
    textPrimary = black3,
    textPrimaryMe = black3,
    textSecondary = grey1,
    onBackground = grey3,
    icon = black,
    iconCurrent = green3,
    badge = red1,
    onBadge = white,
    bubbleMe = green1,
    bubbleOthers = white,
    textFieldBackground = white,
    more = grey4,
    chatPageBgAlpha = 0f,
)
private val DarkColorScheme = CustomColorScheme(
    bottomBar = black1,
    background = black2,
    listItem = black3,
    divider = black4,
    chatPage = black2,
    textPrimary = white4,
    textPrimaryMe = black6,
    textSecondary = grey1,
    onBackground = grey1,
    icon = white5,
    iconCurrent = green3,
    badge = red1,
    onBadge = white,
    bubbleMe = green2,
    bubbleOthers = black5,
    textFieldBackground = black7,
    more = grey5,
    chatPageBgAlpha = 0f,
)
private val NewYearColorScheme = CustomColorScheme(
    bottomBar = red4,
    background = red5,
    listItem = red2,
    divider = red3,
    chatPage = red5,
    textPrimary = white,
    textPrimaryMe = black6,
    textSecondary = grey2,
    onBackground = grey2,
    icon = white5,
    iconCurrent = green3,
    badge = yellow1,
    onBadge = black3,
    bubbleMe = green2,
    bubbleOthers = red6,
    textFieldBackground = red7,
    more = red8,
    chatPageBgAlpha = 1f,
)

data class CustomColorScheme(
    val bottomBar: Color,
    val background: Color,
    val listItem: Color,
    val divider: Color,
    val chatPage: Color,
    val textPrimary: Color,
    val textPrimaryMe: Color,
    val textSecondary: Color,
    val onBackground: Color,
    val icon: Color,
    val iconCurrent: Color,
    val badge: Color,
    val onBadge: Color,
    val bubbleMe: Color,
    val bubbleOthers: Color,
    val textFieldBackground: Color,
    val more: Color,
    val chatPageBgAlpha: Float
)