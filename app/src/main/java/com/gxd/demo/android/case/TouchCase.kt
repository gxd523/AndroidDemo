package com.gxd.demo.android.case

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAll
import com.gxd.demo.android.util.screenSizePercent
import kotlin.math.roundToInt

/**
 * 横向拖动文字示例
 */
@Preview
@Composable
fun DraggableCase() {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val state = rememberDraggableState { delta -> offsetX += delta }
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        Modifier
            .screenSizePercent(100, 15)
            .background(Color.LightGray)
            .draggable(state, Orientation.Horizontal, true, interactionSource, false)
            .offset { IntOffset(offsetX.roundToInt(), 0) },
        Alignment.Center
    ) {
        val isDragged by interactionSource.collectIsDraggedAsState()
        Text(if (isDragged) "拖动中" else "静止")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun ScrollableCase() {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val state = rememberScrollableState { delta ->
        offsetX += delta
        return@rememberScrollableState delta
    }
    val orientation = Orientation.Horizontal
    val overscrollEffect = ScrollableDefaults.overscrollEffect()
    val flingBehavior = ScrollableDefaults.flingBehavior()
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        Modifier
            .screenSizePercent(100, 15)
            .background(Color.LightGray)
            .scrollable(state, orientation, overscrollEffect, true, false, flingBehavior, interactionSource)
            .offset { IntOffset(offsetX.roundToInt(), 0) },
        Alignment.Center
    ) {
        val isDragged by interactionSource.collectIsDraggedAsState()
        Text(if (isDragged) "滑动中" else "静止")
    }
}

@Preview
@Composable
fun DraggableCase1() {
    BoxWithConstraints(
        Modifier.screenSizePercent(100, 15).background(Color.LightGray),
        Alignment.Center
    ) {
        var offsetX by remember { mutableFloatStateOf(0f) }
        var childWidth by remember { mutableFloatStateOf(0f) }
        val state = rememberDraggableState { delta ->
            offsetX = (offsetX + delta).coerceIn(0f, this.maxWidth.value - childWidth)
        }
        val interactionSource = remember { MutableInteractionSource() }
        Box(
            Modifier
                .draggable(state, Orientation.Horizontal, true, interactionSource, false)
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .background(Color.Blue)
                .onGloballyPositioned { layoutCoordinates ->
                    childWidth = layoutCoordinates.size.width.toFloat()// 动态获取子组件宽度
                },
            Alignment.Center
        ) {
            val isDragged by interactionSource.collectIsDraggedAsState()
            Text(if (isDragged) "拖动中" else "静止", color = Color.White)
        }
    }
}

/**
 * 纵向嵌套滑动
 */
@Preview(showBackground = true)
@Composable
fun NestedScrollCase() {
    var offsetY by remember { mutableFloatStateOf(0f) }
    val scrollDispatcher = remember { NestedScrollDispatcher() }
    val draggableState = rememberDraggableState { delta ->
        val scrollSource = NestedScrollSource.UserInput
        val consumedOffset = scrollDispatcher.dispatchPreScroll(Offset(0f, delta), scrollSource)// 1
        offsetY += delta - consumedOffset.y// 2
        scrollDispatcher.dispatchPostScroll(Offset(0f, delta), Offset.Zero, scrollSource)// 3
    }
    val scrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                return super.onPreScroll(available, source)
            }

            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                offsetY += available.y
                return available
            }
        }
    }

    Column(
        Modifier
            .screenSizePercent(30, 20)
            .offset { IntOffset(0, offsetY.roundToInt()) }
            .draggable(draggableState, Orientation.Vertical)
            .nestedScroll(scrollConnection, scrollDispatcher)
    ) {
        LazyColumn(Modifier.fillMaxWidth().height(100.dp).background(Color.LightGray)) {
            items(10) { index ->
                Text("内部-$index")
            }
        }
        repeat(10) { index -> Text("外部-$index") }
    }
}

/**
 * 二维滑动示例
 */
@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun TwoDimensionScrollCase(max: Dp = 200.dp, min: Dp = 0.dp, sliderSize: Dp = 50.dp) {
    val density = LocalDensity.current
    val (minPx, maxPx) = remember(density) { with(density) { min.toPx() to max.toPx() } }
    val sliderSizePx = remember { with(density) { sliderSize.toPx() } }

    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    val draggable2DState = rememberDraggable2DState { delta ->
        val newValueX = offsetX + delta.x
        val newValueY = offsetY + delta.y
        // 偏移量限制在「max」、「min」之间
        offsetX = newValueX.coerceIn(minPx, maxPx - sliderSizePx)
        offsetY = newValueY.coerceIn(minPx, maxPx - sliderSizePx)
    }
    Box(Modifier.size(max).background(Color.LightGray).draggable2D(draggable2DState)) {
        val offset = IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
        Box(Modifier.offset { offset }.size(sliderSize).background(Color.Red))
    }
}

/**
 * 多指手势示例
 */
@Preview
@Composable
fun MultiFingerGestureCase() {

}

/**
 * 自定义触摸示例
 */
@Preview
@Composable
fun CustomTouchCase() {
    Box(
        Modifier
            .screenSizePercent(50, 20)
            .background(Color.LightGray)
            .simpleCustomClick { Log.d("ggg", "点击了一次") }
    )
}

/**
 * 自定义点击，处理了「按下后划出组件」、「多根手指同时或依次抬起」的情况
 */
@Composable
private fun Modifier.simpleCustomClick(onclick: () -> Unit) = pointerInput(Unit) {
    awaitEachGesture {// TODO 「awaitEachGesture」是上面「awaitPointerEventScope」+「while」的封装
        while (true) {// TODO 这里实际有双「while」，妙～
            val pointerEvent = awaitPointerEvent()
            when (pointerEvent.type) {
                PointerEventType.Move -> {
                    val position = pointerEvent.changes.firstOrNull()?.position ?: continue
                    if (position.x < 0 || position.x > size.width || position.y < 0 || position.y > size.height) break
                }

                PointerEventType.Release -> {
                    // 「多根手指同时抬起」、「剩最后一根手指抬起」时在是真正的抬起
                    val isRealRelease = pointerEvent.changes.fastAll {// 遍历所有手指
                        it.changedToUp()// 是否都是从「Press」变为「Release」
                    }
                    if (isRealRelease) onclick()
                }
            }
        }
    }
}