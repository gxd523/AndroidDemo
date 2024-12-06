package com.gxd.demo.compose.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * 饼图
 */
class PieView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val pieOffset = 20.dp
    private val paint by lazy { Paint() }
    private val ovalRectF by lazy { RectF() }
    private lateinit var dataList: List<Pair<Float, Color>>
    private var startAngle = 0f
    private var floatIndex = 0

    fun setData(startAngle: Float = 0f, pieFloatIndex: Int, vararg data: Pair<Float, Color>) {
        this.startAngle = startAngle
        this.floatIndex = if (pieFloatIndex >= data.size) data.size - 1 else pieFloatIndex
        val toList = data.toList()
        val total = toList.map { it.first }.sum()
        dataList = toList.map {
            val angle = it.first * 360 / total
            Pair(angle, it.second)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val radius = min(w, h) / 2
        ovalRectF.set(w / 2f - radius, h / 2f - radius, w / 2f + radius, h / 2f + radius)
    }

    @SuppressLint("NewApi")
    override fun onDraw(canvas: Canvas) {
        if (!::dataList.isInitialized) return

        dataList.forEachIndexed { index, (angle, color) ->
            if (floatIndex == index) {
                canvas.save()
                val radians = Math.toRadians((startAngle + angle / 2).toDouble())
                canvas.translate(pieOffset * cos(radians).toFloat(), pieOffset * sin(radians).toFloat())
            }

            paint.color = color.toArgb()
            canvas.drawArc(ovalRectF, startAngle, angle, true, paint)
            startAngle += angle

            if (floatIndex == index) canvas.restore()
        }
    }
}