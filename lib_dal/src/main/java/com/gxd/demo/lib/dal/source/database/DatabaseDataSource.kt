package com.gxd.demo.lib.dal.source.database

import com.gxd.demo.lib.dal.source.database.dao.RepoDao
import com.gxd.demo.lib.dal.source.database.dao.UserDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseDataSource @Inject constructor(val repoDao: RepoDao, val userDao: UserDao) : RepoDao by repoDao, UserDao by userDao