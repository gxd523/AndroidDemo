package com.gxd.demo.android.view.touch.drag

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

/**
 * 「View.OnDragListener」、「startDragAndDrop」
 * 待整理！！！
 */
class DragListenerGridView(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    companion object {
        private const val COLUMNS = 2
        private const val ROWS = 3
    }

    private val dragListener by lazy { MyOnDragListener() }
    private val orderedChildren by lazy { mutableListOf<View>() }
    private var draggedView: View? = null

    init {
        isChildrenDrawingOrderEnabled = true
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        for (child in children) {
            orderedChildren.add(child) // 初始化位置
            child.setOnLongClickListener { v ->
                draggedView = v
                v.startDragAndDrop(null, DragShadowBuilder(v), v, 0)
                false
            }
            child.setOnDragListener(dragListener)
        }
    }

    override fun onDragEvent(event: DragEvent?): Boolean = super.onDragEvent(event)

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
            child.layout(0, 0, childWidth, childHeight)
            child.translationX = childLeft.toFloat()
            child.translationY = childTop.toFloat()
        }
    }

    private inner class MyOnDragListener : OnDragListener {
        override fun onDrag(v: View, event: DragEvent): Boolean {
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> if (event.localState === v) v.visibility = INVISIBLE
                DragEvent.ACTION_DRAG_ENTERED -> if (event.localState !== v) sort(v)
                DragEvent.ACTION_DRAG_EXITED -> {}
                DragEvent.ACTION_DRAG_ENDED -> if (event.localState === v) v.visibility = VISIBLE
            }
            return true
        }

        private fun sort(targetView: View) {
            var draggedIndex = -1
            var targetIndex = -1
            for ((index, child) in orderedChildren.withIndex()) {
                if (targetView === child) {
                    targetIndex = index
                } else if (draggedView === child) {
                    draggedIndex = index
                }
            }
            orderedChildren.removeAt(draggedIndex)
            orderedChildren.add(targetIndex, draggedView!!)
            var childLeft: Int
            var childTop: Int
            val childWidth = width / COLUMNS
            val childHeight = height / ROWS
            for ((index, child) in orderedChildren.withIndex()) {
                childLeft = index % 2 * childWidth
                childTop = index / 2 * childHeight
                child.animate()
                    .translationX(childLeft.toFloat())
                    .translationY(childTop.toFloat())
                    .setDuration(150)
            }
        }
    }
}