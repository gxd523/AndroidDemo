package com.demo.compose.case

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun CheckBoxCase() {
    val checkedState = remember { mutableStateOf(true) }
    Checkbox(
        checked = checkedState.value,
        onCheckedChange = { checkedState.value = it }
    )
}

@Preview
@Composable
fun FloatingActionButtonCase() {
    ExtendedFloatingActionButton(
        icon = { Icon(Icons.Filled.Favorite, "") },
        text = { Text("FloatingActionButton") },
        onClick = { /*do something*/ },
        elevation = FloatingActionButtonDefaults.elevation(8.dp)
    )
}

@Preview
@Composable
fun RadioButtonCase() {
    val radioOptionList = listOf("A", "B", "C")
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(radioOptionList[1])
    }
    Column {
        radioOptionList.forEach { text ->
            Row(Modifier
                .fillMaxWidth()
                .selectable(
                    selected = (text == selectedOption),
                    onClick = {
                        onOptionSelected
                        (text)
                    }
                )
                .padding(horizontal = 16.dp)
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text) }
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.body1.merge(),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun SliderCase() {
    var sliderPosition by remember { mutableStateOf(0f) }
    Text(text = sliderPosition.toString())
    Slider(value = sliderPosition, onValueChange = { sliderPosition = it })
}

@Preview
@Composable
fun SnackbarCase() {
    Column {
        val snackbarVisibleState = remember { mutableStateOf(false) }

        Button(onClick = { snackbarVisibleState.value = !snackbarVisibleState.value }) {
            if (snackbarVisibleState.value) {
                Text("Hide Snackbar")
            } else {
                Text("Show Snackbar")
            }
        }
        if (snackbarVisibleState.value) {
            Snackbar(
                action = {
                    Button(onClick = {}) {
                        Text("MyAction")
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) { Text(text = "This is a snackbar!") }
        }
    }
}

@Preview
@Composable
fun SwitchCase() {
    val checkedState = remember { mutableStateOf(true) }
    Switch(
        checked = checkedState.value,
        onCheckedChange = { checkedState.value = it }
    )
}

@Preview
@Composable
fun TextFieldCase() {
    Column(Modifier.padding(16.dp)) {
        val textState = remember { mutableStateOf(TextFieldValue()) }
        TextField(
            value = textState.value,
            onValueChange = { textState.value = it }
        )
        Text("The textfield has this text: " + textState.value.text)
    }
}

@Preview
@Composable
fun TopAppBarCase() {
    Column {
        TopAppBar(
            elevation = 4.dp,
            title = {
                Text("I'm a TopAppBar")
            },
            backgroundColor = MaterialTheme.colors.primarySurface,
            navigationIcon = {
                IconButton(onClick = {/* Do Something*/ }) {
                    Icon(Icons.Filled.ArrowBack, null)
                }
            }, actions = {
                IconButton(onClick = {/* Do Something*/ }) {
                    Icon(Icons.Filled.Share, null)
                }
                IconButton(onClick = {/* Do Something*/ }) {
                    Icon(Icons.Filled.Settings, null)
                }
            })

        Text("Hello World")

    }
}