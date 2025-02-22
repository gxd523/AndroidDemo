package com.gxd.demo.android.util

import android.util.Log
import android.view.Choreographer

fun startFrameMonitor(
    lastFrameTimeNanos: Long = System.nanoTime(),
    skipFrameWarningLimit: Int = 30,
    skipFrameCallback: ((Long) -> Unit)? = null,
) {
    val frameCallback = ChoreographerFrameCallback(lastFrameTimeNanos, skipFrameWarningLimit, skipFrameCallback)
    Choreographer.getInstance().postFrameCallback(frameCallback)
}

class ChoreographerFrameCallback(
    private var mLastFrameTimeNanos: Long = System.nanoTime(),
    private val skipFrameWarningLimit: Int = 30,
    private val skipFrameCallback: ((Long) -> Unit)? = null,
) :
    Choreographer.FrameCallback {
    private val mFrameIntervalNanos by lazy { (1_000_000_000 / 60.0).toLong() }

    override fun doFrame(frameTimeNanos: Long) {
        if (mLastFrameTimeNanos < 0) mLastFrameTimeNanos = frameTimeNanos

        val jitterNanos = frameTimeNanos - mLastFrameTimeNanos
        if (jitterNanos >= mFrameIntervalNanos) {
            val skippedFrames = jitterNanos / mFrameIntervalNanos
            if (skippedFrames > skipFrameWarningLimit) skipFrameCallback?.invoke(skippedFrames) ?: Log.d(
                "ggg",
                "丢帧数量：$skippedFrames"
            )
        }

        mLastFrameTimeNanos = frameTimeNanos
        Choreographer.getInstance().postFrameCallback(this)
    }
}