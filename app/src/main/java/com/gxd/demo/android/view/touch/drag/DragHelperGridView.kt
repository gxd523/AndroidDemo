package com.gxd.demo.android.view.touch.drag

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.customview.widget.ViewDragHelper

/**
 * 「ViewDragHelper」
 * 待整理！！！
 */
class DragHelperGridView(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    companion object {
        private const val COLUMNS = 2
        private const val ROWS = 3
    }

    private val dragHelper by lazy { ViewDragHelper.create(this, DragCallback()) }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specWidth = MeasureSpec.getSize(widthMeasureSpec)
        val specHeight = MeasureSpec.getSize(heightMeasureSpec)
        val childWidth = specWidth / COLUMNS
        val childHeight = specHeight / ROWS
        measureChildren(
            MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY)
        )
        setMeasuredDimension(specWidth, specHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childLeft: Int
        var childTop: Int
        val childWidth = width / COLUMNS
        val childHeight = height / ROWS
        for ((index, child) in children.withIndex()) {
            childLeft = index % 2 * childWidth
            childTop = index / 2 * childHeight
            child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean = dragHelper.shouldInterceptTouchEvent(ev)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) postInvalidateOnAnimation()
    }

    private inner class DragCallback : ViewDragHelper.Callback() {
        var capturedLeft = 0f
        var capturedTop = 0f

        override fun tryCaptureView(child: View, pointerId: Int): Boolean = true

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int = left

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int = top

        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            capturedChild.elevation = elevation + 1
            capturedLeft = capturedChild.left.toFloat()
            capturedTop = capturedChild.top.toFloat()
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            dragHelper.settleCapturedViewAt(capturedLeft.toInt(), capturedTop.toInt())
            postInvalidateOnAnimation()
        }
    }
}