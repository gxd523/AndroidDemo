package com.gxd.demo.compose.album

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
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
import androidx.activity.result.ActivityResultLauncher
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
import java.io.File
import java.io.InputStream

class AlbumActivity : ComponentActivity() {
    private val permissionContract = ActivityResultContracts.RequestMultiplePermissions()
    private val permissionLauncher = registerForActivityResult(
        permissionContract
    ) {
        if (it.size == 1 && it.keys.first() == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
            if (!checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请给「WRITE_EXTERNAL_STORAGE」权限", Toast.LENGTH_LONG).show()
            } else {
                val fileName = "avatar.jpg"
                assets.open(fileName).saveToAlbum(this, fileName)?.also { uri ->
                    Log.d("ggg", "saveToAlbumUri->$uri")
                    Toast.makeText(this@AlbumActivity, uri.toString(), Toast.LENGTH_LONG).show()
                }
            }
        } else if (it.size > 1) {
            if (!checkExternalStoragePermission()) {
                Toast.makeText(this, "请给权限", Toast.LENGTH_LONG).show()
            } else {
                this.contentResolver.readAlbumPhoto().updateReadAlbumPhotoList()
            }
        }
    }

    private fun List<Uri>.updateReadAlbumPhotoList() {
        if (this.isNotEmpty()) {
            readAlbumPhotoList.clear()
            this.map { imageUri ->
                val inputStream = this@AlbumActivity.contentResolver.openInputStream(imageUri)
                BitmapFactory.decodeStream(inputStream)
            }.let(readAlbumPhotoList::addAll)
        }
    }

    private var readAlbumPhotoList = mutableStateListOf<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scrollState = rememberScrollState()
            Column(
                Modifier.fillMaxSize().verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                readAlbumPhotoList.map { it.asImageBitmap() }.forEach { imageBitmap ->
                    Image(imageBitmap, "")
                }
                val context = LocalContext.current
                Button(onClick = {
                    context.saveAssetsPictureToAlbum("avatar.jpg", permissionLauncher)?.also { uri ->
                        Log.d("ggg", "saveToAlbumUri->$uri")
                        Toast.makeText(context, uri.toString(), Toast.LENGTH_LONG).show()
                    }
                }, Modifier.systemGesturesPadding()) {
                    Text("存储照片到相册")
                }
                Button(onClick = {
                    context.checkPermissionAndReadAlbumPhoto(permissionLauncher).updateReadAlbumPhotoList()
                }, Modifier.systemGesturesPadding()) {
                    Text("读取相册中的照片")
                }
            }
        }
    }
}

private fun Context.checkPermissionAndReadAlbumPhoto(launcher: ActivityResultLauncher<Array<String>>): List<Uri> {
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
        launcher.launch(permissions)
    } else {
        return this.contentResolver.readAlbumPhoto()
    }
    return emptyList()
}

private fun ContentResolver.readAlbumPhoto(): List<Uri> {
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
        val uriList = mutableListOf<Uri>()
        while (cursor.moveToNext()) {
            val imageId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
            val imageName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
            val imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))

            Log.d("ggg", "Image ID: $imageId, Name: $imageName, Path: $imagePath")

            // 获取图片的 URI（用于打开输入流）
            uriList += Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId.toString())
        }
        return uriList
    }
    return emptyList()
}

private fun Context.saveAssetsPictureToAlbum(fileName: String, launcher: ActivityResultLauncher<Array<String>>): Uri? =
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
        assets.open(fileName).saveToAlbum(this, fileName)
    } else {
        if (checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            assets.open(fileName).saveToAlbum(this, fileName)
        } else {
            launcher.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            null
        }
    }

private fun InputStream.saveToAlbum(context: Context, fileName: String): Uri? {
    val contentResolver = context.contentResolver
    val insertImageUri = contentResolver.insertMediaImage(fileName) ?: return null

    contentResolver.openOutputStream(insertImageUri)?.use { outputStream ->
        this.use { inputStream ->
            inputStream.copyTo(outputStream)
            val imageValues = ContentValues()
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                contentResolver.update(insertImageUri, imageValues, null, null)
                // 通知媒体库更新
                val intent = Intent(@Suppress("DEPRECATION") Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, insertImageUri)
                context.sendBroadcast(intent)
            } else {
                // 「Android Q」添加了「IS_PENDING」状态，为「0」时其他应用才可见
                imageValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(insertImageUri, imageValues, null, null)
            }
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


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        imageValues.apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            // 图片上传过程中防止被用户访问
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
    } else {
        val saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        if (!saveDir.exists() && !saveDir.mkdirs()) {
            Log.e("ggg", "save: error: can't create Pictures directory")
            return null
        }
        // 文件路径查重，重复的话在文件名后拼接数字
        var imageFile = File(saveDir, fileName)
        val fileNameWithoutExtension = imageFile.nameWithoutExtension
        val fileExtension = imageFile.extension

        var queryUri = this.queryMediaImageUnderApi28(imageFile.absolutePath)
        var suffix = 1
        while (queryUri != null) {
            val newName = fileNameWithoutExtension + "(${suffix++})." + fileExtension
            imageFile = File(saveDir, newName)
            queryUri = this.queryMediaImageUnderApi28(imageFile.absolutePath)
        }

        imageValues.apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, imageFile.name)
            // 保存路径
            val imagePath = imageFile.absolutePath
            Log.v("ggg", "save file: $imagePath")
            put(MediaStore.Images.Media.DATA, imagePath)
        }
    }
    val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }
    return insert(collection, imageValues).also { if (it == null) Log.w("ggg", "插入图片返回uri为null") }
}

/**
 * Android Q以下版本，查询媒体库中当前路径是否存在
 * @return Uri 返回null时说明不存在，可以进行图片插入逻辑
 */
private fun ContentResolver.queryMediaImageUnderApi28(imagePath: String): Uri? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) return null

    val imageFile = File(imagePath)
    if (imageFile.canRead() && imageFile.exists()) {
        Log.v("ggg", "query: path: $imagePath exists")
        // 文件已存在，返回一个file://xxx的uri
        return Uri.fromFile(imageFile)
    }
    // 保存的位置
    val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    // 查询是否已经存在相同图片
    query(
        collection,
        arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA),
        "${MediaStore.Images.Media.DATA} == ?",
        arrayOf(imagePath),
        null
    )?.use { cursor ->
        while (cursor.moveToNext()) {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val id = cursor.getLong(idColumn)
            val existsUri = ContentUris.withAppendedId(collection, id)
            Log.v("ggg", "query: path: $imagePath exists uri: $existsUri")
            return existsUri
        }
    }
    return null
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

private fun Context.checkExternalStoragePermission(): Boolean = when {
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
