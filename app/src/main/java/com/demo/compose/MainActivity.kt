package com.demo.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.ui.res.painterResource
import com.google.accompanist.coil.rememberCoilPainter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                Text("Hello Compose!")
                Image(painterResource(android.R.mipmap.sym_def_app_icon), null)
                Image(rememberCoilPainter("https://picsum.photos/300/300"), null)
            }
        }
    }
}