package com.gxd.demo.android.constraint

import android.content.Context
import android.util.AttributeSet
import android.view.ViewAnimationUtils
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.hypot

class MyConstraintHelper(context: Context, attrs: AttributeSet) : ConstraintHelper(context, attrs) {
    override fun updatePostLayout(container: ConstraintLayout) {
        referencedIds.forEach { viewId ->
            val view = container.getViewById(viewId)
            val radius = hypot(view.width.toDouble(), view.height.toDouble()).toInt()
            val animator = ViewAnimationUtils.createCircularReveal(view, 0, 0, 0f, radius.toFloat())
            animator.startDelay = 1_000
            animator.duration = 2_000
            animator.start()
        }
    }
}