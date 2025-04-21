package com.gxd.demo.android.monitor

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.util.Printer
import androidx.core.graphics.createBitmap
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * 通过「子线程Handler」打印「主线程堆栈信息」定位耗时点
 * todo 在「Compose组件」的「点击事件」中竟然无法监测到耗时操作...
 */
object MainThreadMonitor {
    private const val THRESHOLD_MS = 1_000L
    private const val SEPARATOR = "------------------------------"

    private val handlerThread by lazy { HandlerThread("main-monitor").also { it.start() } }
    private val handler by lazy { Handler(handlerThread.looper) }
    private var stackInfo = ""
    private val runnable by lazy {
        object : Runnable {
            override fun run() {
                stackInfo = Looper.getMainLooper().thread.stackTrace.joinToString("\n")
            }
        }
    }

    fun startMonitoring() {
        Looper.getMainLooper().setMessageLogging(object : Printer {
            private var startTime = 0L
            private var logMsg = ""

            override fun println(log: String?) {
                if (log?.startsWith(">>>>> Dispatching to") == true) {
                    startTime = SystemClock.elapsedRealtime()
                    logMsg = log
                    handler.removeCallbacksAndMessages(null)
                    stackInfo = ""
                    handler.postDelayed(runnable, THRESHOLD_MS)
                } else if (log?.startsWith("<<<<< Finished to") == true) {
                    val cost = SystemClock.elapsedRealtime() - startTime
                    if (cost < THRESHOLD_MS) return

                    val msg = "${SEPARATOR}\n主线程处理消息耗时: $cost\n消息内容: $logMsg\n调用栈: $stackInfo\n${SEPARATOR}"
                    Log.e("ggg", msg)
                    handler.removeCallbacksAndMessages(null)
                    stackInfo = ""
                }
            }
        })
    }

    /**
     * 用来测试
     */
    fun testCostTimeTask() {
        when (Random.nextInt(3)) {
            0 -> cpuTask()
            1 -> canvasTask()
            else -> sleepTask()
        }
    }

    /**
     * 用来测试
     */
    private fun canvasTask(n: Int = Int.MAX_VALUE / 1_000_000): Int {
        val bitmap = createBitmap(5_000, 5_000)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        repeat(1_000_00) { canvas.drawCircle(it % 5_000f, it % 5_000f, 100f, paint) }
        return n
    }

    /**
     * 用来测试
     */
    private fun sleepTask() {
        Thread.sleep(2_000)
        Log.e("ggg", "sleep 2000 ms")
    }

    /**
     * 用来测试
     */
    private fun cpuTask() {
        val startTime = System.currentTimeMillis()
        var count = 0
        for (i in 2..999999) {
            if (isPrime(i)) count++
        }
        val duration = System.currentTimeMillis() - startTime
        Log.e("ggg", "计算密集型，耗时 " + duration + "ms")
    }

    /**
     * 用来测试
     */
    private fun isPrime(n: Int): Boolean {
        var i = 2
        while (i <= sqrt(n.toDouble())) {
            if (n % i == 0) return false
            i++
        }
        return true
    }
}