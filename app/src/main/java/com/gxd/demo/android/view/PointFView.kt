package com.gxd.demo.android.view

import android.animation.TypeEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import com.gxd.demo.android.view.util.dp

/**
 * TypeEvaluator
 * ```
 * PointFView(it).also { view ->
 *     view.init()
 *
 *     ObjectAnimator.ofObject(
 *         view,
 *         "point",
 *         PointFView.PointFEvaluator(),
 *         PointF(200f, 400f)
 *     ).also {
 *         it.duration = 1_000
 *         it.startDelay = 1_000
 *         it.repeatMode = ObjectAnimator.REVERSE
 *         it.repeatCount = ObjectAnimator.INFINITE
 *     }.start()
 * ```
 * }
 */
class PointFView(context: Context, attrs: AttributeSet? = null) : AbsCustomView(context, attrs) {
    var point = PointF(0f, 0f)
        set(value) {
            field = value
            invalidate()
        }

    init {
        paint.strokeWidth = 20f.dp
        paint.strokeCap = Paint.Cap.ROUND
    }

    fun init() {
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPoint(point.x, point.y, paint)
    }

    class PointFEvaluator : TypeEvaluator<PointF> {
        override fun evaluate(fraction: Float, startValue: PointF, endValue: PointF): PointF {
            val startX = startValue.x
            val endX = endValue.x
            val currentX = startX + (endX - startX) * fraction
            val startY = startValue.y
            val endY = endValue.y
            val currentY = startY + (endY - startY) * fraction
            return PointF(currentX, currentY)// 线性变化
        }
    }
}