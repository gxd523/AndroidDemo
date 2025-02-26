package com.gxd.demo.lib.dal.di

import com.gxd.demo.lib.dal.source.cache.CacheDataSource
import com.gxd.demo.lib.dal.source.network.NetworkDataSource
import com.gxd.demo.lib.dal.source.network.NetworkDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {
    @Singleton
    @Provides
    fun provideNetworkDataSource(dataSource: NetworkDataSourceImpl): NetworkDataSource = dataSource

    @Singleton
    @Provides
    fun provideCacheDataSource() = CacheDataSource()
}