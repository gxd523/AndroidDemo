package com.demo.compose.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun CardItemView(
    modifier: Modifier = Modifier,
    elevation: Dp = 10.dp,
    title: String = "Jetpack Compose Playground",
    subTitle: String = "Card"
) {
    Card(modifier, elevation = elevation) {
        Column(Modifier.padding(15.dp)) {
            Text(
                buildAnnotatedString {
                    append("welcome to ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.W900,
                            color = Color(0xFF4552B8),
                            fontSize = 20.sp
                        )
                    ) {
                        append(title)
                    }
                }
            )
            Text(
                buildAnnotatedString {
                    append("Now you are in the ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)) {
                        append(subTitle)
                    }
                    append(" section")
                }
            )
        }
    }
}