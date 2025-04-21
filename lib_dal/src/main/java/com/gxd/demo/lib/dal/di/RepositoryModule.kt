package com.gxd.demo.lib.dal.di

import com.gxd.demo.lib.dal.repository.GithubRepository
import com.gxd.demo.lib.dal.repository.GithubRepositoryImpl
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
    abstract fun bindGithubRepository(repository: GithubRepositoryImpl): GithubRepository
}