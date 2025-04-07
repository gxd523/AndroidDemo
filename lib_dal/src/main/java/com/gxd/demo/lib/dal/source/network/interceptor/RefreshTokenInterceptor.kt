package com.gxd.demo.lib.dal.source.network.interceptor

/**
 *
 */
//@RequiresApi(Build.VERSION_CODES.O)
//class RefreshTokenInterceptor : Interceptor {
//    @Inject
//    lateinit var cacheDataSource: CacheDataSource
//    private val formatter by lazy { DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss") }
//    private val lock by lazy { Mutex() }
//
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val oAuthResult = cacheDataSource.oauthResult
//        val requestBuilder = chain.request().newBuilder()
//        if (oAuthResult.expireTime.isNullOrEmpty()) return chain.proceed(requestBuilder.build())
//
//
//        val expireTime = oAuthResult.expireTime.substring(0, 19).let { LocalDateTime.parse(it, formatter) }
//        val currentTime = LocalDateTime.now()
//
//        // 提前5分钟刷新Token
//        val refreshTime = expireTime.minus(5, ChronoUnit.MINUTES)
//        if (expireTime == null || !currentTime.isAfter(refreshTime)) return chain.proceed(requestBuilder.build())
//
//        synchronized(this) {
//            if (currentTime.isAfter(refreshTime)) { // 双重锁检查
//                runBlocking {
//                    val newToken = oAuthResult.refreshToken?.let(::refreshAuthToken)
//                    newToken?.let { token -> CacheUtil.setToken(token) }
//                }
//            }
//        }
//
//        // 添加Token到请求头
//        val builder = requestBuilder.apply {
//            addHeader("token", CacheUtil.getToken().token ?: "")
//        }
//        return chain.proceed(builder.build())
//    }
//
//    private suspend fun refreshAuthToken(refreshToken: String): OAuthResult? = withContext(Dispatchers.IO) {
//        try {
//            val response = OAuthResult()
////            if (response.code == 200 && response.data != null) {
////                response.data
////            } else { // refreshToken过期等失败情况
////                handleTokenRefreshFailure()
////                null
////            }
//            response
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    private fun handleTokenRefreshFailure() {
//        // 处理失败情况，跳转到登录界面
//    }
//}
