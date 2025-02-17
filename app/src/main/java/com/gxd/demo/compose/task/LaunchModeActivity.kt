package com.gxd.demo.compose.task

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemGesturesPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LaunchModeActivity : ComponentActivity() {
    private val appScope by lazy { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        "${Process.myPid()}...${taskInfo.baseActivity?.getTaskAffinity()}...${taskInfo.taskId}...${taskInfo.baseActivity?.info()}...${taskInfo.topActivity?.info()}...${taskInfo.numActivities}"
                    )
                }
                delay(5_000)
            }
        }

        setContent {
            Column(Modifier.systemGesturesPadding().fillMaxSize()) {
                Text("${this@LaunchModeActivity.javaClass.simpleName}")
                val activityName = "StandardActivity"
                Button({
                    Intent().apply {
                        val packageName = this@LaunchModeActivity.packageName
                        ComponentName(packageName, "$packageName.task.$activityName").let(::setComponent)
                    }.let(::startActivity)
                }) {
                    Text("启动$activityName")
                }
            }
        }
    }

    private fun ComponentName.getTaskAffinity(): String =
        packageManager.getActivityInfo(this, PackageManager.GET_META_DATA).taskAffinity

    private fun ComponentName.info() = toString().substring(toString().lastIndexOf("."), toString().length - 1)
}