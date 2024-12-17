package com.gxd.demo.compose.view.util

import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import coil3.Bitmap

fun getBitmap(resources: Resources, @DrawableRes drawableResId: Int, size: Int): Bitmap {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(resources, drawableResId, options)
    options.inJustDecodeBounds = false
    options.inDensity = resources.displayMetrics.densityDpi * options.outWidth / size
    return BitmapFactory.decodeResource(resources, drawableResId, options)
}