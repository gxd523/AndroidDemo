package com.gxd.demo.android.hilt

import android.content.Context
import com.gxd.demo.lib.dal.source.network.service.GithubApiService
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

object HiltEntryPoint {
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface MyHiltEntryPoint {
        fun githubService(): GithubApiService
    }

    fun getGithubService(application: Context): GithubApiService = EntryPointAccessors.fromApplication(
        application, MyHiltEntryPoint::class.java
    ).githubService()
}