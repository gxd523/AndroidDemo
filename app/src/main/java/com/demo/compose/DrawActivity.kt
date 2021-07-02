package com.demo.compose

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class DrawActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SimpleCase()
        }
    }

    @Preview
    @Composable
    fun SimpleCase() {
        Column {
            Text("drawWithContent", Modifier.drawWithContent {
                drawRect(Color.Green)
                drawContent()
            }, fontSize = 30.sp)
            Text("drawBehind", Modifier.drawBehind {
                drawRect(Color.Green)
            }, fontSize = 30.sp)
            Text("drawWithCache", Modifier.drawWithCache {// 对准备过程进行缓存
                // 准备工作,这里的变量会缓存
                val path = Path()
                onDrawWithContent {// 绘制内容本来就是带缓存的
                    // 绘制背景
                    drawContent()
                    // 绘制前景
                    drawPath(path, Brush.horizontalGradient())
                }
            }, fontSize = 30.sp)
        }
    }

    /**
     * 相当于传统方式中继承View的自定义View
     */
    @Preview
    @Composable
    fun CanvasCase() {
        Box(contentAlignment = Alignment.Center) {
            Image(painterResource(R.mipmap.sym_def_app_icon), null)
            Canvas(modifier = Modifier.size(40.dp)) {
                drawRect(Color(0x33FF00FF))
            }
        }
    }
}