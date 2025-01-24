package com.gxd.demo.compose.util

import androidx.compose.runtime.snapshots.SnapshotStateList

fun <T> SnapshotStateList<T>.clearAndAddAll(newItems: List<T>) {
    this.clear()
    this.addAll(newItems)
}