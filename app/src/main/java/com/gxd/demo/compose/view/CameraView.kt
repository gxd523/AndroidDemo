package com.gxd.demo.compose.view

import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import coil3.Bitmap
import com.gxd.demo.compose.R
import kotlin.math.min

/**
 * 「几何变换」+「Camera」实现照片翻转
 */
class CameraView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint by lazy { Paint() }
    private val camera by lazy {
        Camera().apply {
            rotateX(80f)
            setLocation(0f, 0f, -5 * resources.displayMetrics.density)
        }
    }
    private val matrix = Matrix()
    private lateinit var bitmap: Bitmap
    private var leftOffset = 0f
    private var topOffset = 0f

    fun init() {
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val contentSize = min(w, h)
        leftOffset = (w - contentSize) / 2f
        topOffset = (h - contentSize) / 2f
        bitmap = getBitmap(resources, R.drawable.avatar_png, contentSize)
    }

    override fun onDraw(canvas: Canvas) {
        val halfSize = bitmap.width / 2

        canvas.save()
        canvas.translate(leftOffset + halfSize, topOffset + halfSize)
        canvas.rotate(-45f)
        val clipRadius = halfSize * 2
        canvas.clipRect(-clipRadius, -clipRadius, clipRadius, 0)
        canvas.rotate(45f)
        canvas.translate(-(leftOffset + halfSize), -(topOffset + halfSize))
        canvas.drawBitmap(bitmap, leftOffset, topOffset, paint)
        canvas.restore()

        canvas.save()
        canvas.translate(leftOffset + halfSize, topOffset + halfSize)
        canvas.rotate(-45f)
        camera.applyToCanvas(canvas)
        canvas.clipRect(-clipRadius, 0, clipRadius, clipRadius)
        canvas.rotate(45f)
        canvas.translate(-(leftOffset + halfSize), -(topOffset + halfSize))
        canvas.drawBitmap(bitmap, leftOffset, topOffset, paint)
        canvas.restore()

        matrix.reset()
        matrix.setScale(0.5f, 0.5f, 0f, 0f)
        matrix.postRotate(180f, halfSize / 2f, halfSize / 2f)
        matrix.postTranslate(leftOffset + halfSize - halfSize / 2, topOffset + halfSize - halfSize / 2)
//        canvas.drawBitmap(bitmap, matrix, paint)
    }
}