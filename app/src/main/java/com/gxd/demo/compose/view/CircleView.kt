package com.gxd.demo.compose.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import com.gxd.demo.compose.view.util.dp

/**
 * 对「自定义View」通过「自定义尺寸计算」完整重写「onMeasure」
 */
class CircleView(context: Context, attrs: AttributeSet? = null) : AbsCustomView(context, attrs) {
    private val radius by lazy { 50f.dp }
    private val padding by lazy { 10f.dp }

    init {
        paint.color = Color.parseColor("#2AACBB")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = ((padding + radius) * 2).toInt()
        val calculateWidth = resolveSize(size, widthMeasureSpec)
        val calculateHeight = resolveSize(size, heightMeasureSpec)
        setMeasuredDimension(calculateWidth, calculateHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawCircle(padding + radius, padding + radius, radius, paint)
    }

    /**
     * 等价于「resolveSize」
     */
    private fun calculateSize(size: Int, measureSpec: Int): Int {
        val measureSpecSize = MeasureSpec.getSize(measureSpec)
        return when (MeasureSpec.getMode(measureSpec)) {
            MeasureSpec.EXACTLY -> measureSpecSize
            MeasureSpec.AT_MOST -> if (size > measureSpecSize) measureSpecSize else size
            else -> size
        }
    }
}