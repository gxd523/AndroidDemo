package com.gxd.demo.lib.dal.di

import com.gxd.demo.lib.dal.source.network.NetworkDataSource
import com.gxd.demo.lib.dal.source.network.NetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(dataSource: NetworkDataSourceImpl): NetworkDataSource
}