package com.gxd.demo.compose.view

import android.animation.TypeEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import kotlin.math.roundToInt

/**
 * ```
 * ObjectAnimator.ofObject(
 *     view,
 *     "province",
 *     ProvinceView.ProvinceEvaluator(),
 *     "北京市"
 * ).also {
 *     it.duration = 1_000
 *     it.startDelay = 1_000
 *     it.repeatMode = ObjectAnimator.REVERSE
 *     it.repeatCount = ObjectAnimator.INFINITE
 * }.start()
 * ```
 */
class ProvinceView(context: Context, attrs: AttributeSet? = null) : AbsCustomView(context, attrs) {
    companion object {
        private val provinceList by lazy {
            listOf(
                "北京市",
                "天津市",
                "上海市",
                "重庆市",
                "河北省",
                "陕西省",
                "辽宁省",
                "吉林省",
                "湖北省",
                "浙江省",
                "福建省",
                "江苏省",
                "湖南省",
                "海南省",
                "黑龙江省",
            )
        }
    }

    var province = "北京市"
        set(value) {
            field = value
            invalidate()
        }

    init {
        paint.textSize = 80f.dp
        paint.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(province, contentLeftOffset + contentSize / 2, contentTopOffset + contentSize / 2, paint)
    }

    class ProvinceEvaluator : TypeEvaluator<String> {
        override fun evaluate(fraction: Float, startValue: String, endValue: String): String {
            val startIndex = provinceList.indexOf(startValue)
            val endIndex = provinceList.indexOf(endValue)
            val currentIndex = (startIndex + (endIndex - startIndex) * fraction) % provinceList.size
            return provinceList[currentIndex.roundToInt()]
        }
    }
}