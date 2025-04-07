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

class OtherMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(Modifier.systemGesturesPadding().fillMaxSize()) {
                Text("${this@OtherMainActivity.javaClass.simpleName}")
                val activityName = "OtherStandardActivity"
                Button({
                    Intent().apply {
                        val packageName = this@OtherMainActivity.packageName
                        ComponentName(packageName, "$packageName.launchmode.$activityName").let(::setComponent)
                    }.let(::startActivity)
                }) {
                    Text("启动$activityName")
                }
                val activityName1 = "LaunchModeActivity"
                Button({
                    val packageName = "com.gxd.demo.android"
                    val intent = packageManager.getLaunchIntentForPackage(packageName)
                    intent.let(::startActivity)
//                    Intent().apply {


//                        ComponentName(packageName, "$packageName.task.$activityName1").let(::setComponent)
//                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT)
//                    }.let(::startActivity)
                }) {
                    Text("启动$activityName")
                }
            }
        }
    }
}