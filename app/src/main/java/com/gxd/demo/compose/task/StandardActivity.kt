package com.gxd.demo.compose.task

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemGesturesPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier

class StandardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(Modifier.systemGesturesPadding().fillMaxSize()) {
                Text("${this@StandardActivity.javaClass.simpleName}")
                val activityName1 = "OtherStandardActivity"
                Button({
                    Intent().apply {
                        val packageName = "com.gxd.demo.other"
                        ComponentName(packageName, "$packageName.launchmode.$activityName1").let(::setComponent)
                    }.let(::startActivity)
                }) {
                    Text("启动$activityName1")
                }
                val activityName2 = "OtherSingleTaskActivity"
                Button({
                    Intent().apply {
                        val packageName = "com.gxd.demo.other"
                        ComponentName(packageName, "$packageName.launchmode.$activityName2").let(::setComponent)
                    }.let(::startActivity)
                }) {
                    Text("启动$activityName2")
                }
                val activityName3 = "OtherSingleInstanceActivity"
                Button({
                    Intent().apply {
                        val packageName = "com.gxd.demo.other"
                        ComponentName(packageName, "$packageName.launchmode.$activityName3").let(::setComponent)
                    }.let(::startActivity)
                }) {
                    Text("启动$activityName3")
                }
                val activityName4 = "AllowTaskReparentingActivity"
                Button({
                    Intent().apply {3
                        val packageName = "com.gxd.demo.other"
                        ComponentName(packageName, "$packageName.launchmode.$activityName4").let(::setComponent)
                    }.let(::startActivity)
                }) {
                    Text("启动$activityName4")
                }
                val activityName5 = "TaskAffinityActivity"
                Button({
                    Intent().apply {
                        val packageName = "com.gxd.demo.other"
                        ComponentName(packageName, "$packageName.launchmode.$activityName5").let(::setComponent)
                    }.let(::startActivity)
                }) {
                    Text("启动$activityName5")
                }
            }
        }
    }
}