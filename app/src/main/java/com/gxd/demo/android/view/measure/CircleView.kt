package com.gxd.demo.android.view.measure

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import com.gxd.demo.android.view.AbsCustomView
import com.gxd.demo.android.view.util.dp

/**
 * 对「自定义View」通过「自定义尺寸计算」完整重写「onMeasure」
 * 1、重写「onMeasure」
 * 2、用「resolveSize」修成结果，调用「子View」的「measure」
 * 3、调用「setMeasuredDimension」保存尺寸
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