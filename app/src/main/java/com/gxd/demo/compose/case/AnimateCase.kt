package com.gxd.demo.compose.case

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.AnimationResult
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.gxd.demo.compose.util.screenHeightPercent
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.random.Random

/**
 * 随机生成一个大于「min」小于「max」且相比之前的值变化超过30%的新值
 */
private fun Dp.coerceInNewRandomDp(min: Dp = 30.dp, max: Dp = 200.dp): Dp {
    val random = Random.nextInt(max.value.toInt() / 30) * 30f
    val newValue = random.coerceIn(min.value, max.value)
    return if (abs(newValue - this.value) / this.value < 0.3f) this.coerceInNewRandomDp(min, max) else Dp(newValue)
}

/**
 * animateAsState动画示例
 */
@Preview(showBackground = true)
@Composable
fun AnimateAsStateCase() {
    var size by remember { mutableStateOf(50.dp) }
    val animationSpec = remember { tween<Dp>(500, 500) }
    val sizeAnimate by animateDpAsState(size, animationSpec, "size") // 不用「remember」包裹
    Box(Modifier.screenHeightPercent(50), Alignment.Center) {
        Box(
            Modifier
                .size(sizeAnimate)
                .background(Color.Red)
                .clickable { size = size.coerceInNewRandomDp() }
        )
    }
}

/**
 * Animatable动画示例
 */
@Preview(showBackground = true)
@Composable
fun AnimatableCase(initialValue: Dp = 50.dp) {
    val sizeAnimatable = remember { Animatable(initialValue, Dp.VectorConverter) }
    val scope = rememberCoroutineScope()

    Box(Modifier.screenHeightPercent(50), Alignment.Center) {
        Box(Modifier.size(sizeAnimatable.value).background(Color.Red).clickable {
            scope.launch {
                val targetSize = sizeAnimatable.value.coerceInNewRandomDp()
                sizeAnimatable.animateTo(targetSize)
            }
        })
    }
}

/**
 * AnimationSpec示例
 * 自定义Easing：Easing {...}
 * 三姐贝塞尔曲线：CubicBezierEasing(.75f, 0f, .09f, 1f)
 */
@Preview(showBackground = true)
@Composable
fun AnimationSpecCase(boxSize: Dp = 50.dp, easing: Easing = CubicBezierEasing(.75f, 0f, .09f, 1f)) {
    var offsetX by remember { mutableStateOf(0.dp) }
    val animationSpec = tween<Dp>(1_000, easing = easing)
    val offsetAnimate by animateDpAsState(offsetX, animationSpec, "animationSpec")
    val configuration = LocalConfiguration.current
    Box(Modifier.screenHeightPercent()) {
        Box(
            Modifier
                .size(boxSize)
                .offset(offsetAnimate, offsetAnimate)
                .background(Color.Red)
                .clickable { offsetX = Dp((configuration.screenWidthDp - boxSize.value).toFloat()) }
        )
    }
}

/**
 * 关键帧动画示例
 */
@Preview(showBackground = true)
@Composable
fun KeyframesSpecCase(boxSize: Dp = 50.dp, initialValue: Dp = 0.dp, durationMillis: Int = 500) {
    val configuration = LocalConfiguration.current
    val targetValue = Dp((configuration.screenWidthDp - boxSize.value).toFloat())

    val animationSpec = keyframes {
        this.durationMillis = durationMillis
        initialValue at 0 using LinearEasing// 注意这里
        targetValue / 5 at durationMillis / 10 using FastOutSlowInEasing
        targetValue / 2 at durationMillis / 10 * 9 using LinearOutSlowInEasing
        targetValue at durationMillis
    }
    val animatable = remember { Animatable(initialValue, Dp.VectorConverter) }

    val scope = rememberCoroutineScope()

    Box(Modifier.screenHeightPercent()) {
        Box(
            Modifier
                .size(boxSize)
                .offset(animatable.value, animatable.value)
                .background(Color.Red)
                .clickable { scope.launch { animatable.animateTo(targetValue, animationSpec) } }
        )
    }
}

/**
 * 反弹动画示例
 */
@Preview(showBackground = true)
@Composable
fun ReboundCase() {
    var toggle by remember { mutableStateOf<Boolean?>(null) }
    val animatableX = remember {
        Animatable(0.dp, Dp.VectorConverter, label = "labelX")
    }
    val animatableY = remember {
        Animatable(0.dp, Dp.VectorConverter, label = "labelY")
    }

    BoxWithConstraints {
        val offsetX = remember(animatableX.value) {
            val maxOffsetWidth = this.maxWidth - 100.dp
            var cacOffsetX = animatableX.value
            while (cacOffsetX > maxOffsetWidth * 2) cacOffsetX -= maxOffsetWidth * 2
            if (cacOffsetX < maxOffsetWidth) cacOffsetX else maxOffsetWidth * 2 - cacOffsetX
        }
        val offsetY = remember(animatableY.value) {
            val maxOffsetHeight = this.maxHeight - 100.dp
            var cacOffsetY = animatableY.value
            while (cacOffsetY > maxOffsetHeight * 2) cacOffsetY -= maxOffsetHeight * 2
            if (cacOffsetY < maxOffsetHeight) cacOffsetY else maxOffsetHeight * 2 - cacOffsetY
        }
//        animatableX.updateBounds(0.dp, maxWidth - 100.dp)
//        animatableY.updateBounds(0.dp, maxHeight - 100.dp)

        Box(Modifier.screenHeightPercent()) {
            Box(
                Modifier
                    .size(100.dp)
                    .offset(offsetX, offsetY)
                    .background(Color.Green)
                    .clickable { toggle = toggle != true }
            )
        }
    }

    if (toggle == null) return

    val decay = remember { exponentialDecay<Dp>(1f, 1.2f) }
    LaunchedEffect(toggle) {
        launch {
            var result: AnimationResult<Dp, AnimationVector1D>? = null
            do {
                val initialVelocity = result?.endState?.velocity?.times(-1) ?: 3000.dp
                result = animatableX.animateDecay(initialVelocity, decay)
            } while (result.endReason == AnimationEndReason.BoundReached)
        }
        launch {
            var result: AnimationResult<Dp, AnimationVector1D>? = null
            do {
                val initialVelocity = result?.endState?.velocity?.times(-1) ?: 4000.dp
                result = animatableY.animateDecay(initialVelocity, decay)
            } while (result.endReason == AnimationEndReason.BoundReached)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateTransitionCase() {
    var targetState by remember { mutableStateOf(MyState.End) }
    val transitionState = remember { MutableTransitionState(MyState.Init) }
    transitionState.targetState = targetState
    val transition = rememberTransition(transitionState, label = "transitionA")
    transition.AnimatedVisibility({ state -> state == MyState.End }) {
        Box(Modifier.size(50.dp).background(Color.Red))
    }
    val offsetX by transition.animateDp(label = "offsetX", transitionSpec = {
        when {
            MyState.Init isTransitioningTo MyState.Middle -> tween(1_000)
            MyState.Init isTransitioningTo MyState.End -> tween(2_000)
            MyState.Middle isTransitioningTo MyState.End -> spring()
            else -> tween(0)
        }
    }) { state ->
        when (state) {
            MyState.Init -> 0.dp
            MyState.Middle -> 100.dp
            MyState.End -> 200.dp
        }
    }
    val bgColor by transition.animateColor(label = "bgColor", transitionSpec = {
        when {
            MyState.Init isTransitioningTo MyState.Middle -> tween(1_000)
            MyState.Init isTransitioningTo MyState.End -> tween(2_000)
            MyState.Middle isTransitioningTo MyState.End -> spring()
            else -> tween(0)
        }
    }) { state ->
        when (state) {
            MyState.Init -> Color.Red
            MyState.Middle -> Color.Green
            MyState.End -> Color.Blue
        }
    }
    Box(Modifier.screenHeightPercent()) {
        Box(
            Modifier
                .size(100.dp)
                .offset(offsetX)
                .background(bgColor)
                .clickable { targetState = targetState.next() }
        )
    }
}

enum class MyState {
    Init, Middle, End;

    fun next(): MyState = when (this) {
        Init -> Middle
        Middle -> End
        End -> Init
    }
}

@Preview(showBackground = true)
@Composable
fun AnimateVisibilityCase() {
    Row {
        var toggle by remember { mutableStateOf(true) }
        Button(modifier = Modifier.align(Alignment.CenterVertically), onClick = { toggle = !toggle }) { Text("切换") }
        val transformOrigin = TransformOrigin(1f, 1f)
        val enter = scaleIn(tween(2_000), transformOrigin = transformOrigin)
        val targetSize: (IntSize) -> IntSize = { IntSize(it.width / 2, it.height / 2) }
        val animationSpec = tween<IntSize>(2_000)
        val exit = shrinkOut(animationSpec, Alignment.Center, false, targetSize)
        AnimatedVisibility(toggle, enter = enter, exit = exit) {
            Box(Modifier.size(100.dp).background(Color.Red))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CrossfadeCase() {
    Column {
        var targetState by remember { mutableStateOf(MyState.Init) }
        Crossfade(targetState, label = "crossfade") { state ->
            when (state) {
                MyState.Init -> Box(Modifier.size(100.dp).background(Color.Red))
                MyState.Middle -> Box(Modifier.size(80.dp).background(Color.Green))
                MyState.End -> Box(Modifier.size(120.dp).background(Color.Blue))
            }
        }
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { targetState = targetState.next() }
        ) { Text("切换") }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimatedContentCase() {
    Column {
        var targetState by remember { mutableStateOf(true) }
        AnimatedContent(targetState, transitionSpec = {
            when (targetState) {
                true -> {
                    val fadeInTweenSpec = tween<Float>(1_000)
                    val fadeOutTweenSpec = tween<Float>(2_000)
                    fadeIn(fadeInTweenSpec) togetherWith fadeOut(fadeOutTweenSpec)
                }

                false -> {
                    val fadeInTweenSpec = tween<Float>(3_000)
                    val fadeOutTweenSpec = tween<Float>(4_000)
                    fadeIn(fadeInTweenSpec) togetherWith fadeOut(fadeOutTweenSpec)
                }
            }
        }, label = "animatedContent") { state ->
            when (state) {
                true -> Box(Modifier.size(200.dp).background(Color.Red))
                false -> Box(Modifier.size(100.dp).background(Color.Green))
            }
        }
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { targetState = !targetState }
        ) { Text("切换") }
    }
}