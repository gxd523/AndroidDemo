package com.gxd.demo.android.compose.case

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemGesturesPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gxd.demo.android.util.screenHeightPercent
import com.gxd.demo.android.util.screenSizePercent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
        modifier = Modifier.screenHeightPercent(30)
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            Text("Main content goes here", Modifier.align(Alignment.Center))
        }
    }
}

/**
 * 下拉刷新
 */
@Preview(name = "Pull to refresh", showBackground = true)
@Composable
fun PullToRefreshCase(modifier: Modifier = Modifier, initCount: Int = 10, delayTime: Long = 1_000) {
    val itemList = remember {
        List(initCount) { index -> index to "第${index}个初始item" }.asReversed().toMutableStateList()
    }

    var isRefreshing by remember { mutableStateOf(false) }
    var isLoadingMore by remember { mutableStateOf(false) }

    val refreshState = rememberPullToRefreshState()
    val listState = rememberLazyListState()

    val performLoadMore: suspend () -> Unit = remember {
        {
            if (!isLoadingMore && !isRefreshing) {
                try {
                    isLoadingMore = true
                    delay(delayTime)
                    itemList.add(itemList.size to "第${itemList.size}个底部item")
                } finally {
                    isLoadingMore = false
                }
            }
        }
    }

    val scope = rememberCoroutineScope()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < 0 && !listState.canScrollForward && !isLoadingMore && !isRefreshing) {
                    scope.launch { performLoadMore() }
                }
                return Offset.Zero
            }
        }
    }

    val onRefresh: () -> Unit = remember {
        {
            scope.launch {
                isRefreshing = true

                delay(delayTime)
                itemList.add(0, itemList.size to "第${itemList.size}个刷新item")
                isRefreshing = false
            }
        }
    }
    val indicator: @Composable BoxScope.() -> Unit = remember {
        {
            Indicator(refreshState, isRefreshing, Modifier.align(Alignment.TopCenter))
        }
    }
    PullToRefreshBox(
        isRefreshing,
        onRefresh,
        modifier.fillMaxSize().systemGesturesPadding(),
        refreshState,
        indicator = indicator
    ) {
        LazyColumn(Modifier.fillMaxSize().nestedScroll(nestedScrollConnection), listState) {
            items(itemList, { it.first }) { item ->
                ListItem({ Text(item.second) }, Modifier.padding(horizontal = 10.dp))
                HorizontalDivider()
            }

            if (isLoadingMore) {
                item(key = "loading_more_indicator") {
                    Box(Modifier.fillMaxWidth().padding(16.dp), Alignment.Center) {
                        CircularProgressIndicator(strokeWidth = 3.dp, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

/**
 * 自定义下拉刷新指示器
 * 在{PullToRefreshCase}中使用
 */
@Composable
fun PullToRefreshIndicator(state: PullToRefreshState, isRefreshing: Boolean, modifier: Modifier = Modifier) =
    Box(modifier, Alignment.Center) {
        Crossfade(isRefreshing, Modifier.align(Alignment.Center), tween(100)) { refreshing ->
            if (refreshing) {
                CircularProgressIndicator(Modifier.size(20.dp))
            } else {
                val distanceFraction = { state.distanceFraction.coerceIn(0f, 1f) }
                Icon(
                    Icons.Filled.ArrowDownward,
                    "Refresh",
                    Modifier
                        .size(18.dp)
                        .graphicsLayer {
                            val progress = distanceFraction()
                            this.alpha = progress
                            this.scaleX = progress
                            this.scaleY = progress
                        }
                )
            }
        }
    }

@Preview
@Composable
fun PagerCase() {
//HorizontalPager() { }
}