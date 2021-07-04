package com.demo.compose.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp

data class User(val name: String, val age: Int)

@Preview(name = "Preview Test")
@Composable
fun PreviewCase(user: User = User("Spike", 21)) {
    Column(
        Modifier
            .background(Color.White)
            .padding(5.dp)
    ) {
        Text("姓名: ${user.name}")
        Text("年龄: ${user.age}")
    }
}

@Preview
@Composable
fun PreViewParameterCase(@PreviewParameter(UserProvider::class) user: User) {
    Column(
        Modifier
            .background(Color.White)
            .padding(5.dp)
    ) {
        Text("姓名: ${user.name}")
        Text("年龄: ${user.age}")
    }
}