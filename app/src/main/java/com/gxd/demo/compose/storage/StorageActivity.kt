package com.gxd.demo.compose.storage

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels

/**
 * 共享存储示例
 * https://developer.android.com/training/data-storage/shared?hl=zh-cn
 */
class StorageActivity : ComponentActivity() {
    private val viewModel: StorageViewModel by viewModels()
    private val pickMediaContract = ActivityResultContracts.PickMultipleVisualMedia(10)
    internal val pickMediaLauncher = registerForActivityResult(
        pickMediaContract
    ) { uriList ->
        viewModel.updateUriList(uriList)
    }
    private val permissionContract = ActivityResultContracts.RequestMultiplePermissions()
    internal val permissionLauncher = registerForActivityResult(
        permissionContract
    ) {
        if (it.keys.contains(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)) {
            this@StorageActivity.contentResolver.readAlbumPhoto().let(viewModel::updateUriList)
        }
    }
    internal lateinit var deletePhotoSenderLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentSenderContract = ActivityResultContracts.StartIntentSenderForResult()
        deletePhotoSenderLauncher = registerForActivityResult(intentSenderContract) { result ->
            if (result.resultCode != RESULT_OK) return@registerForActivityResult
            Toast.makeText(this, "图片删除成功", Toast.LENGTH_LONG).show()
        }
        setContent {
            StorageScreen()
        }
    }
}
