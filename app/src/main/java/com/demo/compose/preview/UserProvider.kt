package com.demo.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class UserProvider : PreviewParameterProvider<User> {
    override val values: Sequence<User> = sequenceOf(User("Tom", 20), User("Jerry", 22))
}