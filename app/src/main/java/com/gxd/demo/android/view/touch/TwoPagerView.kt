package com.gxd.demo.android.view.touch

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.gxd.demo.android.view.AbsCustomView

class TwoPagerView(context: Context, attrs: AttributeSet? = null) : AbsCustomView(context, attrs) {
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }
}