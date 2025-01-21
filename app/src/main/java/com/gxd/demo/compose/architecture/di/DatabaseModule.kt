package com.gxd.demo.compose.architecture.di

import android.content.Context
import androidx.room.Room
import com.gxd.demo.compose.architecture.source.database.GithubDatabase
import com.gxd.demo.compose.architecture.source.database.table.RepoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): GithubDatabase = Room.databaseBuilder(
        context.applicationContext,
        GithubDatabase::class.java,
        "github.db"
    ).build()

    @Provides
    fun provideRepoDao(database: GithubDatabase): RepoDao = database.repoDao()
}