package com.gxd.demo.android.view

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import androidx.core.graphics.toColorInt
import com.gxd.demo.android.view.util.dp

@SuppressLint("NewApi")
class MeshDrawable : Drawable() {
    private val interval by lazy { 100f.dp }
    private val paint by lazy { Paint() }

    init {
        paint.color = "#F9A825".toColorInt()
        paint.strokeWidth = 3f.dp
    }

    override fun draw(canvas: Canvas) {
        var x = bounds.left.toFloat()
        while (x <= bounds.right) {
            canvas.drawLine(// 竖线
                x, bounds.top.toFloat(), x, bounds.bottom.toFloat(), paint
            )
            x += interval
        }
        var y = bounds.top.toFloat()
        while (y <= bounds.bottom) {
            canvas.drawLine(// 横线
                bounds.left.toFloat(), y, bounds.right.toFloat(), y, paint
            )
            y += interval
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getAlpha(): Int = paint.alpha

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getColorFilter(): ColorFilter? = paint.colorFilter

    /**
     * 不透明度
     * 暂不考虑挖空的情况
     */
    @Suppress("OVERRIDE_DEPRECATION")
    override fun getOpacity(): Int = when (paint.alpha) {
        0 -> PixelFormat.TRANSPARENT
        0xff -> PixelFormat.OPAQUE
        else -> PixelFormat.TRANSLUCENT
    }
}