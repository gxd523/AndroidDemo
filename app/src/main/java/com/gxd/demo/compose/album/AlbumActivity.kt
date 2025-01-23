package com.gxd.demo.compose.album

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemGesturesPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.core.content.ContextCompat
import com.gxd.demo.compose.util.checkPermissions
import java.io.InputStream

class AlbumActivity : ComponentActivity() {
    private val permissionContract = ActivityResultContracts.RequestMultiplePermissions()
    private val permissionLauncher = registerForActivityResult(permissionContract) {
        if (!checkPermissionResult()) {
            Toast.makeText(this, "请给权限", Toast.LENGTH_LONG).show()
        }
        val bitmapList = this.contentResolver.readAlbumPhoto()
        if (bitmapList.isNotEmpty()) {
            readAlbumPhoto.clear()
            readAlbumPhoto.addAll(bitmapList)
        }
    }
    private var readAlbumPhoto = mutableStateListOf<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scrollState = rememberScrollState()
            Column(
                Modifier.fillMaxSize().verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                readAlbumPhoto.map { it.asImageBitmap() }.forEach { imageBitmap ->
                    Image(imageBitmap, "")
                }
                val context = LocalContext.current
                Button(onClick = {
                    context.saveAssetsPictureToAlbum("avatar.jpg")?.also { uri ->
                        Toast.makeText(context, uri.toString(), Toast.LENGTH_LONG).show()
                    }
                }, Modifier.systemGesturesPadding()) {
                    Text("存储照片到相册")
                }
                Button(onClick = {
                    val bitmapList = this@AlbumActivity.readAlbumPhoto()
                    if (bitmapList.isNotEmpty()) {
                        readAlbumPhoto.clear()
                        readAlbumPhoto.addAll(bitmapList)
                    }
                }, Modifier.systemGesturesPadding()) {
                    Text("读取相册中的照片")
                }
            }
        }
    }

    private fun Activity.readAlbumPhoto(): List<Bitmap> {
        val permissions = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> arrayOf(// Android 14及以上
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            )

            Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU -> arrayOf(// Android 13
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_IMAGES
            )

            else -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)// Android 12 API 32及以下

        }
        if (permissions.any {
                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }) {
            permissionLauncher.launch(permissions)
        } else {
            return this.contentResolver.readAlbumPhoto()
        }
        return emptyList()
    }
}

private fun ContentResolver.readAlbumPhoto(): List<Bitmap> {
    val projection = arrayOf(
        MediaStore.Images.Media._ID, // 图片的唯一标识符
        MediaStore.Images.Media.DISPLAY_NAME, // 图片名称
        MediaStore.Images.Media.DATE_ADDED, // 图片添加日期
        MediaStore.Images.Media.DATA // 图片的完整路径
    )

    query(// 查询相册中的图片
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 查询的 URI
        projection, // 查询的列
        null, // WHERE 子句
        null, // WHERE 子句中的参数
        MediaStore.Images.Media.DATE_ADDED + " DESC" // 排序方式
    )?.use { cursor ->
        val bitmapList = mutableListOf<Bitmap>()
        while (cursor.moveToNext()) {
            val imageId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
            val imageName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
            val imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))

            Log.d("ggg", "Image ID: $imageId, Name: $imageName, Path: $imagePath")

            // 获取图片的 URI（用于打开输入流）
            val imageUri: Uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId.toString())

            // 打开图片输入流（图片的 Bitmap）
            val inputStream = openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            bitmapList += bitmap
        }
        return bitmapList
    }
    return emptyList()
}

private fun Context.saveAssetsPictureToAlbum(fileName: String): Uri? = assets.open(fileName).saveToAlbum(this, fileName)

private fun InputStream.saveToAlbum(context: Context, fileName: String): Uri? {
    val contentResolver = context.contentResolver
    val insertImageUri = contentResolver.insertMediaImage(fileName) ?: return null

    contentResolver.openOutputStream(insertImageUri)?.use { outputStream ->
        this.use { inputStream ->
            inputStream.copyTo(outputStream)
            val imageValues = ContentValues().apply {
                // 「Android Q」添加了「IS_PENDING」状态，为「0」时其他应用才可见
                put(MediaStore.Images.Media.IS_PENDING, 0)
            }
            contentResolver.update(insertImageUri, imageValues, null, null)
        }
    }
    return insertImageUri
}

private fun ContentResolver.insertMediaImage(fileName: String): Uri? {
    val imageValues = ContentValues().apply {// 图片信息
        fileName.mimeType()?.also { mimeType ->
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        }

        val date = System.currentTimeMillis() / 1000
        put(MediaStore.Images.Media.DATE_ADDED, date)
        put(MediaStore.Images.Media.DATE_MODIFIED, date)
    }

    imageValues.apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        // 图片上传过程中防止被用户访问
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }

    val url = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }
    return insert(url, imageValues).also { if (it == null) Log.w("ggg", "插入图片返回uri为null") }
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

private fun Context.checkPermissionResult(): Boolean = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {// Android 14及以上部分照片和视频访问权限
        checkPermissions(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) ||
                checkPermissions(Manifest.permission.READ_MEDIA_IMAGES) ||
                checkPermissions(Manifest.permission.READ_MEDIA_VIDEO) ||
                checkPermissions(Manifest.permission.READ_MEDIA_AUDIO)
    }

    Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU -> {// Android 13及以上完整照片和视频访问权限
        checkPermissions(Manifest.permission.READ_MEDIA_IMAGES) ||
                checkPermissions(Manifest.permission.READ_MEDIA_VIDEO) ||
                checkPermissions(Manifest.permission.READ_MEDIA_AUDIO)
    }


    else -> checkPermissions(// Android 12及以下完整本地读写访问权限
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
}
