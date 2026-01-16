package com.gxd.demo.android.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.withStyledAttributes
import com.gxd.demo.android.R
import com.gxd.demo.android.view.util.dpp

/**
 * 自定义属性动画
 * invalidate()
 * 「onDraw」中使用该属性
 */
class FloatingLabelEditText(context: Context, attrs: AttributeSet? = null) : AppCompatEditText(context, attrs) {
    var useFloatingLabel = false
        set(value) {
            if (field == value) return
            field = value
            if (value) {
                paint.textSize = labelTextSize
                setPadding(paddingStart, (paddingTop + labelTextSize + labelTopMargin).toInt(), paddingEnd, paddingBottom)
            } else {
                setPadding(paddingStart, (paddingTop - labelTextSize - labelTopMargin).toInt(), paddingEnd, paddingBottom)
            }
        }

    private val labelTextSize = 12f.dpp
    private val labelTopMargin = 8.dpp
    private val labelHorizontalOffset = 5f.dpp
    private val labelVerticalOffset = 23f.dpp
    private val labelAnimateVerticalOffset = 16f.dpp

    private val paint by lazy { Paint() }

    private var floatingLabelShown = false
    private var floatingLabelFraction = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val floatingLabelAnimator by lazy {
        ObjectAnimator.ofFloat(this, "floatingLabelFraction", 0f, 1f).also {
            it.duration = 800
        }
    }

    init {
        // 从「attrs」集合中过滤出「FloatingLabelEditText」集合
        context.withStyledAttributes(attrs, R.styleable.FloatingLabelEditText) {
            useFloatingLabel = getBoolean(R.styleable.FloatingLabelEditText_useFloatingLabel, true)
        }
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        if (!useFloatingLabel) return

        if (floatingLabelShown && text.isNullOrEmpty()) {
            floatingLabelAnimator.reverse()
            floatingLabelShown = false
        } else if (!floatingLabelShown && !text.isNullOrEmpty()) {
            floatingLabelAnimator.start()
            floatingLabelShown = true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!useFloatingLabel) return

        paint.alpha = (floatingLabelFraction * 0xff).toInt()
        val y = labelVerticalOffset + (1 - floatingLabelFraction) * labelAnimateVerticalOffset
        canvas.drawText(hint.toString(), labelHorizontalOffset, y, paint)
    }
}