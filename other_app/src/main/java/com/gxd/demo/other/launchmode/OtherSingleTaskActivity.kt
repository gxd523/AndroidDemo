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

class OtherSingleTaskActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(Modifier.systemGesturesPadding().fillMaxSize()) {
                Text("${this@OtherSingleTaskActivity.javaClass.simpleName}")
                val activityName = "OtherInstanceActivity"
                Button({
                    Intent().apply {
                        val packageName = this@OtherSingleTaskActivity.packageName
                        ComponentName(packageName, "$packageName.$activityName").let(::setComponent)
                    }.let(::startActivity)
                }) {
                    Text("启动$activityName")
                }
            }
        }
    }
}