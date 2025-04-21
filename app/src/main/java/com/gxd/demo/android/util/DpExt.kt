package com.gxd.demo.android.util

import android.content.res.Resources
import android.util.TypedValue

val Float.dip
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

val Float.sip
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics)

val Int.dip
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()

val Int.sip
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()

val Int.px
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this.toFloat(), Resources.getSystem().displayMetrics).toInt()

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()