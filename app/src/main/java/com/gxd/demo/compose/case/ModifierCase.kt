package com.gxd.demo.compose.case

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max

/**
 * Modifier顺序敏感性
 */
@Preview
@Composable
private fun ModifierOrderSensitiveCase() {
    Box(Modifier.background(Color.Blue).size(100.dp)) {
        Text(
            "按钮",
            Modifier
                .background(Color.Green)
                .padding(10.dp)
                .background(Color.Red)
                .clickable {}
                .padding(20.dp)
                .background(Color.White)
                .align(Alignment.Center)
        )
    }
}

/**
 * 自定义Modifier示例
 */
@Preview(showBackground = true)
@Composable
fun CustomModifierCase() {
    Row {
        Box(Modifier.size(100.dp).background(Color.Red))
        Box(Modifier.size(100.dp).weight(1f).background(Color.Blue))
    }
}

/**
 * 自定义Modifier，如果内部有「状态」，则需要套上「composed」
 */
fun Modifier.clickScale(contentColor: Color = Color.Red, padding: Dp = 8.dp): Modifier = composed {
    var paddingInner by remember { mutableStateOf(padding) }
    Modifier.padding(paddingInner).background(contentColor).clickable {
        paddingInner = if (paddingInner == padding) 0.dp else padding
    }
}

/**
 * 「Modifier」的「layout」的添加「padding」的示例
 */
@Preview(showBackground = true)
@Composable
fun LayoutCase(padding: Dp = 20.dp) {
    Box(Modifier.background(Color.Blue)) {
        Text(
            "AAAAAA\nBBBBBB",
            Modifier.layout { measurable, constraints ->
                val paddingPx = padding.roundToPx()
                val doublePaddingPx = paddingPx * 2
                val newConstraints = constraints.copy(
                    maxWidth = constraints.maxWidth - doublePaddingPx,
                    maxHeight = constraints.maxHeight - doublePaddingPx
                )
                val placeable = measurable.measure(newConstraints)
                layout(placeable.width + doublePaddingPx, placeable.height + doublePaddingPx) {
                    placeable.placeRelative(paddingPx, paddingPx)
                }
            }.background(Color.White)
        )
    }
}

@Preview
@Composable
private fun DrawModifierCase() {
    Box(Modifier.size(200.dp).background(Color.Red).drawWithContent {
        drawCircle(Color.Green)
        drawContent()
        drawCircle(color = Color.Yellow, radius = size.minDimension / 3f)
    }.padding(20.dp).background(Color.Magenta)) {
        Text("123", color = Color.White, fontSize = 50.sp)
    }
}

@Preview(showBackground = true)
@Composable
private fun PaddingCase() {
    Box {
        Box(
            Modifier
                .background(Color.Red)
                .requiredSize(80.dp)
                .background(Color.Yellow)
                .requiredSize(40.dp)
        )
    }
}

@Preview
@Composable
fun PointerInputCase() {
    val context = LocalContext.current
    Box(
        Modifier
            .background(Color.Blue)
            .size(200.dp)
            .background(Color.Red)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    Toast.makeText(context, "单击", Toast.LENGTH_SHORT).show()
                }, onLongPress = {
                    Toast.makeText(context, "长按", Toast.LENGTH_SHORT).show()
                }, onDoubleTap = {
                    Toast.makeText(context, "双击", Toast.LENGTH_SHORT).show()
                })
            }
            .padding(50.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun ParentDataModifierCase() {
    CustomParentDataLayout(Modifier.background(Color.LightGray)) {
        Text("123", Modifier.width(50.dp).wrapContentHeight().background(Color.Blue).setCustomParentData("aaa"))
        Text("abc", Modifier.width(100.dp).wrapContentHeight().background(Color.Red).setCustomParentData("bbb"))
    }
}

@Composable
fun CustomParentDataLayout(modifier: Modifier = Modifier, content: @Composable CustomParentDataScope.() -> Unit) {
    Layout({ CustomParentDataScope.content() }, modifier) { measurableList, constraints ->
        var width = 0
        var height = 0

        val placeableList = measurableList.map { measurable ->
            val customParentData = measurable.parentData as? String

            measurable.measure(constraints).also { placeable ->
                width = max(width, placeable.width)
                height += placeable.height
            }
        }

        layout(width, height) {
            var totalHeight = 0
            placeableList.forEach { placeable ->
                placeable.placeRelative(0, totalHeight)
                totalHeight += placeable.height
            }
        }
    }
}

/**
 * 「LayoutScopeMarker」让使用者只能在「CustomParentDataScope」直接内部使用「setCustomParentData」，内部的内部都不能用
 */
@LayoutScopeMarker
object CustomParentDataScope {
    fun Modifier.setCustomParentData(text: String): Modifier = this then object : ParentDataModifier {
        override fun Density.modifyParentData(parentData: Any?): Any? = parentData as? String ?: text
    }
}

@Preview
@Composable
fun OnRemeasuredModifierCase() {
    val context = LocalContext.current
    var size by remember { mutableStateOf(100.dp) }
    var onSizeChanged by remember { mutableStateOf(IntSize(0, 0)) }
    Box(
        Modifier
            .size(size)
            .background(Color.Red)
            .clickable {
                size = size.coerceInNewRandomDp(100.dp, 300.dp)
            }.onSizeChanged {
                onSizeChanged = it
            }
    ) {
        Text("$onSizeChanged")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun ModifierLocalCase() {
    val modifierLocal = modifierLocalOf { "aaa" }
    Modifier.modifierLocalProvider(modifierLocal) {
        "abc"
    }.modifierLocalConsumer {
        modifierLocal.current
    }
}

/**
 * 使用「foldIn」遍历「Modifier链」的示例
 */
fun main() {
    val modifier = Modifier.size(100.dp).background(Color.Red).padding(10.dp).clickable { }
    val result = modifier.foldIn<Modifier>(Modifier) { acc, element ->
        acc.then(element).also { returnValue ->
            println("acc = ${acc.name()}, element = ${element.name()}, return = ${returnValue.name()}")
        }
    }
    println("result = ${result.name()}")
}

fun Modifier.name() = this.javaClass.simpleName