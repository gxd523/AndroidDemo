package com.demo.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.intellij.lang.annotations.JdkConstants

class ModifierActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val modifier = Modifier
                .background(Color.Yellow)
                .padding(100.dp)
                .padding(50.dp)
                .size(200.dp)
                .size(100.dp)
                .background(Color.Red)
            println("gxd...a...${modifier.hashCode()}")
            Custom(modifier)
        }
        AAA.bbb()
    }

    @Composable
    fun Custom(modifier: Modifier = Modifier) {
        Box(
            modifier.background(Color.Blue)
        ) {
        }
    }
}

interface AAA {
    companion object : AAA

    fun aaa() {
        println("aaa")
    }
}

fun AAA.bbb() {
    println("bbb")
}

object BBB : AAA {

}