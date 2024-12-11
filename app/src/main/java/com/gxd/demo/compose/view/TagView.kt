package com.gxd.demo.compose.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.collection.floatListOf
import java.util.Random

class TagView(context: Context, attrs: AttributeSet? = null) : AppCompatTextView(context, attrs) {
    private val TEXT_SIZE_LIST = floatListOf(12f, 16f, 22f, 28f, 36f)
    private val BG_COLOR_LIST = intArrayOf(
        Color.parseColor("#E91E63"),
        Color.parseColor("#673AB7"),
        Color.parseColor("#3F51B5"),
        Color.parseColor("#2196F3"),
        Color.parseColor("#009688"),
        Color.parseColor("#FF9800"),
        Color.parseColor("#FF5722"),
        Color.parseColor("#795548"),
    )
    private val X_PADDING by lazy { 16.dp }
    private val Y_PADDING by lazy { 8.dp }
    private val CORNER_RADIUS by lazy { 4f.dp }
    private val paint by lazy { Paint() }
    private val random by lazy { Random() }

    init {
        setTextColor(Color.WHITE)
        textSize = TEXT_SIZE_LIST[random.nextInt(TEXT_SIZE_LIST.size)]
        paint.color = BG_COLOR_LIST[random.nextInt(BG_COLOR_LIST.size)]
        setPadding(X_PADDING, Y_PADDING, X_PADDING, Y_PADDING)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), CORNER_RADIUS, CORNER_RADIUS, paint)
        super.onDraw(canvas)
    }
}