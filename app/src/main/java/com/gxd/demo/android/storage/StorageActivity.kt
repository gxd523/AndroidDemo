package com.gxd.demo.android.storage

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.gxd.demo.android.base.BaseActivity

/**
 * 共享存储示例
 * https://developer.android.com/training/data-storage/shared?hl=zh-cn
 */
class StorageActivity : BaseActivity() {
    private val viewModel: StorageViewModel by viewModels()
    internal lateinit var pickMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    internal lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    internal lateinit var deletePhotoSenderLauncher: ActivityResultLauncher<IntentSenderRequest>
    internal lateinit var createFileLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentSenderContract = ActivityResultContracts.StartIntentSenderForResult()
        deletePhotoSenderLauncher = registerForActivityResult(intentSenderContract) { result ->
            if (result.resultCode != RESULT_OK) return@registerForActivityResult
            Toast.makeText(this, "图片删除成功", Toast.LENGTH_LONG).show()
        }
        val pickMediaContract = ActivityResultContracts.PickMultipleVisualMedia(10)
        pickMediaLauncher = registerForActivityResult(pickMediaContract) { uriList ->
            viewModel.updateUriList(uriList)
        }
        val permissionContract = ActivityResultContracts.RequestMultiplePermissions()
        permissionLauncher = registerForActivityResult(permissionContract) {
            if (it.keys.contains(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)) {
                this@StorageActivity.contentResolver.readAlbumPhoto().let(viewModel::updateUriList)
            }
        }
        val createFileContract = ActivityResultContracts.StartActivityForResult()
        createFileLauncher = registerForActivityResult(createFileContract) { result ->
            viewModel.readTxt.value = result.data?.data?.let(this@StorageActivity.contentResolver::readTextFromUri)
        }

        setContent {
            StorageScreen()
        }
    }
}
