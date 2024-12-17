package com.gxd.demo.compose.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.gxd.demo.compose.R

class RingView(context: Context, attrs: AttributeSet? = null) : AbsCustomView(context, attrs) {
    private val ovalRectF by lazy { RectF() }
    private val strokeWidth by lazy { 15f.dp }
    private val textBoundsRect by lazy { Rect() }
    private val fontMetric by lazy { Paint.FontMetrics() }

    fun init() {

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        ovalRectF.set(
            contentOffsetX + strokeWidth / 2,
            contentOffsetY + strokeWidth / 2,
            contentOffsetX + contentSize - strokeWidth / 2,
            contentOffsetY + contentSize - strokeWidth / 2
        )
    }

    override fun onDraw(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
        paint.color = Color.DKGRAY

        val radius = contentSize / 2f
        canvas.drawCircle(radius + contentOffsetX, radius + contentOffsetY, radius - strokeWidth / 2, paint)

        paint.strokeCap = Paint.Cap.ROUND
        paint.color = Color.RED
        canvas.drawArc(ovalRectF, 0f, 120f, false, paint)

        paint.style = Paint.Style.FILL
        paint.textSize = 70f.dp
        paint.typeface = ResourcesCompat.getFont(context, R.font.oppo_sans)
        paint.textAlign = Paint.Align.CENTER

        val text = "Sport"
        paint.getTextBounds(text, 0, text.length, textBoundsRect)
        paint.getFontMetrics(fontMetric)

        paint.color = Color.BLACK
        canvas.drawText(
            text,
            contentOffsetX + contentSize / 2f,
            contentOffsetY + contentSize / 2f - (fontMetric.ascent + fontMetric.descent) / 2,
            paint
        )
    }
}