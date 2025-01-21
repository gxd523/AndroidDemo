package com.gxd.demo.compose.architecture.ui.repo

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gxd.demo.compose.architecture.uitl.launchCustomChromeTab
import com.gxd.demo.compose.ui.theme.WechatTheme
import com.gxd.demo.lib.dal.repository.Repo

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
        Column(Modifier.fillMaxSize()) {
            var text by remember(uiState.username) { mutableStateOf(uiState.username) }
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
                    .wrapContentHeight(),
            )
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                if (uiState.repoList.isEmpty()) {
                    Text(uiState.errorMsg, fontSize = 50.sp)
                    return@Column
                }
                LazyColumn {
                    items(uiState.repoList.size) { index ->
                        uiState.repoList
                        RepoItemComponent(uiState.repoList[index])
                    }
                }
            }
        }
    }
}

@Composable
private fun RepoItemComponent(repo: Repo) {
    val context = LocalContext.current
    val toolbarColor = WechatTheme.colorScheme.background.toArgb()
    Card(
        Modifier.fillMaxWidth().padding(15.dp).clickable {
            context.launchCustomChromeTab(Uri.parse(repo.url), toolbarColor)
        },
        elevation = elevatedCardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(3.dp)
    ) {
        Column(Modifier.padding(10.dp)) {

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
}