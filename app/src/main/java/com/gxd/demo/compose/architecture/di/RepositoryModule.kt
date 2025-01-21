package com.gxd.demo.compose.architecture.di

import com.gxd.demo.compose.architecture.repository.GithubRepository
import com.gxd.demo.compose.architecture.repository.GithubRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindGithubRepository(dataSource: GithubRepositoryImpl): GithubRepository
}