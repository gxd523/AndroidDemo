package com.gxd.demo.android.util

import androidx.compose.runtime.snapshots.SnapshotStateList

fun <T> SnapshotStateList<T>.clearAndAddAll(newItems: List<T>) {
    this.clear()
    this.addAll(newItems)
}