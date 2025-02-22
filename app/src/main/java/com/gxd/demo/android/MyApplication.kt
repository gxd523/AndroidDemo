package com.gxd.demo.android

import android.app.Application
import android.os.Process
import android.util.Log
import com.gxd.demo.android.util.getAppSignature
import com.gxd.demo.android.util.startFrameMonitor
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Log.d("ggg", "MyApplication...${Process.myPid()}...${getAppSignature()}")
        startFrameMonitor(skipFrameWarningLimit = 1)
    }
}