package com.gxd.demo.android.view.touch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.OverScroller
import androidx.core.view.children
import kotlin.math.abs

/**
 * 更加底层的触摸反馈自定义View，没有使用「GestureDetector」
 * 「velocityTracker」、「viewConfiguration」
 * 只能有两个「子View」所以没做页面动态加载
 */
class TwoPager(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {
    private val downPointF by lazy { PointF() }
    private val overScroller by lazy { OverScroller(context) }

    /**
     * 惯性滑动时根据当前位置获取初始速度
     */
    private val velocityTracker by lazy { VelocityTracker.obtain() }
    private val viewConfiguration by lazy { ViewConfiguration.get(context) }

    /**
     * fling的最小速度
     */
    private val minVelocity by lazy { viewConfiguration.scaledMinimumFlingVelocity }

    /**
     * 限制用户fling的最大速度，防止用户无意间快速一划，导致页面很快的飞过去
     */
    private val maxVelocity by lazy { viewConfiguration.scaledMaximumFlingVelocity.toFloat() }

    /**
     * 分页滑动的阈值
     */
    private val pagingSlop by lazy { viewConfiguration.scaledPagingTouchSlop }
    private var downScrollX = 0f

    /**
     * 是否达到滑动阈值，进入滑动状态
     */
    private var scrolling = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 给所有的「子View」一个统一的宽和高，这里直接填入了「父View」的宽高
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

    /**
     * 触摸点的「父View」的上层如果有「子View」需要拦截
     */
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) velocityTracker.clear()
        velocityTracker.addMovement(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                scrolling = false
                downPointF.set(event.x, event.y)
                downScrollX = scrollX.toFloat()
            }

            MotionEvent.ACTION_MOVE -> if (!scrolling && abs(downPointF.x - event.x) > pagingSlop) {
                scrolling = true
                parent.requestDisallowInterceptTouchEvent(true)
                return true
            }
        }
        return false
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
                val dx = (-(event.x - downPointF.x) + downScrollX).toInt().coerceIn(0, width)
                scrollTo(dx, 0)
            }

            MotionEvent.ACTION_UP -> {
                velocityTracker.computeCurrentVelocity(1000, maxVelocity) // 计算此时的惯性速度，「1000」表示1秒移动的像素数，即「像素/秒」
                val xVelocity = velocityTracker.xVelocity
                val targetPage = if (abs(xVelocity) < minVelocity) {// 松手速度很小时，靠近哪边就向哪边吸附
                    if (scrollX > width / 2) 1 else 0
                } else {// 松手速度大于「minVelocity」时，「xVelocity<0」表示向左快划，所以停在第1页，反之停在第0页
                    if (xVelocity < 0) 1 else 0
                }
                val scrollDistance = if (targetPage == 1) width - scrollX else -scrollX
                overScroller.startScroll(scrollX, 0, scrollDistance, 0)
                postInvalidateOnAnimation()// 当前控件在下一帧标记为失效，配合「computeScroll」
            }
        }
        return true
    }

    /**
     * 会在「draw」中被调用
     */
    override fun computeScroll() {
        if (!overScroller.computeScrollOffset()) return
        scrollTo(overScroller.currX, overScroller.currY)
        postInvalidateOnAnimation()
    }
}