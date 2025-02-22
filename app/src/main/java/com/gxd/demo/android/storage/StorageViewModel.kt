package com.gxd.demo.android.storage

import android.Manifest
import android.app.RecoverableSecurityException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gxd.demo.android.util.clearAndAddAll

class StorageViewModel : ViewModel() {
    var uriStateList = mutableStateListOf<Uri>()
    var readTxt = mutableStateOf<String?>(null)

    fun updateUriList(uriList: List<Uri>) {
        uriStateList.clearAndAddAll(uriList)
    }

    fun addPhotoToAlbum(assetFileName: String, context: Context) {
        context.assets.open(assetFileName).insertPhotoToAlbum(
            context.contentResolver, assetFileName
        )?.let(::listOf)?.let(::updateUriList)
    }

    fun readAlbumPhoto(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            (context as? StorageActivity)?.permissionLauncher?.let {
                arrayOf(
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                ).let(it::launch)
            }
        } else {
            context.contentResolver.readAlbumPhoto().let(::updateUriList)
        }
    }

    fun usePhotoPicker(context: Context) {
        (context as? StorageActivity)?.pickMediaLauncher?.let {
            PickVisualMediaRequest(PickVisualMedia.ImageAndVideo).let(it::launch)
        }
    }

    fun removePhotoFromAlbum(context: Context) {
        if (uriStateList.isEmpty()) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intentSender = MediaStore.createDeleteRequest(context.contentResolver, uriStateList).intentSender
            val senderRequest = IntentSenderRequest.Builder(intentSender).build()
            (context as? StorageActivity)?.deletePhotoSenderLauncher?.launch(senderRequest)
        } else {
            val selection = null
            val selectionArgs = null
            uriStateList.forEach {
                context.contentResolver.delete(it, selection, selectionArgs)
            }
        }
    }

    fun createFile(
        context: Context,
        fileName: String = "aaa.pdf",
        pickerInitialUri: Uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
    ) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, fileName)
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }
        (context as? StorageActivity)?.createFileLauncher?.launch(intent)
    }

    fun writeFile(context: Context) {
        val fileName = "gxd.txt"
        context.assets.open(fileName).writeFile(context.contentResolver, fileName)
    }

    fun readFile(context: Context) {
        readTxt.value = context.readFile()
    }

    /**
     * 修改文件需要申请写权限
     */
    fun requestWritePermission(context: Context) {
        if (uriStateList.isEmpty()) return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DESCRIPTION, "Updated description")
            }
            uriStateList.forEach { uri ->
                context.contentResolver.update(uri, values, null, null)
            }
        } else {
            try {
                val intentSender = MediaStore.createWriteRequest(context.contentResolver, uriStateList).intentSender
                val senderRequest = IntentSenderRequest.Builder(intentSender).build()
                (context as? StorageActivity)?.deletePhotoSenderLauncher?.launch(senderRequest)
            } catch (e: RecoverableSecurityException) {// Handle RecoverableSecurityException for API 29
                val intentSender = e.userAction.actionIntent.intentSender
                val senderRequest = IntentSenderRequest.Builder(intentSender).build()
                (context as? StorageActivity)?.deletePhotoSenderLauncher?.launch(senderRequest)
            }
        }
    }

    fun shareImage(context: Context) {
        val firstImageUri = uriStateList.firstOrNull() ?: return
        val launcher = (context as? StorageActivity)?.createFileLauncher ?: return
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, firstImageUri)
            setType("image/*")
        }.let(launcher::launch)
    }
}
