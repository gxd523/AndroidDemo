package com.gxd.demo.lib.dal.source.network

import com.gxd.demo.lib.dal.source.network.model.NetworkRepo

interface NetworkDataSource {
    suspend fun getRepositoryList(username: String): List<NetworkRepo>
}