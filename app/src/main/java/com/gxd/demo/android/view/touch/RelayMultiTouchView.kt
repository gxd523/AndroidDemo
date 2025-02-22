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
 * 「接力型」多指触控
 */
class RelayMultiTouchView(context: Context, attrs: AttributeSet? = null) : AbsCustomView(context, attrs) {
    private val downPointF by lazy { PointF() }
    private lateinit var bitmap: Bitmap
    private var offsetX = 0f
    private var offsetY = 0f
    private var downOffsetX = 0f
    private var downOffsetY = 0f

    /**
     * 最后一根落下的手指
     */
    private var lastPointerId = 0

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
        val pointerIndex: Int
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                pointerIndex = 0
                lastPointerId = event.getPointerId(pointerIndex)
                downPointF.set(event.getX(pointerIndex), event.getY(pointerIndex))
                downOffsetX = offsetX
                downOffsetY = offsetY
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                pointerIndex = event.actionIndex
                lastPointerId = event.getPointerId(pointerIndex)
                downPointF.set(event.getX(pointerIndex), event.getY(pointerIndex))
                downOffsetX = offsetX
                downOffsetY = offsetY
            }

            MotionEvent.ACTION_POINTER_UP -> {
                pointerIndex = event.actionIndex
                if (event.getPointerId(pointerIndex) == lastPointerId) {
                    // 注意此时「pointerCount」还没更新(-1)
                    val newPointerIndex = if (pointerIndex == event.pointerCount - 1) {
                        event.pointerCount - 2
                    } else {
                        event.pointerCount - 1
                    }
                    lastPointerId = event.getPointerId(newPointerIndex)
                    downPointF.set(event.getX(newPointerIndex), event.getY(newPointerIndex))
                    downOffsetX = offsetX
                    downOffsetY = offsetY
                }
            }

            MotionEvent.ACTION_MOVE -> {
                pointerIndex = event.findPointerIndex(lastPointerId)
                offsetX = downOffsetX + event.getX(pointerIndex) - downPointF.x
                offsetY = downOffsetY + event.getY(pointerIndex) - downPointF.y
                invalidate()
            }
        }

        return true
    }
}