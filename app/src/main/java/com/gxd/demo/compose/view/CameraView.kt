package com.gxd.demo.compose.view

import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import androidx.core.graphics.withSave
import coil3.Bitmap
import com.gxd.demo.compose.R
import com.gxd.demo.compose.view.util.getBitmap

/**
 * 「几何变换」+「Camera」+「属性动画」实现照片翻转
 * ```
 * val degrees = 90f
 * val keyFrame1 = Keyframe.ofFloat(0f, 0f)
 * val keyFrame2 = Keyframe.ofFloat(0.2f, degrees * 0.8f)
 * val keyFrame3 = Keyframe.ofFloat(0.8f, degrees * 1.2f)
 * val keyFrame4 = Keyframe.ofFloat(1f, degrees * 2f)
 * val creaseKeyFrameHolder = PropertyValuesHolder.ofKeyframe(
 *     CameraView.CREASE_DEGREES,
 *     keyFrame1,
 *     keyFrame2,
 *     keyFrame3,
 *     keyFrame4
 * )
 * val topFlipPropertyHolder = PropertyValuesHolder.ofFloat(CameraView.TOP_FLIP_DEGREES, -degrees)
 * val bottomFlipPropertyHolder = PropertyValuesHolder.ofFloat(CameraView.BOTTOM_FLIP_DEGREES, degrees)
 * ObjectAnimator.ofPropertyValuesHolder(
 *     view,
 *     topFlipPropertyHolder,
 *     bottomFlipPropertyHolder,
 *     creaseKeyFrameHolder
 * ).also {
 *     it.duration = 1_000
 *     it.startDelay = 1_000
 *     it.repeatMode = ObjectAnimator.REVERSE
 *     it.repeatCount = ObjectAnimator.INFINITE
 * }.start()
 * ```
 */
class CameraView(context: Context, attrs: AttributeSet? = null) : AbsCustomView(context, attrs) {
    companion object {
        const val BOTTOM_FLIP_DEGREES = "bottomFlipDegrees"
        const val TOP_FLIP_DEGREES = "topFlipDegrees"
        const val CREASE_DEGREES = "creaseDegrees"
    }

    var topFlipDegrees = 0f
        set(value) {
            field = value
            invalidate()
        }
    var bottomFlipDegrees = 0f
        set(value) {
            field = value
            invalidate()
        }
    var creaseDegrees = -30f
        set(value) {
            field = value
            invalidate()
        }

    private val camera by lazy {
        Camera().apply {
            setLocation(0f, 0f, -10 * resources.displayMetrics.density)
        }
    }
    private val matrix = Matrix()
    private lateinit var bitmap: Bitmap

    fun init() {
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = getBitmap(resources, R.drawable.avatar_png, contentSize)
        contentOffsetX = (w - bitmap.width) / 2f
        contentOffsetY = (h - bitmap.height) / 2f
    }

    override fun onDraw(canvas: Canvas) {
        val halfSize = bitmap.width / 2
        val clipRadius = halfSize * 2

        canvas.withSave {
            canvas.translate(contentOffsetX + halfSize, contentOffsetY + halfSize)
            canvas.rotate(creaseDegrees)
            camera.save()
            camera.rotateX(topFlipDegrees)
            camera.applyToCanvas(canvas)
            camera.restore()
            canvas.clipRect(-clipRadius, -clipRadius, clipRadius, 0)
            canvas.rotate(-creaseDegrees)
            canvas.translate(-(contentOffsetX + halfSize), -(contentOffsetY + halfSize))
            canvas.drawBitmap(bitmap, contentOffsetX, contentOffsetY, paint)
        }

        canvas.withSave {
            canvas.translate(contentOffsetX + halfSize, contentOffsetY + halfSize)
            canvas.rotate(creaseDegrees)
            camera.save()
            camera.rotateX(bottomFlipDegrees)
            camera.applyToCanvas(canvas)
            camera.restore()
            canvas.clipRect(-clipRadius, 0, clipRadius, clipRadius)
            canvas.rotate(-creaseDegrees)
            canvas.translate(-(contentOffsetX + halfSize), -(contentOffsetY + halfSize))
            canvas.drawBitmap(bitmap, contentOffsetX, contentOffsetY, paint)
        }

        matrix.reset()
        matrix.setScale(0.5f, 0.5f, 0f, 0f)
        matrix.postRotate(180f, halfSize / 2f, halfSize / 2f)
        matrix.postTranslate(contentOffsetX + halfSize - halfSize / 2, contentOffsetY + halfSize - halfSize / 2)
//        canvas.drawBitmap(bitmap, matrix, paint)
    }
}