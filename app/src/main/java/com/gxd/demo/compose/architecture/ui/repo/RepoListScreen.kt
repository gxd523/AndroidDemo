package com.gxd.demo.compose.architecture.ui.repo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemGesturesPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.elevatedCardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoListScreen(modifier: Modifier = Modifier) {
    val viewModel: RepoListViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PullToRefreshBox(
        uiState.isLoading,
        { viewModel.pullToRefresh() },
        modifier.systemGesturesPadding().fillMaxSize()
    ) {
        ConstraintLayout(Modifier.fillMaxSize()) {
            var text by remember(uiState.username) { mutableStateOf(uiState.username) }
            val (
                textFieldId,
                textId,
                lazyColumnId,
            ) = createRefs()
            BasicTextField(
                text,
                {
                    text = it
                    viewModel.updateUsername(it)
                },
                Modifier
                    .padding(5.dp)
                    .background(Color.LightGray, CircleShape)
                    .fillMaxWidth()
                    .padding(10.dp)
                    .wrapContentHeight()
                    .constrainAs(textFieldId) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    },
            )
            if (uiState.repoList.isEmpty()) {
                Text(uiState.errorMsg, Modifier.constrainAs(textId) {
                    start.linkTo(parent.start)
                    top.linkTo(textFieldId.bottom)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }, fontSize = 50.sp)
                return@ConstraintLayout
            }
            LazyColumn(Modifier.fillMaxSize().constrainAs(lazyColumnId) {
                start.linkTo(parent.start)
                top.linkTo(textFieldId.bottom, margin = 60.dp)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {
                items(uiState.repoList.size) { index ->
                    RepoItemComponent(uiState, index)
                }
            }
        }
    }
}

@Composable
private fun RepoItemComponent(uiState: RepoListUiState, index: Int) = Card(
    Modifier.fillMaxWidth().padding(15.dp).clickable {},
    elevation = elevatedCardElevation(defaultElevation = 8.dp),
    shape = RoundedCornerShape(3.dp)
) {
    Column(Modifier.padding(10.dp)) {
        val repo = uiState.repoList[index]
        Text(buildAnnotatedString {
            append("库名: ")
            val spanStyle = SpanStyle(fontWeight = FontWeight.W900, color = Color(0xFF4552B8))
            withStyle(spanStyle) { append(repo.name) }
        })
        Text(buildAnnotatedString {
            append("地址: ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.W900)) { append(repo.url) }
        })
    }
}