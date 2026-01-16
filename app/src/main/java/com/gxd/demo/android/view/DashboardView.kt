package com.gxd.demo.android.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathDashPathEffect
import android.graphics.PathEffect
import android.graphics.PathMeasure
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import com.gxd.demo.android.view.util.dpp
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * 仪表板
 * 重点：PathEffect、PathMeasure
 */
class DashboardView(context: Context, attrs: AttributeSet? = null) : AbsCustomView(context, attrs) {
    companion object {
        private const val DEFAULT_ANGLE = 120
        private const val DEFAULT_COUNT = 20
        private const val DEFAULT_INDEX = 8
    }

    private val arcPath by lazy { Path() }
    private val lineStopPointF by lazy { PointF() }

    private val dashWidth by lazy { 2f.dpp }
    private val dashHeight by lazy { 5f.dpp }
    private val dashPath by lazy {
        Path().apply { addRect(0f, 0f, dashWidth, dashHeight, Path.Direction.CW) }
    }
    private lateinit var dashPathEffect: PathEffect
    private var angle: Int = DEFAULT_ANGLE
    private var count: Int = DEFAULT_COUNT
    private var index: Int = DEFAULT_INDEX

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = dashWidth
    }

    fun init(angle: Int = DEFAULT_ANGLE, count: Int = DEFAULT_COUNT, index: Int = DEFAULT_INDEX) {
        this.angle = angle
        this.count = count
        this.index = index
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val radius = min(w, h) / 2
        val ovalRectF = RectF(w / 2f - radius, h / 2f - radius, w / 2f + radius, h / 2f + radius)
        arcPath.reset()
        arcPath.addArc(ovalRectF, (90 - angle / 2f) + angle, 360f - angle)

        val pathMeasure = PathMeasure(arcPath, false)
        val advance = (pathMeasure.length - dashWidth) / count
        dashPathEffect = PathDashPathEffect(dashPath, advance, 0f, PathDashPathEffect.Style.ROTATE)

        val lineLength = radius - 20f.dpp
        val radians = Math.toRadians((90 + angle / 2 + (360 - angle) * index / count).toDouble())
        lineStopPointF.set(
            width / 2f + lineLength * cos(radians).toFloat(),
            height / 2f + lineLength * sin(radians).toFloat()
        )
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(width / 2f, height / 2f, lineStopPointF.x, lineStopPointF.y, paint)

        canvas.drawPath(arcPath, paint)

        paint.pathEffect = dashPathEffect
        canvas.drawPath(arcPath, paint)
    }
}