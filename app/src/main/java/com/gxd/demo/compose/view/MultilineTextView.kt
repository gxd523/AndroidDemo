package com.gxd.demo.compose.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.gxd.demo.compose.R

/**
 * 多行文字环绕图片
 * paint.breakText
 * paint.fontSpacing
 */
class MultilineTextView(context: Context, attrs: AttributeSet? = null) : AbsCustomView(context, attrs) {
    companion object {
        private const val DEFAULT_TEXT =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam arcu erat, suscipit a vehicula eu, condimentum vel turpis. Aenean viverra euismod neque nec hendrerit. Sed fermentum ex vitae diam tristique, in accumsan arcu convallis. Nam malesuada arcu quis purus cursus, non suscipit turpis egestas. Fusce massa ex, molestie vel elit eget, porta malesuada quam."
    }

    private val fontMetric by lazy { Paint.FontMetrics() }
    private val imageSize by lazy { 100.dp }
    private val imageSrcRect by lazy { Rect() }
    private val imageDstRect by lazy { Rect() }
    private lateinit var image: Bitmap

    init {
        paint.color = Color.BLACK
        paint.typeface = ResourcesCompat.getFont(context, R.font.oppo_sans)
        paint.textSize = 20f.sp
        paint.getFontMetrics(fontMetric)
    }

    fun init(@DrawableRes drawableId: Int = R.drawable.avatar_png) {
        image = BitmapFactory.decodeResource(resources, drawableId)
        imageSrcRect.set(0, 0, image.width, image.height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        imageDstRect.set(width - imageSize, height / 2 - imageSize / 2, width, height / 2 + imageSize / 2)
    }

    override fun onDraw(canvas: Canvas) {
        if (!::image.isInitialized) return

        canvas.drawBitmap(image, imageSrcRect, imageDstRect, paint)

        var textStartIndex = 0
        var verticalStartY = -fontMetric.top
        var maxLineWidth = 0f

        while (textStartIndex < DEFAULT_TEXT.length) {
            maxLineWidth = if (
                verticalStartY + fontMetric.bottom < imageDstRect.top || verticalStartY + fontMetric.top > imageDstRect.bottom
            ) {
                width.toFloat()
            } else {
                imageDstRect.left.toFloat()
            }
            val breakTextPos = paint.breakText(
                DEFAULT_TEXT, textStartIndex, DEFAULT_TEXT.length, true, maxLineWidth, null
            )
            canvas.drawText(
                DEFAULT_TEXT, textStartIndex, textStartIndex + breakTextPos, 0f, verticalStartY, paint
            )
            textStartIndex += breakTextPos
            verticalStartY += paint.fontSpacing // TODO: 更推荐的文字间隔
        }
    }
}