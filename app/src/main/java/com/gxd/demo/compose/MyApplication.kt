package com.gxd.demo.compose

import android.app.Application
import android.os.Process
import android.util.Log
import com.gxd.demo.compose.util.getAppSignature
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
    }
}