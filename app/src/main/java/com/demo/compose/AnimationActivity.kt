package com.demo.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class AnimationActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
//            SimpleCase()
            AnimatedVisibilityCase()
        }
    }

    @Preview
    @Composable
    private fun SimpleCase() {
        var big by remember { mutableStateOf(false) }
        val size by animateDpAsState(// size不能修改
            if (big) 200.dp else 80.dp,
            tween(1000)// 动画时长
        )
        Box(
            Modifier
                .size(size)
                .background(Color.Green)
                .clickable { big = !big }
        ) {
        }
    }

    /**
     * 多个控件、属性同时动画
     */
    @Preview
    @Composable
    private fun TransitionCase() {
        var big by remember { mutableStateOf(false) }
        val bigTransition = updateTransition(big, label = "big")// 记录一个百分比的状态值
        // size、cornerSize使用同一个计算过程
        val size by bigTransition.animateDp(label = "size") { if (it) 200.dp else 80.dp }
        val cornerSize by bigTransition.animateDp(label = "cornerSize") { if (it) 12.dp else 0.dp }
        Box(
            Modifier
                .size(size)
                .clip(RoundedCornerShape(cornerSize))
                .background(Color.Green)
                .clickable { big = !big }
        ) {
        }
    }

    @Preview
    @Composable
    private fun AnimatableCase() {
        val anim = remember { Animatable(80.dp, Dp.VectorConverter) }
        var big by remember { mutableStateOf(false) }
        LaunchedEffect(big) {// key1改变时协程才会再次执行
            anim.snapTo(if (big) 300.dp else 0.dp)// 让变量直接变成目标值，没有动画过程
            anim.animateTo(// 有动画过程
                if (big) 150.dp else 80.dp,
                spring(Spring.DampingRatioMediumBouncy)// 弹簧效果
            )
        }
        Box(
            Modifier
                .size(anim.value)
                .background(Color.Green)
                .clickable { big = !big }
        ) {
        }
    }

    @Preview
    @Composable
    fun AnimateContentSize() {
        var big by remember { mutableStateOf(false) }
        Column(Modifier.animateContentSize()) {
            Box(
                Modifier
                    .size(80.dp)
                    .background(Color.Yellow)
                    .clickable { big = !big }
            ) {
            }
            if (big) {
                Box(
                    Modifier
                        .size(80.dp)
                        .background(Color.Blue)
                        .clickable { big = !big }
                ) {
                }
            }
        }
    }

    @Preview
    @Composable
    fun CrossfadeCase() {
        var big by remember { mutableStateOf(false) }
        Crossfade(big) {
            if (it) {
                Box(
                    Modifier
                        .size(80.dp)
                        .background(Color.Yellow)
                        .clickable { big = !big }
                )
            } else {
                Box(
                    Modifier
                        .size(80.dp)
                        .background(Color.Blue)
                        .clickable { big = !big }
                )
            }
        }
    }

    @ExperimentalAnimationApi
    @Preview
    @Composable
    fun AnimatedVisibilityCase() {
        var big by remember { mutableStateOf(false) }
        AnimatedVisibility(big) {
            Box(
                Modifier
                    .size(80.dp)
                    .background(Color.Yellow)
                    .clickable { big = !big }
            )
        }
    }
}