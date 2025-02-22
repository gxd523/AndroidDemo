package com.gxd.demo.android.constraint

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import com.gxd.demo.android.R

class ConstraintFlowActivity : Activity() {
    private val flow by lazy { findViewById<Flow>(R.id.flow) }
    private var orientation = Flow.VERTICAL
    private var wrapMode = Flow.WRAP_NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.layout_constraint_flow)
    }

    fun onClick(v: View) {
        val constraintLayout = v as ConstraintLayout

        // 动态创建按钮
        val newButton = Button(this).apply {
            text = "New Button"
            id = View.generateViewId()
        }

        constraintLayout.addView(newButton)

        val currentIds = flow.referencedIds.toMutableList()
        currentIds.add(newButton.id)
        flow.referencedIds = currentIds.toIntArray()
    }

    fun onBtn1Click(v: View) {
        orientation = if (orientation == Flow.VERTICAL) Flow.HORIZONTAL else Flow.VERTICAL
        flow.setOrientation(orientation)
    }

    fun onBtn2Click(v: View) {
        wrapMode = when (wrapMode) {
            Flow.WRAP_NONE -> Flow.WRAP_CHAIN
            Flow.WRAP_CHAIN -> Flow.WRAP_ALIGNED
            else -> Flow.WRAP_NONE
        }
        flow.setWrapMode(wrapMode)
    }
}