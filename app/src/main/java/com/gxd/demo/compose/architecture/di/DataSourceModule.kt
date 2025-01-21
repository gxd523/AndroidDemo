package com.gxd.demo.compose.architecture.di

import com.gxd.demo.compose.architecture.source.database.DatabaseDataSource
import com.gxd.demo.compose.architecture.source.database.DatabaseDataSourceImpl
import com.gxd.demo.compose.architecture.source.network.NetworkDataSource
import com.gxd.demo.compose.architecture.source.network.NetworkDataSourceImpl
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

    @Singleton
    @Binds
    abstract fun bindDatabaseDataSource(dataSource: DatabaseDataSourceImpl): DatabaseDataSource
}