package com.demo.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MutableStateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
//            MutableStateOf2()
            AutoOptimizeRecompose()
        }
    }

    @Composable
    private fun MutableStateOf1() {
        var name by remember { mutableStateOf("gxd") }

        Column {
            Text(name, fontSize = 22.sp)

            Button(
                onClick = { name = changeText(name) },
                Modifier
                    .padding(20.dp, 10.dp)
                    .fillMaxWidth()
            ) {
                Text("更新")
            }
        }

        lifecycleScope.launch {
            delay(3000)
            name = changeText(name)
        }
    }

    private fun changeText(text: String): String = if (text == "gxd") "郭晓东" else "gxd"

    @Composable
    fun ShowNetworkRequestData(url: String) {
        val result = remember(url) { requestNetworkData(url) }
        Text("显示耗时计算结果：$result")
    }

    private fun requestNetworkData(url: String): String = "$url-->result"

    @Composable
    fun MutableStateOf2() {
        val nums = remember { mutableStateListOf(1, 2, 3) }
        Column {
            Button(onClick = {
                nums += nums.last() + 1
            }) {
                Text("加一")
            }
            for (num in nums) {
                println("gxd...eeee")
                Text("第 $num 个文字")
            }
        }
    }

    var user = User("aaa")

    @Composable
    fun AutoOptimizeRecompose() {
        var flag by remember { mutableStateOf(1) }
        Column {
            Text(flag.toString())
            PassiveRecompose(user)
            Button(onClick = {
                flag++
                user = User("bbb")
//                user.username = "bbb"
            }, Modifier.fillMaxWidth()) {
                Text("被动Recompose")
            }
        }
    }

    @Composable
    fun PassiveRecompose(user: User) {
        println("gxd...$user")
        Text(user.toString())
    }

    @Stable
    data class User(var username: String)
}