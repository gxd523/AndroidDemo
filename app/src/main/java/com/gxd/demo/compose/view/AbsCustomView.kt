package com.gxd.demo.compose.view

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

abstract class AbsCustomView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    protected val paint by lazy { Paint() }
    protected var leftOffset = 0f
    protected var topOffset = 0f
    protected var contentSize = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        contentSize = min(w, h)
        leftOffset = (w - contentSize) / 2f
        topOffset = (h - contentSize) / 2f
    }
}