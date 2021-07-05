package com.demo.compose.case

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

/**
 * 根据可用空间显示不同的内容
 */
@Preview
@Composable
fun BoxWithConstraintsCase() {
    Column {
        Column {
            MyBoxWithConstraints()
        }

        Text("Here we set the size to 150.dp", Modifier.padding(top = 20.dp))
        Column(Modifier.size(150.dp)) { MyBoxWithConstraints() }
    }
}

@Composable
private fun MyBoxWithConstraints() {
    BoxWithConstraints {
        val boxWithConstraintsScope = this
        // You can use this scope to get the minWidth, maxWidth, minHeight, maxHeight in dp and constraints

        Column {
            if (boxWithConstraintsScope.maxHeight >= 200.dp) {
                Text(
                    "This is only visible when the maxHeight is >= 200.dp",
                    style = TextStyle(fontSize = 20.sp)
                )
            }
            Text("maxWidth: ${boxWithConstraintsScope.maxWidth}")
            Text("minWidth: ${boxWithConstraintsScope.minWidth}")
            Text("maxHeight: ${boxWithConstraintsScope.maxHeight}")
            Text("minHeight: ${boxWithConstraintsScope.minHeight}")
        }
    }
}

@Preview
@Composable
fun SpacerDemo() {
    Column {
        Text("Hello")
        Spacer(modifier = Modifier.size(30.dp))
        Text("World")
    }
}

@Preview
@Composable
fun ConstraintLayoutCase() {
    ConstraintLayout {
        val (text1, text2, text3) = createRefs()

        Box(
            Modifier
                .background(Color.Red)
                .size(50.dp)
                .constrainAs(text1) {
                    start.linkTo(text2.end)
                })
        Box(
            Modifier
                .background(Color.Green)
                .size(50.dp)
                .constrainAs(text2) {
                    top.linkTo(text1.bottom)
                })
        Box(
            Modifier
                .background(Color.Blue)
                .size(50.dp)
                .constrainAs(text3) {
                    start.linkTo(text2.end)
                    top.linkTo(text2.bottom)
                })
    }
}