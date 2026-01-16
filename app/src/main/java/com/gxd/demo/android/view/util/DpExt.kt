package com.gxd.demo.android.view.util

import android.content.res.Resources
import android.util.TypedValue

val Float.dpp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

val Float.spp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics)

val Int.dpp
    get() = this.toFloat().dpp.toInt()

val Int.spp
    get() = this.toFloat().spp.toInt()