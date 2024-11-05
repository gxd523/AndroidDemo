package com.gxd.demo.compose.case

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gxd.demo.compose.util.halfScreenHeight
import com.gxd.demo.compose.util.screenSizePercent


/**
 * 自动换行的流式布局
 */
@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun FlowCase() {
    val filters = listOf("Washer/Dryer", "Ramp access", "Garden", "Cats OK", "Dogs OK", "Smoke-free")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        filters.forEach { title ->
            var selected by remember { mutableStateOf(false) }
            val leadingIcon: @Composable () -> Unit = {
                Icon(Icons.Default.Check, null, tint = if (selected) Color.Green else Color.Unspecified)
            }
            FilterChip(
                selected,
                onClick = { selected = !selected },
                label = { Text(title, color = if (selected) Color.Green else Color.White) },
                leadingIcon = if (selected) leadingIcon else null
            )
        }
    }
}

/**
 * 可以设置边框、圆角、阴影等
 */
@Preview
@Composable
fun SurfaceCase() {
    Surface(Modifier.width(300.dp).height(100.dp), RoundedCornerShape(8.dp), shadowElevation = 10.dp) {
        Row {
            Image(
                painterResource(android.R.mipmap.sym_def_app_icon), null,
                Modifier.size(100.dp), contentScale = ContentScale.Crop
            )
            Spacer(Modifier.padding(horizontal = 12.dp))
            Column(Modifier.fillMaxHeight(), Arrangement.Center) {
                Text("Liratie", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.padding(vertical = 8.dp))
                Text("李贝")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BoxWithConstraintsCase(widthPercent: Int = 80) {
    BoxWithConstraints(Modifier.screenSizePercent(widthPercent, 15), Alignment.Center) {
        if (this.maxWidth < 300.dp) Text("Small Screen") else Button(onClick = {}) { Text("Large Screen") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TopAppBarCase() {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("主页", "我喜欢", "设置")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("主页") },
                navigationIcon = { IconButton(onClick = {/*TODO*/ }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                actions = {
                    IconButton(onClick = {/*TODO*/ }) { Icon(Icons.Filled.Search, null) }
                    IconButton(onClick = {/*TODO*/ }) { Icon(Icons.Filled.MoreVert, null) }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                items.forEachIndexed { index, item ->
                    Column(
                        Modifier.weight(1f).clickable { selectedItem = index },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(if (selectedItem == index) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, null)
                        Text(item)
                    }
                }
            }
        },
        modifier = Modifier.halfScreenHeight(30)
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            Text("Main content goes here", Modifier.align(Alignment.Center))
        }
    }
}

@Preview
@Composable
fun PagerCase() {
//HorizontalPager() { }
}