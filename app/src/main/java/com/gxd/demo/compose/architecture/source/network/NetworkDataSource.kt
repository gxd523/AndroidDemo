package com.gxd.demo.compose.architecture.source.network

import com.gxd.demo.compose.data.NetworkRepo

interface NetworkDataSource {
    suspend fun getRepositoryList(username: String): List<NetworkRepo>
}