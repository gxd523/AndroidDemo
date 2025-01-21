package com.gxd.demo.compose.case

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.gxd.demo.compose.data.NetworkRepo
import com.gxd.demo.compose.request.service
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun SimpleRetrofitRequest(user: String = "gxd523") {
    Column {
        val scope = rememberCoroutineScope()
        var repoList by remember { mutableStateOf<List<NetworkRepo>>(emptyList()) }
        Button(onClick = {
            scope.launch { repoList = service.requestRepoList(user) }
        }) {
            Text("获取 $user 的所有Repo")
        }
        LazyColumn {
            items(repoList.size) { index ->
                val repo = repoList[index]
                Text("${repo.name}: ${repo.size}")
                HorizontalDivider()
            }
        }
    }
}