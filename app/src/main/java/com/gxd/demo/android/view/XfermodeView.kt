package com.gxd.demo.android.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import androidx.core.graphics.createBitmap

/**
 * 「Xfermode」使用示例
 */
class XfermodeView(context: Context, attrs: AttributeSet? = null) : AbsCustomView(context, attrs) {
    var porterDuffMode = PorterDuff.Mode.XOR

    //    private val layerBounds by lazy { RectF() }
    private val xfermode by lazy { PorterDuffXfermode(porterDuffMode) }
    private var redCircleBitmap: Bitmap? = null
    private var blueSquareBitmap: Bitmap? = null

    init {
        // 设置「硬件离屏缓冲」，相比「saveLayer」不用每次绘制都创建「离屏缓冲」，性能消耗小很多
        // 默认「layerType」为「NONE」，没有「离屏缓冲」
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w == 0 || h == 0) return

//        layerBounds.set(
//            contentOffsetX, contentOffsetY,
//            contentOffsetX + contentSize.toFloat(),
//            contentOffsetY + contentSize.toFloat()
//        )

        // 注意这里「Bitmap」的要是「方块」和「圆」的「并集」大小
        redCircleBitmap = createBitmap(contentSize, contentSize)
        blueSquareBitmap = createBitmap(contentSize, contentSize)

        val canvas = Canvas()
        canvas.setBitmap(redCircleBitmap)
        paint.color = Color.RED
        canvas.drawOval(contentSize / 3f, 0f, contentSize.toFloat(), contentSize * 2 / 3f, paint)

        canvas.setBitmap(blueSquareBitmap)
        paint.color = Color.BLUE
        canvas.drawRect(0f, contentSize / 3f, contentSize * 2 / 3f, contentSize.toFloat(), paint)
    }

    override fun onDraw(canvas: Canvas) {
//        val saveCount = canvas.saveLayer(layerBounds, null)

        redCircleBitmap?.let {
            canvas.drawBitmap(it, contentOffsetX, contentOffsetY, paint)
        }

        blueSquareBitmap?.let {
            paint.xfermode = xfermode
            canvas.drawBitmap(it, contentOffsetX, contentOffsetY, paint)
            paint.xfermode = null
        }

//        canvas.restoreToCount(saveCount)
    }
}