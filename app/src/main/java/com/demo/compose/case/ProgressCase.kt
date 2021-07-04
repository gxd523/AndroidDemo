package com.demo.compose.case

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun CircularProgressCase() {
    CircularProgressIndicator()
}

@Preview
@Composable
fun LinearProgressCase() {
    LinearProgressIndicator()
}

@Preview
@Composable
fun CircularProgressIndicatorCase() {
    var progress by remember { mutableStateOf(0.1f) }
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(30.dp))
        CircularProgressIndicator(progress = animatedProgress)
        Spacer(Modifier.height(25.dp))
        Row {
            OutlinedButton(
                onClick = { if (progress < 1f) progress += 0.1f },
                Modifier.padding(5.dp)
            ) {
                Text("Increase")
            }

            OutlinedButton(
                onClick = { if (progress > 0f) progress -= 0.1f },
                Modifier.padding(5.dp)
            ) {
                Text("Decrease")
            }
        }
    }
}

@Preview
@Composable
fun LinearProgressIndicatorSample() {
    var progress by remember { mutableStateOf(0.1f) }
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(30.dp))
        LinearProgressIndicator(progress = animatedProgress)
        Spacer(Modifier.height(25.dp))
        Row {
            OutlinedButton(
                onClick = { if (progress < 1f) progress += 0.1f },
                Modifier.padding(5.dp)
            ) {
                Text("Increase")
            }

            OutlinedButton(
                onClick = { if (progress > 0f) progress -= 0.1f },
                Modifier.padding(5.dp)
            ) {
                Text("Decrease")
            }
        }
    }
}