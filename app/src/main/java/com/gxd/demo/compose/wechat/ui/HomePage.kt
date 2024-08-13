package com.gxd.demo.compose.wechat.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gxd.demo.compose.wechat.HomeViewModel
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun HomePage() {
    val viewModel: HomeViewModel = viewModel()
    Column {
        val pagerState = rememberPagerState(pageCount = { 4 })
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { pagerState ->
                viewModel.selectedTab = pagerState
            }
        }
        HorizontalPager(pagerState, Modifier.weight(1f)) { pageIndex ->
            when (pageIndex) {
                0 -> ChatList(viewModel.chatList)
                1 -> Box(Modifier.fillMaxSize())
                2 -> Box(Modifier.fillMaxSize().background(Color.Gray))
                3 -> Box(Modifier.fillMaxSize().background(Color.Blue))
                else -> Box(Modifier.fillMaxSize().background(Color.Green))
            }
        }
        val scope = rememberCoroutineScope()
        BottomBar(viewModel.selectedTab) { page ->
            viewModel.selectedTab = page
            scope.launch { pagerState.animateScrollToPage(page) }
        }
    }
}

@Preview
@Composable
private fun HomePagePreview() {
    HomePage()
}