package com.gxd.demo.android.view.touch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import com.gxd.demo.android.view.AbsCustomView
import com.gxd.demo.android.view.util.COLOR_LIST
import com.gxd.demo.android.view.util.dp

/**
 * 「各自为战型」多点触控
 */
class AloneMultiTouchView(context: Context, attrs: AttributeSet? = null) : AbsCustomView(context, attrs) {
    private val path by lazy { Path() }
    private val pathMap by lazy { mutableMapOf<Int, Path>() }
    private val pathPool by lazy { ArrayDeque<Path>(5) }

    init {
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 4f.dp
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        pathMap.values.forEachIndexed { index, path ->
            paint.color = COLOR_LIST.getOrNull(index) ?: Color.BLACK
            canvas.drawPath(path, paint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val path = pathPool.removeLastOrNull() ?: Path()
                path.reset()
                val pointerIndex = event.actionIndex
                path.moveTo(event.getX(pointerIndex), event.getY(event.actionIndex))
                invalidate()
                pathMap[event.getPointerId(pointerIndex)] = path
            }

            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until event.pointerCount) {
                    pathMap[event.getPointerId(i)]?.lineTo(event.getX(i), event.getY(i))
                }
                invalidate()
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                pathMap.remove(event.getPointerId(event.actionIndex))?.let { removedPath ->
                    pathPool.addFirst(removedPath)
                }
                invalidate()
            }
        }
        return true
    }
}