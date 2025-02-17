package com.gxd.demo.other.launchmode

import android.app.ActivityManager
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.MessageDigest

class OtherApplication : Application() {
    private val appScope by lazy { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    override fun onCreate() {
        super.onCreate()
        appScope.launch {
            while (true) {
                val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
                val appTaskList = activityManager.appTasks
                if (appTaskList.isNullOrEmpty()) {
                    Log.d("ggg", "没有任务栈")
                    return@launch
                }
                if (appTaskList.size > 1) Log.d("ggg", "任务栈有${appTaskList.size}个")
                appTaskList.forEach { task ->
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return@launch
                    val taskInfo = task.taskInfo
                    Log.d(
                        "ggg",
                        "${Process.myPid()}...Other...${taskInfo.baseActivity?.getTaskAffinity()}...${taskInfo.taskId}...${taskInfo.baseActivity?.info()}...${taskInfo.topActivity?.info()}...${taskInfo.numActivities}"
                    )
                }
                delay(5_000)
            }
        }
        Log.d("ggg", "OtherApplication...${Process.myPid()}...${getAppSignature()}")
    }

    private fun ComponentName.getTaskAffinity(): String =
        packageManager.getActivityInfo(this, PackageManager.GET_META_DATA).taskAffinity

    private fun Context.getAppSignature(): String? {
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            PackageManager.GET_SIGNING_CERTIFICATES
        } else {
            PackageManager.GET_SIGNATURES
        }
        val packageInfo = packageManager.getPackageInfo(packageName, flags)
        // 获取签名数组
        val signature = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.signingInfo?.apkContentsSigners
        } else {
            packageInfo.signatures
        }?.getOrNull(0) ?: return null
        // 获取签名的 SHA-1 摘要
        val messageDigest = MessageDigest.getInstance("SHA-1")
        val digestByteArray = messageDigest.digest(signature.toByteArray())

        return digestByteArray.joinToString("") { String.format("%02x", it).uppercase() }
    }

    private fun ComponentName.info() = toString().substring(toString().lastIndexOf("."), toString().length - 1)
}