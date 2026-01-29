package com.gxd.demo.android.compose.case

import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun LoginFormCase() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        TextField(
            email,
            { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    // 核心：告诉系统这是用户名/邮箱
                    contentType = ContentType.Username
                }
        )
        Spacer(Modifier.height(8.dp))
        TextField(
            password,
            { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    // 核心：告诉系统这是密码
                    contentType = ContentType.Password
                }
        )
    }
}

@Preview(
    name = "Red",
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Preview(
    name = "Green",
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE
)
@Preview(
    name = "Red API 30",
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    apiLevel = 30
)
@Preview(
    name = "Green API 30",
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    apiLevel = 30
)
@Preview(
    name = "Red Dark",
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "Green Dark",
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun MaterialThemeCase() {
    CustomTheme {
        Card {
            Button({}, Modifier.padding(16.dp)) {
                Text("guoxiaodong.com")
            }
        }
    }
}

@Composable
private fun CustomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        // 在「API 12」及以上才有「动态配色」功能
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        shapes = Shapes(medium = CutCornerShape(16.dp)),
        typography = Typography(),
        content = content
    )
}

private val darkScheme = darkColorScheme()
private val lightScheme = lightColorScheme()