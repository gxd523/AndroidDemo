package com.gxd.demo.android.view.touch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import com.gxd.demo.android.R
import com.gxd.demo.android.view.AbsCustomView
import com.gxd.demo.android.view.util.getBitmap

/**
 * 「合作型」多指触控
 */
class CooperateMultiTouchView(context: Context, attrs: AttributeSet? = null) : AbsCustomView(context, attrs) {
    private val downPointF by lazy { PointF() }
    private lateinit var bitmap: Bitmap
    private var offsetX = 0f
    private var offsetY = 0f
    private var downOffsetX = 0f
    private var downOffsetY = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        bitmap = getBitmap(resources, R.drawable.avatar_png, contentSize)
        contentOffsetX = (w - bitmap.width) / 2f
        contentOffsetY = (h - bitmap.height) / 2f

        offsetX = contentOffsetX
        offsetY = contentOffsetY
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var sumX = 0f
        var sumY = 0f
        val isPointerUp = event.actionMasked == MotionEvent.ACTION_POINTER_UP
        for (index in 0 until event.pointerCount) {
            if (isPointerUp && event.actionIndex == index) continue// 多跟手指抬起一根手指「pointerCount」并没有-1
            sumX += event.getX(index)
            sumY += event.getY(index)
        }
        val pointerCount = if (isPointerUp) event.pointerCount - 1 else event.pointerCount
        val focusX = sumX / pointerCount
        val focusY = sumY / pointerCount

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> {
                downPointF.set(focusX, focusY)
                downOffsetX = offsetX
                downOffsetY = offsetY
            }

            MotionEvent.ACTION_MOVE -> {
                offsetX = downOffsetX + focusX - downPointF.x
                offsetY = downOffsetY + focusY - downPointF.y
                invalidate()
            }
        }

        return true
    }
}