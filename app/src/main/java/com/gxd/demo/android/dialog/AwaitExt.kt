package com.gxd.demo.android.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun Context.showConfirmDialog(
    title: String, message: String, positiveText: String = "确认", negativeText: String = "取消",
): Boolean = suspendCancellableCoroutine { continuation ->
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText) { dialog, which ->
            dialog.dismiss()
            if (continuation.isActive) continuation.resume(true)
        }
        .setNegativeButton(negativeText) { dialog, which ->
            dialog.dismiss()
            if (continuation.isActive) continuation.resume(false)
        }
        .setOnDismissListener { if (continuation.isActive) continuation.resume(false) }
        .create()
        .also { dialog -> continuation.invokeOnCancellation { dialog.dismiss() } }
        .show()
}