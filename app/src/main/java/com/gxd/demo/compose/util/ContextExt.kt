package com.gxd.demo.compose.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import com.github.gzuliyujiang.oaid.DeviceID
import com.github.gzuliyujiang.oaid.DeviceIdentifier
import com.github.gzuliyujiang.oaid.IGetter
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.Exception
import java.security.MessageDigest
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