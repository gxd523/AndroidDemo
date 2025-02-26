package com.gxd.demo.android.architecture.di

import com.gxd.demo.android.BuildConfig
import com.gxd.demo.lib.dal.di.qualifier.BaseUrl
import com.gxd.demo.lib.dal.di.qualifier.GithubClientId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @BaseUrl
    @Singleton
    @Provides
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @GithubClientId
    @Singleton
    @Provides
    fun provideGithubClientId(): String = BuildConfig.GITHUB_CLIENT_ID
}