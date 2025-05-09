package com.gxd.demo.android.storage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemGesturesPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun StorageScreen(modifier: Modifier = Modifier, viewModel: StorageViewModel = viewModel()) = Column(
    modifier.fillMaxSize().systemGesturesPadding(),
    Arrangement.Center,
    Alignment.CenterHorizontally
) {
    val context = LocalContext.current
    val imageBitmapList by remember {
        derivedStateOf {
            viewModel.uriStateList.mapNotNull { it.toImageBitmap(context.contentResolver) }
        }
    }
    LazyVerticalGrid(GridCells.Fixed(3), contentPadding = PaddingValues(10.dp)) {
        items(imageBitmapList.size) { Image(imageBitmapList[it], "") }
    }
    val readTxt = viewModel.readTxt.value
    if (!readTxt.isNullOrEmpty()) Text(readTxt)
    ButtonList()
}

@Composable
private fun ButtonList(modifier: Modifier = Modifier, viewModel: StorageViewModel = viewModel()) = Column(modifier) {
    val context = LocalContext.current
    Button(onClick = { viewModel.usePhotoPicker(context) }) { Text("照片选择器") }
    Button(onClick = { viewModel.readAlbumPhoto(context) }) { Text("读取相册照片") }
    Button(onClick = { viewModel.addPhotoToAlbum("avatar.jpg", context) }) { Text("添加照片到相册") }
    Button(onClick = { viewModel.removePhotoFromAlbum(context) }) { Text("删除相册中的照片") }
    Button(onClick = { viewModel.shareImage(context) }) { Text("分享图片") }
    Button(onClick = { viewModel.createFile(context) }) { Text("创建文件") }
    Button(onClick = { viewModel.writeFile(context) }) { Text("写入文件") }
    Button(onClick = { viewModel.readFile(context) }) { Text("读取文件") }
}