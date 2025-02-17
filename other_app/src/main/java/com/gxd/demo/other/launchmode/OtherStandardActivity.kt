package com.gxd.demo.other.launchmode

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

class OtherStandardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(Modifier.systemGesturesPadding().fillMaxSize()) {
                Text("${this@OtherStandardActivity.javaClass.simpleName}")
                val activityName = "OtherSingleTaskActivity"
                Button({
                    Intent().apply {
                        val packageName = this@OtherStandardActivity.packageName
                        ComponentName(packageName, "$packageName.launchmode.$activityName").let(::setComponent)
                    }.let(::startActivity)
                }) {
                    Text("启动$activityName")
                }
                val activityName1 = "OtherSingleInstanceActivity"
                Button({
                    Intent().apply {
                        val packageName = this@OtherStandardActivity.packageName
                        ComponentName(packageName, "$packageName.launchmode.$activityName1").let(::setComponent)
                    }.let(::startActivity)
                }) {
                    Text("启动$activityName1")
                }
                val activityName2 = "TaskAffinityActivity"
                Button({
                    Intent().apply {
                        val packageName = this@OtherStandardActivity.packageName
                        ComponentName(packageName, "$packageName.launchmode.$activityName2").let(::setComponent)
                    }.let(::startActivity)
                }) {
                    Text("启动$activityName2")
                }
            }
        }
    }
}