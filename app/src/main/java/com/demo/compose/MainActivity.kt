package com.demo.compose

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.transform.RoundedCornersTransformation
import com.demo.compose.common.CardItemView
import com.google.accompanist.coil.rememberCoilPainter
import kotlin.reflect.KClass

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val dataList = remember {
                mutableStateListOf(
                    CaseActivity::class,
                    ModifierActivity::class,
                    MutableStateActivity::class,
                    AnimationActivity::class,
                    DrawActivity::class,
                    LayoutActivity::class,
                    TouchActivity::class,
                    AndroidViewAddComposeActivity::class,
                    ComposeAddAndroidViewActivity::class,
                    ComposeAddAndroidViewActivity::class,
                    ComposeAddAndroidViewActivity::class,
                    ComposeAddAndroidViewActivity::class,
                    ComposeAddAndroidViewActivity::class,
                    ComposeAddAndroidViewActivity::class,
                    ComposeAddAndroidViewActivity::class,
                )
            }
            MainListView(dataList)
        }
    }

    @Composable
    fun <T : ComponentActivity> MainListView(dataList: List<KClass<out T>>) {
        val cornerRadiusPx = with(LocalDensity.current) { 0.dp.toPx() }
        Log.d("gxd", "cornerRadiusPx = $cornerRadiusPx")
        LazyColumn(Modifier.fillMaxWidth()) {
            item {
                Image(
                    rememberCoilPainter(
                        "https://picsum.photos/300/300",
                        requestBuilder = {
                            transformations(
                                RoundedCornersTransformation(cornerRadiusPx)
                            )
                        },
                        previewPlaceholder = android.R.mipmap.sym_def_app_icon
                    ),
                    null,
                    Modifier
                        .fillMaxWidth()
                        .requiredHeight(200.dp),
                    contentScale = ContentScale.FillWidth
                )
            }
            items(dataList) { item ->
                CardItemView(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable {
                            Intent(this@MainActivity, item.java).let(::startActivity)
                        }, title = item.java.simpleName)
            }
        }
    }

    @Composable
    private fun <T : ComponentActivity> ItemView(item: KClass<out T>) {
        Button(
            onClick = { Intent(this@MainActivity, item.java).let(::startActivity) },
            Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(android.R.mipmap.sym_def_app_icon), "")
                Text(
                    item.java.simpleName,
                    fontSize = 26.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}