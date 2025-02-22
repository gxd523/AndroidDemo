package com.gxd.demo.android.constraint

import android.app.Activity
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.gxd.demo.android.R

class ConstraintActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.layout_constraint_set_start)
    }

    fun onClick(v: View) {
        val constraintLayout = v as ConstraintLayout
        val constraintSet = ConstraintSet().apply {
            isForceId = false// 防止布局中有无ID的控件时报错
            clone(this@ConstraintActivity, R.layout.layout_constraint_set_end)// 获取布局中的约束集
        }
        TransitionManager.beginDelayedTransition(constraintLayout)
        constraintSet.applyTo(constraintLayout)
    }
}