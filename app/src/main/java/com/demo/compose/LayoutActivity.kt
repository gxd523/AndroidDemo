package com.demo.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.roundToInt

class LayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LayoutCase(Modifier.padding(8.dp)) {
                Text("aaa")
                Text("bbb")
                Image(painterResource(android.R.mipmap.sym_def_app_icon), null)
            }
        }
    }

    @Preview
    @Composable
    fun SimpleCase() {
        Text("haha",
            Modifier
                .background(Color.Green)
                .layout { measurable, constraints ->
                    val padding = 8.dp
                        .toPx()
                        .roundToInt()
                    val paddingConstraints = constraints
                        .copy()
                        .apply {
                            constrainWidth(maxWidth - padding * 2)
                            constrainHeight(maxHeight - padding * 2)
                        }
                    val placeable = measurable.measure(paddingConstraints)// 测量内部
                    layout(placeable.width + padding * 2, placeable.height + padding * 2) {
                        placeable.placeRelative(padding, padding)// 摆放内部控件
                    }
                }
                .background(Color.Yellow)
        )
    }

    /**
     * 实现简单的Column()
     */
    @Composable
    fun LayoutCase(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
        Layout(content, modifier) { measurableList, constraints ->
            val placeableList = mutableListOf<Placeable>()
            var width = 0
            var height = 0
            measurableList.forEach { measurable ->
                // 每个measurable只能测量一次, 原生控件可以多次测量, 但当界面层级加深时, 会对性能产生很大影响
                val placeable = measurable.measure(constraints)
                width = max(width, placeable.width)
                height += placeable.height
                placeableList += placeable
            }
            layout(width, height) {
                var currentHeight = 0
                placeableList.forEach { placeable ->
                    placeable.placeRelative(0, currentHeight)
                    currentHeight += placeable.height
                }
            }
        }
    }

    /**
     * 固有特性测量：内部内容的尺寸
     * Compose虽然不能重复测量，但是可以在测量之前先进行固有特性测量，然后再做正式测量
     * 固有特性测量不被算入测量次数
     * 改变固有尺寸需要充血[androidx.compose.ui.layout.MeasurePolicy]
     */
    @Preview
    @Composable
    fun IntrinsicMeasurementCase() {
        // 用每个子控件最小固有尺寸的最大值作为父控件的高度，2个Text有固有高度，Divider没有固有高度
        Row(Modifier.height(IntrinsicSize.Min)) {
            Text("aaaaa")// 文字的最大/最小固有尺寸是一样的
            Divider(// 分割线没有内部内容，所以最大/最小固有尺寸都是0
                Modifier
                    .width(1.dp)
                    .fillMaxHeight(),
                Color.Black
            )
            Text("bbbbb", fontSize = 30.sp)
        }
    }
}