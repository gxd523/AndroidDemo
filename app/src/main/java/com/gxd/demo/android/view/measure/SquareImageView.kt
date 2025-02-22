package com.gxd.demo.android.view.measure

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.min

/**
 * 继承已有「View」通过「onMeasure」简单修改尺寸
 */
class SquareImageView(context: Context, attrs: AttributeSet? = null) : AppCompatImageView(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }
}