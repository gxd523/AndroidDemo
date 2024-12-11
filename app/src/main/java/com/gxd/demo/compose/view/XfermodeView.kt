package com.gxd.demo.compose.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Xfermode
import android.util.AttributeSet

/**
 * Xfermode使用示例
 */
class XfermodeView(context: Context, attrs: AttributeSet? = null) : AbsCustomView(context, attrs) {
    private val layerBounds by lazy { RectF() }
    private lateinit var xfermode: Xfermode
    private var circleBitmap: Bitmap? = null
    private var squareBitmap: Bitmap? = null

    init {
        setLayerType(LAYER_TYPE_NONE, null)
    }

    fun init(porterDuffMode: PorterDuff.Mode = PorterDuff.Mode.SRC_OVER) {
        xfermode = PorterDuffXfermode(porterDuffMode)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w == 0 || h == 0) return

        layerBounds.set(
            leftOffset, topOffset, leftOffset + contentSize.toFloat(), topOffset + contentSize.toFloat()
        )

        circleBitmap = Bitmap.createBitmap(contentSize, contentSize, Bitmap.Config.ARGB_8888)
        squareBitmap = Bitmap.createBitmap(contentSize, contentSize, Bitmap.Config.ARGB_8888)

        val canvas = Canvas()
        canvas.setBitmap(circleBitmap)
        paint.color = Color.RED
        canvas.drawOval(contentSize / 3f, 0f, contentSize.toFloat(), contentSize * 2 / 3f, paint)

        canvas.setBitmap(squareBitmap)
        paint.color = Color.BLUE
        canvas.drawRect(0f, contentSize / 3f, contentSize * 2 / 3f, contentSize.toFloat(), paint)
    }

    override fun onDraw(canvas: Canvas) {
        if (!::xfermode.isInitialized) return

        val saveCount = canvas.saveLayer(layerBounds, null)

        circleBitmap?.let {
            canvas.drawBitmap(it, leftOffset, topOffset, paint)
        }

        squareBitmap?.let {
            paint.xfermode = xfermode
            canvas.drawBitmap(it, leftOffset, topOffset, paint)
            paint.xfermode = null
        }

        canvas.restoreToCount(saveCount)
    }
}