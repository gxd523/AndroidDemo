package com.gxd.demo.compose.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import kotlin.math.roundToInt

class DrawableView(context: Context, attrs: AttributeSet? = null) : AbsCustomView(context, attrs) {
    private val drawable by lazy { MeshDrawable() }

    override fun onDraw(canvas: Canvas) {
        val leftOffsetInt = contentOffsetX.roundToInt()
        val topOffsetInt = contentOffsetY.roundToInt()
        drawable.setBounds(leftOffsetInt, topOffsetInt, contentSize + leftOffsetInt, contentSize + topOffsetInt)
        drawable.draw(canvas)
    }
}