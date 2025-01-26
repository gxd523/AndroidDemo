package com.gxd.demo.compose.storage

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
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
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


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
        while (cursor.moveToNext()) {
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

fun InputStream.writeFile(
    contentResolver: ContentResolver, fileName: String = "gxd.txt", path: String = "WeiXin", mimeType: String = "txt/plain",
) {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName) // 文件名
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType) // 文件类型
        put(MediaStore.Downloads.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/$path")
    }

    val insertUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues) ?: return
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }
    contentResolver.openOutputStream(insertUri)?.use { outputStream ->
        this.use { inputStream -> inputStream.copyTo(outputStream) }
    }
}

fun Context.readFile(fileName: String = "gxd.txt", path: String = "WeiXin"): String? {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "text/plain"

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
//        putExtra(DocumentsContract.EXTRA_INITIAL_URI, MediaStore.Downloads.getContentUri(MediaStore.Downloads.DOWNLOAD_URI))
    }

    (this as? StorageActivity)?.createFileLauncher?.launch(intent)

    return null

    // TODO: 直接通过「ContentResolver」只能读取本次安装后写入的文件，卸载重装后就无法读取之前写入的文件了。。。

    val projection = arrayOf(
        MediaStore.Downloads._ID,
        MediaStore.Downloads.DISPLAY_NAME,
        MediaStore.Downloads.SIZE,
        MediaStore.Downloads.MIME_TYPE,
        MediaStore.Downloads.DATA,
    )

    val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
//        MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
//        MediaStore.Downloads.EXTERNAL_CONTENT_URI
    } else {
        MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    }

    val selection = "${MediaStore.DownloadColumns.MIME_TYPE} = ?"
    val selectionArgs = arrayOf("text/plain")

    contentResolver.query(
        collection, projection, null, null, null
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID))
            val displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME))
            val size = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Downloads.SIZE))
            val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Downloads.MIME_TYPE))
            val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Downloads.DATA))
            Log.d("ggg", "$displayName, $size, $mimeType, $path")
            val fileUri = Uri.withAppendedPath(collection, id.toString())
            return contentResolver.readTextFromUri(fileUri)
        } else {
            Log.d("ggg", "No txt file found.")
        }
    }
    return null
}


@Throws(IOException::class)
fun ContentResolver.readTextFromUri(uri: Uri): String {
    val stringBuilder = StringBuilder()
    openInputStream(uri)?.use { inputStream ->
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            var line: String? = reader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = reader.readLine()
            }
        }
    }
    return stringBuilder.toString()
}