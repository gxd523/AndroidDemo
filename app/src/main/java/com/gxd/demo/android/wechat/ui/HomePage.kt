package com.gxd.demo.android.wechat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gxd.demo.android.wechat.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomePage() {
    val viewModel: HomeViewModel = viewModel()
    LaunchedEffect(Unit) { viewModel.requestChatList() }
    Column {
        val pagerState = rememberPagerState(pageCount = { 4 })
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { pagerState ->
                viewModel.selectedTab = pagerState
            }
        }
        HorizontalPager(pagerState, Modifier.weight(1f)) { pageIndex ->
            val chatList by viewModel.chatList.collectAsStateWithLifecycle()
            when (pageIndex) {
                0 -> ChatList(chatList)
                1 -> ContactsPage()
                2 -> DiscoveryPage()
                3 -> MePage()
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