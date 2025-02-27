package com.gxd.demo.android.compose.case

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
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
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.gxd.demo.android.util.screenHeightPercent
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.random.Random

/**
 * animateAsState动画示例
 */
@Preview(showBackground = true)
@Composable
fun AnimateAsStateCase() {
    var size by remember { mutableStateOf(50.dp) }
    val animationSpec = remember { tween<Dp>(500, 500) }
    val sizeAnimate by animateDpAsState(size, animationSpec, "size") // 不用「remember」包裹
    Box(Modifier.screenHeightPercent(), Alignment.Center) {
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

    Box(Modifier.screenHeightPercent(), Alignment.Center) {
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
                .clickable { offsetX = Dp((configuration.screenWidthDp - boxSize.value)) }
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
    val targetValue = Dp((configuration.screenWidthDp - boxSize.value))

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
 * 弹簧动画示例
 */
@Preview(showBackground = true)
@Composable
fun SpringSpecCase(initialValue: Dp = 0.dp) {
    val offsetYAnimatable = remember {
        Animatable(initialValue, Dp.VectorConverter)
    }
    val scope = rememberCoroutineScope()
    val animationSpec = remember {
        spring<Dp>(Spring.DampingRatioHighBouncy, Spring.StiffnessHigh)
    }
    val boxSize = 50.dp
    val configuration = LocalConfiguration.current
    val targetValue = remember(configuration.screenWidthDp) {
        Dp((configuration.screenWidthDp - boxSize.value))
    }

    Box(Modifier.screenHeightPercent(), Alignment.TopCenter) {
        Box(
            Modifier
                .size(boxSize)
                .offset(0.dp, offsetYAnimatable.value)
                .background(Color.Red)
                .clickable {
                    scope.launch { offsetYAnimatable.animateTo(targetValue, animationSpec) }
                }
        )
    }
}

/**
 * 「RepeatableSpec」+「TweenSpec」示例
 */
@Preview(showBackground = true)
@Composable
fun RepeatableSpecCase(initialValue: Dp = 0.dp) {
    val offsetAnimatable = remember { Animatable(initialValue, Dp.VectorConverter) }
    val scope = rememberCoroutineScope()
    val animationSpec = remember {
        repeatable<Dp>(9, tween(50, 1_000), RepeatMode.Reverse)
    }
    val boxSize = 50.dp
    val configuration = LocalConfiguration.current
    val targetValue = remember(configuration.screenWidthDp) {
        Dp((configuration.screenWidthDp - boxSize.value).toFloat())
    }

    Box(Modifier.screenHeightPercent(50)) {
        Box(
            Modifier
                .size(boxSize)
                .offset(offsetAnimatable.value, offsetAnimatable.value)
                .background(Color.Red)
                .clickable {
                    scope.launch { offsetAnimatable.animateTo(targetValue, animationSpec) }
                })
    }
}

/**
 * 「衰减」+「撞边」动画示例
 */
@Preview(showBackground = true)
@Composable
fun AnimateDecayCase() {
    val decayXAnim = remember {
        Animatable(0.dp, Dp.VectorConverter)
    }
    val decayYAnimatable = remember {
        Animatable(0.dp, Dp.VectorConverter)
    }
    val animationSpec = remember { exponentialDecay<Dp>() }
    val scope = rememberCoroutineScope()

    BoxWithConstraints(Modifier.screenHeightPercent(50)) {
        val boxSize = 50.dp
        decayXAnim.updateBounds(0.dp, this.maxWidth - boxSize) // TODO: 这里只想执行一次该怎么做
        decayYAnimatable.updateBounds(0.dp, this.maxHeight - boxSize)
        Box(
            Modifier
                .size(boxSize)
                .offset(decayXAnim.value, decayYAnimatable.value)
                .background(Color.Red)
                .clickable {
                    scope.launch { decayXAnim.animateDecay(1999.dp, animationSpec) }
                    scope.launch { decayYAnimatable.animateDecay(999.dp, animationSpec) }
                })
    }
}

/**
 * 反弹动画示例
 */
@Preview(showBackground = true)
@Composable
fun ReboundCase() {
    var toggle by remember { mutableStateOf<Boolean?>(null) }
    val offsetXAnim = remember {
        Animatable(0.dp, Dp.VectorConverter)
    }
    val offsetYAnim = remember {
        Animatable(0.dp, Dp.VectorConverter)
    }

    BoxWithConstraints(Modifier.screenHeightPercent()) {
        val boxSize = 100.dp
        // 相比「updateBounds」更精准的做法
        val offsetX = remember(offsetXAnim.value) {
            val maxOffsetWidth = this.maxWidth - boxSize
            var cacOffsetX = offsetXAnim.value
            while (cacOffsetX > maxOffsetWidth * 2) cacOffsetX -= maxOffsetWidth * 2
            if (cacOffsetX < maxOffsetWidth) cacOffsetX else maxOffsetWidth * 2 - cacOffsetX
        }
        val offsetY = remember(offsetYAnim.value) {
            val maxOffsetHeight = this.maxHeight - boxSize
            var cacOffsetY = offsetYAnim.value
            while (cacOffsetY > maxOffsetHeight * 2) cacOffsetY -= maxOffsetHeight * 2
            if (cacOffsetY < maxOffsetHeight) cacOffsetY else maxOffsetHeight * 2 - cacOffsetY
        }
        // 使用「updateBounds」有误差，因为每一帧有「16ms」的延迟
//        animatableX.updateBounds(0.dp, maxWidth - 100.dp)
//        animatableY.updateBounds(0.dp, maxHeight - 100.dp)

        Box(
            Modifier
                .size(boxSize)
                .offset(offsetX, offsetY)
                .background(Color.Green)
                .clickable { toggle = toggle != true }
        )
    }

    if (toggle == null) return// 第一次组合时不要触发动画，重组时触发

    val decaySpec = remember { exponentialDecay<Dp>(1f, 1f) }

    LaunchedEffect(toggle) {
        var result: AnimationResult<Dp, AnimationVector1D>? = null
        do {
            val initialVelocity = result?.endState?.velocity?.times(-1) ?: 4500.dp
            result = offsetXAnim.animateDecay(initialVelocity, decaySpec)
        } while (result?.endReason == AnimationEndReason.BoundReached)
    }
    LaunchedEffect(toggle) {
        var result: AnimationResult<Dp, AnimationVector1D>? = null
        do {
            val initialVelocity = result?.endState?.velocity?.times(-1) ?: 3500.dp
            result = offsetYAnim.animateDecay(initialVelocity, decaySpec)
        } while (result?.endReason == AnimationEndReason.BoundReached)
    }
}

@Preview(showBackground = true)
@Composable
fun TransitionCase() {
    val transitionState = remember { MutableTransitionState(MyState.Start) }
    val transition = rememberTransition(transitionState, "transitionA")

    transition.AnimatedVisibility({ state -> state == MyState.End }) {
        Box(Modifier.size(150.dp).background(Color.Black))
    }

    val offsetTransAnim by transition.animateDp({
        when {
            MyState.Start isTransitioningTo MyState.Middle -> tween(1_000)
            MyState.Start isTransitioningTo MyState.End -> tween(2_000)
            MyState.Middle isTransitioningTo MyState.End -> spring()
            else -> tween(0)
        }
    }, "offsetX") { state ->
        when (state) {
            MyState.Start -> 0.dp
            MyState.Middle -> 150.dp
            MyState.End -> 300.dp
        }
    }

    val colorTransAnim by transition.animateColor({
        when {
            MyState.Start isTransitioningTo MyState.Middle -> tween(1_000)
            MyState.Start isTransitioningTo MyState.End -> tween(2_000)
            MyState.Middle isTransitioningTo MyState.End -> spring()
            else -> tween(0)
        }
    }, "bgColor") { state ->
        when (state) {
            MyState.Start -> Color.Red
            MyState.Middle -> Color.Green
            MyState.End -> Color.Blue
        }
    }

    Box(Modifier.screenHeightPercent()) {
        Box(
            Modifier
                .size(100.dp)
                .offset(offsetTransAnim, offsetTransAnim)
                .background(colorTransAnim)
                .clickable { transitionState.targetState = transitionState.targetState.next() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AnimatedVisibilityCase() {
    Box(Modifier.screenHeightPercent(), Alignment.Center) {
        var toggle by remember { mutableStateOf(true) }
        val (enter, exit) = createEnterExitTransition()

        AnimatedVisibility(toggle, enter = enter, exit = exit) {
            Box(Modifier.size(300.dp).background(Color.Red))
        }

        Button({ toggle = !toggle }) { Text("切换") }
    }
}

@Preview(showBackground = true)
@Composable
fun CrossfadeCase() {
    Box(Modifier.screenHeightPercent(), Alignment.Center) {
        var targetState by remember { mutableStateOf(MyState.Start) }
        Crossfade(targetState, label = "crossfade", animationSpec = tween(1_000)) { state ->
            when (state) {
                MyState.Start -> Box(Modifier.size(300.dp).background(Color.Red))
                MyState.Middle -> Box(Modifier.size(150.dp).background(Color.Green))
                MyState.End -> Box(Modifier.size(250.dp).background(Color.Blue))
            }
        }
        Button({ targetState = targetState.next() }) { Text("切换") }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimatedContentCase() {
    Box(Modifier.screenHeightPercent(), Alignment.Center) {
        var targetState by remember { mutableStateOf(true) }
        AnimatedContent(targetState, transitionSpec = {
            when (targetState) {
                true -> createEnterExitTransition(1_000).run { first togetherWith second } using SizeTransform(false)
                false -> createEnterExitTransition(2_000).run { first togetherWith second } using SizeTransform(false)
            }
        }, label = "animatedContent") { state ->
            when (state) {
                true -> Box(Modifier.size(300.dp).background(Color.Red))
                false -> Box(Modifier.size(150.dp).background(Color.Green))
            }
        }
        Button({ targetState = !targetState }) { Text("切换") }
    }
}

private enum class MyState {
    Start, Middle, End;

    fun next(): MyState = when (this) {
        Start -> Middle
        Middle -> End
        End -> Start
    }
}

/**
 * 随机生成一个大于「min」小于「max」且相比之前的值变化超过30%的新值
 */
fun Dp.coerceInNewRandomDp(min: Dp = 30.dp, max: Dp = 200.dp): Dp {
    val random = Random.nextInt(max.value.toInt() / 30) * 30f
    val newValue = random.coerceIn(min.value, max.value)
    return if (abs(newValue - this.value) / this.value < 0.3f) this.coerceInNewRandomDp(min, max) else Dp(newValue)
}

private fun createEnterExitTransition(duration: Int = 1_000): Pair<EnterTransition, ExitTransition> {
    val floatTweenSpec = tween<Float>(duration)
    val intOffsetTweenSpec = tween<IntOffset>(duration)

    val enter = slideInHorizontally(intOffsetTweenSpec) { -it } +
            scaleIn(floatTweenSpec) +
            fadeIn(floatTweenSpec)
    val exit = slideOutHorizontally(intOffsetTweenSpec) { it } +
            scaleOut(floatTweenSpec) +
            fadeOut(floatTweenSpec)
    return Pair(enter, exit)
}