package com.gxd.demo.android.util

import java.io.ByteArrayOutputStream
import java.io.InputStream

fun InputStream.tosByteArray(): ByteArray = use { inputStream ->
    ByteArrayOutputStream().use { outputStream ->
        val buffer = ByteArray(1024)
        var readLength = 0
        while (inputStream.read(buffer).also { readLength = it } > 0) {
            outputStream.write(buffer, 0, readLength)
        }
        outputStream.toByteArray()
    }
}