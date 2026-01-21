package com.gxd.demo.android.architecture.ui.repo

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.elevatedCardElevation
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
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.gxd.demo.android.BuildConfig
import com.gxd.demo.android.compose.wechat.theme.WechatTheme
import com.gxd.demo.android.util.launchCustomChromeTab
import com.gxd.demo.lib.dal.repository.Repo

@Composable
fun RepoListScreen(modifier: Modifier = Modifier, viewModel: RepoListViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading by viewModel.loadingUiState.collectAsStateWithLifecycle()
    PullToRefreshBox(
        isLoading,
        { viewModel.pullToRefresh() },
        modifier.systemGesturesPadding().fillMaxSize()
    ) {
        Column(Modifier.fillMaxSize()) {
            val defaultText by viewModel.inputUsernameState.collectAsStateWithLifecycle()
            var text by remember { mutableStateOf(defaultText) }
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
                    item { GithubUserItem(uiState) }
                    if (uiState.readRepoList.isNotEmpty()) {
                        item {
                            Text(
                                uiState.readRepoList.joinToString(", ") { it.name },
                                Modifier.padding(10.dp).wrapContentHeight().background(Color.LightGray)
                            )
                        }
                    }
                    items(uiState.repoList.size) { index ->
                        uiState.repoList
                        RepoItemComponent(uiState.repoList[index], uiState.onItemClick)
                    }
                }
            }
        }
    }
}

@Composable
private fun GithubUserItem(uiState: RepoListUiState) {
    val githubUser = uiState.githubUser
    if (githubUser == null) {
        val context = LocalContext.current
        val toolbarColor = WechatTheme.colorScheme.background.toArgb()
        Button({
            val authUrl = "https://github.com/login/oauth/authorize".toUri().buildUpon()
                .appendQueryParameter("client_id", BuildConfig.GITHUB_CLIENT_ID)
                .appendQueryParameter("redirect_uri", "https://gxd523.github.io/oauth")
                .appendQueryParameter("scope", "user:all")
                .build()
            context.launchCustomChromeTab(authUrl, toolbarColor)
        }) {
            Text("Github授权登录")
        }
    } else {
        Column {
            Text("${githubUser.login}")
            Text("${githubUser.email}")
            AsyncImage(githubUser.avatarUrl, "")
        }
    }
}

/**
 * 「item」中不要直接获取「viewModel」，「onItemClick」可以放到「uiState」中
 */
@Composable
private fun RepoItemComponent(repo: Repo, onItemClick: (Repo) -> Unit) {
    val context = LocalContext.current
    val toolbarColor = WechatTheme.colorScheme.background.toArgb()
    Card(
        Modifier.fillMaxWidth().padding(15.dp).clickable {
            context.launchCustomChromeTab(repo.url.toUri(), toolbarColor)
            onItemClick.invoke(repo)
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