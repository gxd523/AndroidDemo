package com.gxd.demo.lib.dal.di

import android.content.Context
import androidx.room.Room
import com.gxd.demo.lib.dal.source.database.GithubDatabase
import com.gxd.demo.lib.dal.source.database.dao.RepoDao
import com.gxd.demo.lib.dal.source.database.dao.UserDao
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

    @Provides
    fun provideUserDao(database: GithubDatabase): UserDao = database.userDao()
}