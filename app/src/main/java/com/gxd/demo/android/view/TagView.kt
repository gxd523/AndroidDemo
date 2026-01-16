package com.gxd.demo.android.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.collection.floatListOf
import com.gxd.demo.android.view.util.COLOR_LIST
import com.gxd.demo.android.view.util.dpp
import java.util.Random

class TagView(context: Context, attrs: AttributeSet? = null) : AppCompatTextView(context, attrs) {
    companion object {
        private val TEXT_SIZE_LIST = floatListOf(12f, 16f, 22f, 28f, 36f)
        private val X_PADDING by lazy { 16.dpp }
        private val Y_PADDING by lazy { 8.dpp }
        private val CORNER_RADIUS by lazy { 4f.dpp }
    }

    private val paint by lazy { Paint() }
    private val random by lazy { Random() }

    init {
        setTextColor(Color.WHITE)
        textSize = TEXT_SIZE_LIST[random.nextInt(TEXT_SIZE_LIST.size)]
        paint.color = COLOR_LIST[random.nextInt(COLOR_LIST.size)]
        setPadding(X_PADDING, Y_PADDING, X_PADDING, Y_PADDING)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), CORNER_RADIUS, CORNER_RADIUS, paint)
        super.onDraw(canvas)
    }
}