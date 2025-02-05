package com.gxd.demo.compose.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import java.security.MessageDigest

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

fun Context.getSha1Signature(): String? {
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