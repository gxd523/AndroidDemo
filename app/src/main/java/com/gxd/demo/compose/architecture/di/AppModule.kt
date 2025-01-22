package com.gxd.demo.compose.architecture.di

import com.gxd.demo.compose.BuildConfig
import com.gxd.demo.lib.dal.di.qualifier.BaseUrl
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
}