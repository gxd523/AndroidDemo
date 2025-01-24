package com.gxd.demo.compose.storage

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import java.io.InputStream


fun Uri.toImageBitmap(contentResolver: ContentResolver): ImageBitmap? {
    contentResolver.openInputStream(this)?.use { inputStream ->
        return BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
    }
    return null
}

fun InputStream.insertPhotoToAlbum(contentResolver: ContentResolver, photoFileName: String): Uri? {
    val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    val contentValues = ContentValues().apply {
        photoFileName.mimeType()?.also { mimeType ->
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        }
        val date = System.currentTimeMillis() / 1000
        put(MediaStore.Images.Media.DATE_ADDED, date)
        put(MediaStore.Images.Media.DATE_MODIFIED, date)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        with(contentValues) {
            put(MediaStore.Images.Media.DISPLAY_NAME, photoFileName)
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Images.Media.IS_PENDING, 1)// 图片上传过程中防止被用户访问
        }
    }
    val insertPhotoUri = contentResolver.insert(collection, contentValues) ?: return null

    contentResolver.openOutputStream(insertPhotoUri)?.use { outputStream ->
        this.use { inputStream -> inputStream.copyTo(outputStream) }
    }
    contentValues.clear()
    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
    contentResolver.update(insertPhotoUri, contentValues, null, null)
    return insertPhotoUri
}

fun ContentResolver.readAlbumPhoto(): List<Uri> {
    val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    val projection = arrayOf(
        MediaStore.Images.Media._ID, // 图片的唯一标识符
        MediaStore.Images.Media.DISPLAY_NAME, // 图片名称
        MediaStore.Images.Media.DATE_ADDED, // 图片添加日期
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.DATA // 图片的完整路径
    )

    val selection = "${MediaStore.Images.Media.SIZE} >= ?"
    val selectionArgs = arrayOf("${1 * 1024}")
    val sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC"
    query(// 查询相册中的图片
        collection, // 查询的 URI
        projection, // 查询的列
        selection, // WHERE 子句
        selectionArgs, // WHERE 子句中的参数
        sortOrder // 排序方式
    )?.use { cursor ->
        val uriList = mutableListOf<Uri>()
        while (cursor.moveToNext().also { Log.d("ggg", "moveToNext = $it") }) {
            val imageId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
            val imageName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
            val imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
            val imageSize = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
            Log.d("ggg", "Image ID: $imageId, Name: $imageName, Path: $imagePath, Image: $imageSize")

            uriList += Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId.toString())
        }
        return uriList
    }
    return emptyList()
}

private fun String.mimeType(): String? = this.toLowerCase(Locale.current).let { fileName ->
    when {
        fileName.endsWith(".png") -> "image/png"
        fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") -> "image/jpeg"
        fileName.endsWith(".webp") -> "image/webp"
        fileName.endsWith(".gif") -> "image/gif"
        else -> null
    }
}