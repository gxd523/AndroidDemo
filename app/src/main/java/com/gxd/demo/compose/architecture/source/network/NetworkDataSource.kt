package com.gxd.demo.compose.architecture.source.network

interface NetworkDataSource {
    suspend fun getRepositoryList(username: String): List<NetworkRepo>
}