package com.gxd.demo.compose.case

import android.graphics.Camera
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.gxd.demo.compose.R
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
fun CustomDrawCase() {
    val imageBitmap = ImageBitmap.imageResource(R.drawable.avatar_png) // 内部有包「remember」
    val paint by remember { mutableStateOf(Paint()) }
    val camera by remember { mutableStateOf(Camera()) }
    val rotationAnimatable = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        val animationSpec = infiniteRepeatable<Float>(tween(2_000))
        rotationAnimatable.animateTo(360f, animationSpec)
    }

    Canvas(Modifier.background(Color.LightGray).padding(50.dp).size(100.dp)) {
        drawIntoCanvas { canvas ->
            val width = size.width
            val height = size.height
            val translateX = width / 2
            val translateY = height / 2
            val rotateDegrees = 45f

            canvas.translate(translateX, translateY)
            canvas.rotate(-rotateDegrees)

            camera.save()
            camera.rotateX(rotationAnimatable.value)
            camera.applyToCanvas(canvas.nativeCanvas)
            camera.restore()

            canvas.rotate(rotateDegrees)
            canvas.translate(-translateX, -translateY)

            val dstSize = IntSize(width.roundToInt(), height.roundToInt())
            canvas.drawImageRect(imageBitmap, dstSize = dstSize, paint = paint)
        }
    }
}
