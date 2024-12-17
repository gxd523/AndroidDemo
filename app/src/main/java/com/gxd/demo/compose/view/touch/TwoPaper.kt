package com.gxd.demo.compose.view.touch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.*
import android.widget.OverScroller
import androidx.core.view.children
import kotlin.math.abs

/**
 * 更加底层的触摸反馈自定义View，没有使用「GestureDetector」
 * 「velocityTracker」、「viewConfiguration」
 */
class TwoPager(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {
    private val downPointF by lazy { PointF() }
    private val overScroller by lazy { OverScroller(context) }
    private val velocityTracker by lazy { VelocityTracker.obtain() }
    private val viewConfiguration by lazy { ViewConfiguration.get(context) }
    private var minVelocity = viewConfiguration.scaledMinimumFlingVelocity
    private var maxVelocity = viewConfiguration.scaledMaximumFlingVelocity
    private var pagingSlop = viewConfiguration.scaledPagingTouchSlop
    private var downScrollX = 0f
    private var scrolling = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childLeft = 0
        for (child in children) {
            child.layout(childLeft, 0, childLeft + width, height)
            childLeft += width
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) velocityTracker.clear()
        velocityTracker.addMovement(event)
        var result = false
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                scrolling = false
                downPointF.set(event.x, event.y)
                downScrollX = scrollX.toFloat()
            }

            MotionEvent.ACTION_MOVE -> if (!scrolling) {
                val dx = downPointF.x - event.x
                if (abs(dx) > pagingSlop) {
                    scrolling = true
                    parent.requestDisallowInterceptTouchEvent(true)
                    result = true
                }
            }
        }
        return result
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) velocityTracker.clear()
        velocityTracker.addMovement(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downPointF.set(event.x, event.y)
                downScrollX = scrollX.toFloat()
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = (downPointF.x - event.x + downScrollX).toInt().coerceIn(0, width)
                scrollTo(dx, 0)
            }

            MotionEvent.ACTION_UP -> {
                velocityTracker.computeCurrentVelocity(1000, maxVelocity.toFloat()) // 5m/s 5km/h
                val vx = velocityTracker.xVelocity
                val scrollX = scrollX
                val targetPage = if (abs(vx) < minVelocity) {
                    if (scrollX > width / 2) 1 else 0
                } else {
                    if (vx < 0) 1 else 0
                }
                val scrollDistance = if (targetPage == 1) width - scrollX else -scrollX
                overScroller.startScroll(getScrollX(), 0, scrollDistance, 0)
                postInvalidateOnAnimation()
            }
        }
        return true
    }

    override fun computeScroll() {
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.currX, overScroller.currY)
            postInvalidateOnAnimation()
        }
    }
}