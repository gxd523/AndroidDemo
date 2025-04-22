package com.gxd.demo.android

import android.app.Application
import android.content.Context
import android.os.Process
import android.os.SystemClock
import android.util.Log
import com.gxd.demo.android.util.getAppSignature
import com.gxd.demo.android.util.getDeviceID
import com.gxd.demo.android.util.mergeDex
import com.gxd.demo.android.util.startFrameMonitor
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class MyApplication : Application() {
    companion object {
        lateinit var instance: MyApplication
    }

    val appScope by lazy { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
    var appStartTime: Long = 0

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        appStartTime = SystemClock.uptimeMillis()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        appScope.launch {
            Log.d("ggg", "pid = ${Process.myPid()}\nsignature = ${getAppSignature()}\ndeviceID = ${getDeviceID()}")
        }

        startFrameMonitor(skipFrameWarningLimit = 2)

        mergeDex("plugin.apk") // 加载「插件」用来测试「热修复」、「插件化」

//        CrashReport.initCrashReport(this, "1f8ed656d3", false)

//        AnrMonitor.start {}

//        MainThreadMonitor.startMonitoring()
    }
}