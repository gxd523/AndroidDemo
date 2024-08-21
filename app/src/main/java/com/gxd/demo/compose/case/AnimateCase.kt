package com.gxd.demo.compose.case

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.AnimationResult
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Preview
@Composable
fun Rebound() {
    var toggle by remember { mutableStateOf<Boolean?>(null) }
    val animatableX = remember { Animatable(0.dp, Dp.VectorConverter, label = "labelX") }
    val animatableY = remember { Animatable(0.dp, Dp.VectorConverter, label = "labelY") }

    BoxWithConstraints {
        val offsetX = remember(animatableX.value) {
            val maxOffsetWidth = maxWidth - 100.dp
            var cacOffsetX = animatableX.value
            while (cacOffsetX > maxOffsetWidth * 2) cacOffsetX -= maxOffsetWidth * 2
            if (cacOffsetX < maxOffsetWidth) cacOffsetX else maxOffsetWidth * 2 - cacOffsetX
        }
        val offsetY = remember(animatableY.value) {
            val maxOffsetHeight = maxHeight - 100.dp
            var cacOffsetY = animatableY.value
            while (cacOffsetY > maxOffsetHeight * 2) cacOffsetY -= maxOffsetHeight * 2
            if (cacOffsetY < maxOffsetHeight) cacOffsetY else maxOffsetHeight * 2 - cacOffsetY
        }
//        animatableX.updateBounds(0.dp, maxWidth - 100.dp)
//        animatableY.updateBounds(0.dp, maxHeight - 100.dp)

        Box(Modifier.fillMaxSize()) {
            Box(Modifier.size(100.dp).offset(offsetX, offsetY).background(Color.Green).clickable { toggle = !(toggle ?: false) })
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
            } while (result?.endReason == AnimationEndReason.BoundReached)
        }
        launch {
            var result: AnimationResult<Dp, AnimationVector1D>? = null
            do {
                val initialVelocity = result?.endState?.velocity?.times(-1) ?: 4000.dp
                result = animatableY.animateDecay(initialVelocity, decay)
            } while (result?.endReason == AnimationEndReason.BoundReached)
        }
    }
}