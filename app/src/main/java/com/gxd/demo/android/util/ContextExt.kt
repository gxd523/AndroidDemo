package com.gxd.demo.android.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import com.github.gzuliyujiang.oaid.DeviceID
import com.github.gzuliyujiang.oaid.DeviceIdentifier
import com.github.gzuliyujiang.oaid.IGetter
import com.gxd.demo.android.MyApplication
import com.gxd.demo.android.R
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.security.MessageDigest
import java.util.Locale
import kotlin.coroutines.resume

fun Context.getPhoneNumber(permissionLauncher: ActivityResultLauncher<Array<String>>): String? {
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Manifest.permission.READ_PHONE_NUMBERS
    } else {
        Manifest.permission.READ_PHONE_STATE
    }
    if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
        permissionLauncher.launch(arrayOf(permission))
        return null
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val subscriptionManager = getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        return subscriptionManager.getPhoneNumber(SubscriptionManager.DEFAULT_SUBSCRIPTION_ID)
    } else {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        @SuppressLint("HardwareIds")
        return telephonyManager.line1Number
    }
}

fun Context.getAppSignature(): String? {
    val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        PackageManager.GET_SIGNING_CERTIFICATES
    } else {
        PackageManager.GET_SIGNATURES
    }
    val packageInfo = packageManager.getPackageInfo(packageName, flags)
    // 获取签名数组
    val signature = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo.signingInfo?.apkContentsSigners
    } else {
        packageInfo.signatures
    }?.getOrNull(0) ?: return null
    // 获取签名的 SHA-1 摘要
    val messageDigest = MessageDigest.getInstance("SHA-1")
    val digestByteArray = messageDigest.digest(signature.toByteArray())

    return digestByteArray.joinToString("") { String.format("%02x", it).uppercase() }
}

suspend fun Context.getDeviceID(): String = if (DeviceID.supportedOAID(this)) {// 是否支持OAID/AAID
    val oaid = DeviceIdentifier.getOAID(this)
    if (oaid.checkXxxID()) {
        oaid
    } else {
        suspendCancellableCoroutine<String> { continuation ->
            DeviceID.getOAID(this, object : IGetter {
                override fun onOAIDGetComplete(oaid: String?) {
                    val result = if (!oaid.isNullOrEmpty() && oaid.checkXxxID()) oaid else getAndroidIdOrGUID()
                    continuation.resume(result)
                }

                override fun onOAIDGetError(error: Exception?) {
                    getAndroidIdOrGUID().let(continuation::resume)
                }
            })
        }
    }
} else {
    getAndroidIdOrGUID()
}

private fun Context.getAndroidIdOrGUID(): String {
    val androidID = DeviceIdentifier.getAndroidID(this)
    return if (androidID.checkXxxID() && androidID.lowercase() != "9774d56d682e549c") androidID else DeviceID.getGUID(this)
}

private fun String?.checkXxxID(): Boolean = !isNullOrEmpty() && !all { it == '0' }

/**
 * 监听网络是否可用
 */
fun Context.networkConnectListenerFlow() = callbackFlow {
    Log.d("ggg", ".networkConnectListenerFlow...${Thread.currentThread().name}")
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        /**
         * 当网络可用时被调用，相当于跟我们喊 “网络通啦”！
         */
        override fun onAvailable(network: Network) {
            connectivityManager?.getNetworkCapabilities(network)?.let { networkCapabilities ->
                if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    Log.d("ggg", ".onAvailable...")
                    trySend(true)
                }
            }
        }

        /**
         * 一旦网络丢失，比如用户切换到其他网络了，它就会出声提醒我们
         * todo 这里有问题，关闭wifi如果流量本来就是开着的也会回调这里
         */
        override fun onLost(network: Network) {
            trySend(false)
        }

        /**
         * 要是没有可用网络了，它就会来报信
         */
        override fun onUnavailable() {
            trySend(false)
        }

        /**
         * 当网络的功能有变化，像是网络能不能上网、是不是有效这类情况改变时，它就会通知我们
         */
        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED).let(::trySend)
        }
    }

    // 这是创建一个请求构建器，就像是搭个框架，用来设置我们想要的网络标准
    val networkRequest = NetworkRequest.Builder()
        // 给网络加上一个必须能上网的要求，这样找出来的网络肯定是能让应用联网的
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        // 给网络请求指定传输类型，像「Wi-Fi」、「以太网」或者「蜂窝网络」，这样就能找到符合这些「传输类型」要求的网络
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
    awaitClose { connectivityManager?.unregisterNetworkCallback(networkCallback) }
}

private fun NetworkCapabilities.getNetworkType(): NetworkType? = if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
    NetworkType.WIFI
} else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
    NetworkType.CELLULAR
} else if (hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
    NetworkType.ETHERNET
} else null


enum class NetworkType {
    WIFI, CELLULAR, ETHERNET
}

/**
 * 跳转Chrome浏览器
 */
fun Context.launchCustomChromeTab(uri: Uri, @ColorInt toolbarColor: Int) {
    val customTabBarColor = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(toolbarColor)
        .build()
    val customTabsIntent = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(customTabBarColor)
        .build()
    customTabsIntent.launchUrl(this, uri)
}

/**
 * 获得屏幕宽度
 */
fun Context.getScreenWidth(): Int {
    val outMetrics = DisplayMetrics()
    (getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.defaultDisplay?.getMetrics(outMetrics)
    return outMetrics.widthPixels
}

/**
 * 获得屏幕高度
 */
fun Context.getScreenHeight(): Int {
    val outMetrics = DisplayMetrics()
    (getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.defaultDisplay?.getMetrics(outMetrics)
    return outMetrics.heightPixels
}

fun Context.getMemoryInfo(): ActivityManager.MemoryInfo {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return ActivityManager.MemoryInfo().also { memoryInfo ->
        activityManager.getMemoryInfo(memoryInfo)
    }
}

fun showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    val applicationContext = MyApplication.instance
    val toast = Toast(applicationContext)
    with(toast) {
        view = TextView(applicationContext).apply {
            textSize = 34f
            setTextColor(0xffffffff.toInt())
            background = AppCompatResources.getDrawable(applicationContext, R.drawable.shape_toast_bg)
            setPadding(34.dip, 15.dip, 34.dip, 16.dip)
            gravity = Gravity.CENTER_HORIZONTAL
            this.text = text
        }
        this.duration = duration
        setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0, 350.dip)
    }
    toast.show()
}

private fun Context.checkAccessibility(): Boolean {
    val accessibilityEnabled = try {
        Settings.Secure.getInt(contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
    } catch (e: Exception) {
        null
    }

    if (accessibilityEnabled != 1) return false

    return try {
        val serviceName = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES) ?: return false
        serviceName.lowercase(Locale.getDefault()).contains(packageName.lowercase(Locale.getDefault()))
    } catch (e: Exception) {
        false
    }
}

suspend fun Context.openAccessibilitySetting() {
    if (checkAccessibility()) return
    delay(2_000)
    Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).let(::startActivity)
}