package com.gxd.demo.compose.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.OverScroller
import com.gxd.demo.compose.R
import kotlinx.coroutines.Runnable

/**
 * 实现功能：「双击缩放」、「双指捏合」、「拖动图片」
 */
class ScalableImageView(context: Context, attrs: AttributeSet? = null) : AbsCustomView(context, attrs) {
    companion object {
        private const val EXTRA_SCALE_FACTOR = 2.5f
    }

    private val gestureListener by lazy { ScalableOnGestureListener() }

    /**
     * setOnDoubleTapListener(this@ScalableImageView) 可以不用写
     */
    private val gestureDetector by lazy { GestureDetector(context, gestureListener) }
    private val refreshFlyingTask by lazy { RefreshFlyingTask() }

    /**
     * 「OverScroller」与「Scroller」的区别不仅仅是划过头「Over」，「Scroller」似乎没有起始速度
     */
    private val scroller by lazy { OverScroller(context) }
    private val scaleAnim by lazy {
        ObjectAnimator.ofFloat(this, "scaleFraction", 0f, 1f)
    }
    private lateinit var bitmap: Bitmap

    private var offsetX = 0f
    private var offsetY = 0f

    /**
     * 图片在放大状态水平方向的最大偏移量
     */
    private var maxOffsetX = 0f
    private var maxOffsetY = 0f

    private var big = false
    private var smallScale = 0f
    private var bigScale = 0f
    private var scaleFraction = 0f
        set(value) {
            field = value
            invalidate()
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = getBitmap(resources, R.drawable.test_picture, contentSize)
        contentLeftOffset = (w - bitmap.width) / 2f
        contentTopOffset = (h - bitmap.height) / 2f

        if (bitmap.width / bitmap.height.toFloat() > width / height.toFloat()) {
            smallScale = width / bitmap.width.toFloat()
            bigScale = height / bitmap.height.toFloat() * EXTRA_SCALE_FACTOR
        } else {
            smallScale = height / bitmap.height.toFloat()
            bigScale = width / bitmap.width.toFloat() * EXTRA_SCALE_FACTOR
        }
        maxOffsetX = bigScale * bitmap.width / 2 - width / 2
        maxOffsetY = bigScale * bitmap.height / 2 - height / 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.translate(offsetX * scaleFraction, offsetY * scaleFraction) // TODO: 这里乘「scaleFraction」很精妙
        val scale = smallScale + (bigScale - smallScale) * scaleFraction
        canvas.scale(scale, scale, width / 2f, height / 2f)
        canvas.drawBitmap(bitmap, contentLeftOffset, contentTopOffset, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean = gestureDetector.onTouchEvent(event)

    private inner class ScalableOnGestureListener : GestureDetector.SimpleOnGestureListener() {
        /**
         * 几乎必返回「true」
         * 「OnGestureListener」只用到了这一个方法
         */
        override fun onDown(e: MotionEvent): Boolean = true

        /**
         * 连续按2次会触发，2次间隔「300毫秒」，短于「40毫秒」也不会触发
         * 「返回值」也没用
         */
        override fun onDoubleTap(e: MotionEvent): Boolean {
            big = !big
            if (big) {
                // TODO: 这里比较难懂
                val doubleTapToCenterOffsetX = e.x - width / 2// 「双击点」到「缩放中心点」的「偏移量」
                val doubleTapToCenterOffsetY = e.y - height / 2// 「双击点」到「缩放中心点」的「偏移量」
                offsetX = -(doubleTapToCenterOffsetX * bigScale / smallScale - doubleTapToCenterOffsetX)
                offsetY = -(doubleTapToCenterOffsetY * bigScale / smallScale - doubleTapToCenterOffsetY)
                coerceInOffset()

                scaleAnim.start()
            } else {
                scaleAnim.reverse()
            }

            return true
        }

        /**
         * 注意「distance」是「前一个位置」减去「当前位置」
         */
        override fun onScroll(downEvent: MotionEvent?, currentEvent: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            if (!big) return false
            offsetX -= distanceX
            offsetY -= distanceY
            coerceInOffset()
            invalidate()
            return true
        }

        override fun onFling(downEvent: MotionEvent?, currentEvent: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (!big) return false
            scroller.fling(
                offsetX.toInt(),
                offsetY.toInt(),
                velocityX.toInt(),
                velocityY.toInt(),
                -maxOffsetX.toInt(),
                maxOffsetX.toInt(),
                -maxOffsetY.toInt(),
                maxOffsetY.toInt(),
                50.dp,
                50.dp
            )

//        for (i in 10..100 step 10) {
//            postDelayed({ refreshFling() }, i.toLong())
//        }
            postOnAnimation(refreshFlyingTask) // TODO: 这里避免不停创建Runnable对象，「postAnimation」对等待「下一帧」时才推到主线程队列中
            return true
        }

        /**
         * 校准偏移量
         */
        private fun coerceInOffset() {
            offsetX = offsetX.coerceIn(-maxOffsetX, maxOffsetX)
            offsetY = offsetY.coerceIn(-maxOffsetY, maxOffsetY)
        }
        /**
         * 支持双击时的单击回调，按下抬起后超过「300毫秒」时回调
         */
//        override fun onSingleTapConfirmed(e: MotionEvent): Boolean = false

        /**
         * 第二次按下之后的事件，很少用
         */
//        override fun onDoubleTapEvent(e: MotionEvent): Boolean = false

        /**
         * 按下「100毫秒」后回调，不管是否嵌套滑动「父控件」
         */
//        override fun onShowPress(e: MotionEvent) {}

        /**
         * 单击，基本等价于「OnClick」
         * 「返回值」：是否消费该事件，无论返回「true」或「false」无区别，「onDown」才有区别
         */
//        override fun onSingleTapUp(e: MotionEvent): Boolean = false

//        override fun onLongPress(e: MotionEvent) {}
    }

    private inner class RefreshFlyingTask : Runnable {
        override fun run() {
            val isRunning = scroller.computeScrollOffset()
            offsetX = scroller.currX.toFloat()
            offsetY = scroller.currY.toFloat()
            invalidate()

            if (!isRunning) return
            postOnAnimation(this) // TODO: 这里避免不停创建Runnable对象，「postAnimation」对等待「下一帧」时才推到主线程队列中
        }
    }
}