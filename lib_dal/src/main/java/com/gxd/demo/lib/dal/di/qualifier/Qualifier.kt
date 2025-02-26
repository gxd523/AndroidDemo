package com.gxd.demo.lib.dal.di.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GithubClientId

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GithubSecret