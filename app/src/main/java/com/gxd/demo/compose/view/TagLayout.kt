package com.gxd.demo.compose.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.math.max

/**
 * 最复杂的「自定义Layout」，需要重写「onMeasure」、「onLayout」
 */
class TagLayout(context: Context, attrs: AttributeSet? = null) : ViewGroup(context, attrs) {
    private val childrenBoundList = mutableListOf<Rect>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec)
        // 「TagLayout」已使用的宽度
        var totalWidthUsed = 0
        // 「TagLayout」已使用的高度
        var totalHeightUsed = 0
        // 当前行已使用的宽度
        var lineWidthUsed = 0
        // 当前行最高的「子View」的高度
        var lineMaxHeight = 0

        for ((index, child) in children.withIndex()) {
            measureChildWithMargins(// 「measureChildWithMargins」计算「childMeasureSpec」执行「child.measure」
                child,
                widthMeasureSpec,
                0,// 去掉「lineWidthUsed」，按照全宽「widthMeasureSize」测量
                heightMeasureSpec,
                totalHeightUsed
            )

            if (widthMeasureMode != MeasureSpec.UNSPECIFIED && lineWidthUsed + child.measuredWidth > widthMeasureSize) {
                totalHeightUsed += lineMaxHeight
                lineWidthUsed = 0
                lineMaxHeight = 0
                measureChildWithMargins(
                    child,
                    widthMeasureSpec,
                    0,
                    heightMeasureSpec,
                    totalHeightUsed
                )
            }

            if (index >= childrenBoundList.size) childrenBoundList.add(Rect())
            val childBound = childrenBoundList[index]

            childBound.set(
                lineWidthUsed,
                totalHeightUsed,
                lineWidthUsed + child.measuredWidth,
                totalHeightUsed + child.measuredHeight
            )
            lineWidthUsed += child.measuredWidth
            totalWidthUsed = max(totalWidthUsed, lineWidthUsed)
            lineMaxHeight = max(lineMaxHeight, child.measuredHeight)
        }
        setMeasuredDimension(totalWidthUsed, totalHeightUsed + lineMaxHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for ((index, child) in children.withIndex()) {
            val childBound = childrenBoundList[index]
            child.layout(childBound.left, childBound.top, childBound.right, childBound.bottom)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams? = MarginLayoutParams(context, attrs)

    /**
     * 如果当前「ViewGroup」不能滑动，则不应该让内部的「子View」等待一段时间在设置为按下
     * todo 小细节
     */
    override fun shouldDelayChildPressedState(): Boolean = false
}