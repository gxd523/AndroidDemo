package com.demo.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview

class ComposeViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_android_view)

        val composeView = findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            SimpleCase()
        }
    }

    @Preview
    @Composable
    fun SimpleCase() {
        Text("Hello Compose!")
    }
}