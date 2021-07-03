package com.demo.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview

class TouchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SimpleCase()
        }
    }

    @Preview
    @Composable
    fun SimpleCase() {
        Modifier.pointerInput(Unit) {
            forEachGesture {
                awaitPointerEventScope {
                    val event = awaitPointerEvent()
                    event.changes.forEach { change ->
                        change.position
                        change.consumed
                    }
                }
            }
        }
//        Modifier.draggable()
//        Modifier.swipeable()
//        Modifier.transformable()
//        Modifier.scrollable()
//        Modifier.verticalScroll()
    }
}