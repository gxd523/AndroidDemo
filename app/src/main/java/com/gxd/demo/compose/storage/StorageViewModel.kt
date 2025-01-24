package com.gxd.demo.compose.storage

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.gxd.demo.compose.util.clearAndAddAll

class StorageViewModel : ViewModel() {
    var uriStateList = mutableStateListOf<Uri>()

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
}
