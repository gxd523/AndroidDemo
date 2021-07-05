package com.demo.compose

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.flow

/**
 * Compose里面引用Android View
 */
class AndroidViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SimpleCase()
        }
    }

    @Preview
    @Composable
    private fun SimpleCase() {
        Column {
            val liveData = remember { MutableLiveData(1) }
            val liveDataInCompose by liveData.observeAsState()

            val flow = flow<Int> {}
            val flowInCompose by flow.collectAsState(initial = 1)

            var toggle by remember { mutableStateOf(false) }
            Text("compose text")
            AndroidView(
                {
//                    val layoutInflater = LayoutInflater.from(it)
//                    layoutInflater.inflate(R.layout.activity_android_view, null, false)// TODO: 2021/7/3 root
                    View(it).apply { setBackgroundColor(android.graphics.Color.BLUE) }
                },
                Modifier
                    .size(100.dp)
                    .background(Color(0x33FF00FF))
            ) { view ->// Recompose过程执行，此处执行Android View更新UI代码
//                view.findViewById<TextView>(R.id.activity_android_view_title_tv).text = "haha"
                view.setBackgroundColor(if (toggle) android.graphics.Color.RED else android.graphics.Color.BLUE)
            }
            Image(painterResource(android.R.mipmap.sym_def_app_icon), null, Modifier.clickable {
                toggle = !toggle
            })
        }
    }
}