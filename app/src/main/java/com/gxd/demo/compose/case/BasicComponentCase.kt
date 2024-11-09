package com.gxd.demo.compose.case

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.skydoves.landscapist.coil.CoilImage
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
fun AlertDialogCase() {
    var toggle by remember { mutableStateOf(false) }
    Button(onClick = { toggle = !toggle }) { Text(if (toggle) "关闭" else "打开") }
    if (!toggle) return

    AlertDialog(
        onDismissRequest = { toggle = true },
        confirmButton = { TextButton(onClick = { toggle = false }) { Text("确认", fontWeight = FontWeight.W700) } },
        dismissButton = { TextButton(onClick = { toggle = false }) { Text("取消", fontWeight = FontWeight.W700) } },
        title = { Text("开启位置服务", fontWeight = FontWeight.W700, style = MaterialTheme.typography.headlineMedium) },
        text = { Text("这将意味着，我们会给您提供精准的位置服务，并且您将接受关于您订阅的位置信息", fontSize = 16.sp) }
    )
}

@Preview(showBackground = true)
@Composable
fun DialogCase() {
    var toggle by remember { mutableStateOf(false) }
    Button(onClick = { toggle = !toggle }) { Text(if (toggle) "关闭" else "打开") }
    if (!toggle) return

    Dialog(onDismissRequest = { toggle = false }) {
        Box(Modifier.size(300.dp).background(Color.White), contentAlignment = Alignment.Center) {
            Column {
                LinearProgressIndicator()
                Spacer(Modifier.height(5.dp))
                Text("加载中...")
            }
        }
    }
}

/**
 * MutableInteractionSource用法待研究，应该可以获取按键状态
 */
@Preview(showBackground = true)
@Composable
fun ButtonCase() {
    val interactionSource = remember { MutableInteractionSource() }
    val (text, icon, buttonColor) = when {
        interactionSource.collectIsPressedAsState().value -> Triple("Just Pressed", Icons.Filled.Favorite, Color.Black)
        else -> Triple("Just Button", Icons.Filled.FavoriteBorder, Color.Red)
    }
    Button(
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        modifier = Modifier.width(IntrinsicSize.Min).height(IntrinsicSize.Min),
        onClick = { /*TODO*/ }
    ) {
        Icon(icon, null, Modifier.size(ButtonDefaults.IconSize))
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun CardCase() {
    Card(
        Modifier.fillMaxWidth().padding(15.dp).clickable {},
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(3.dp)
    ) {
        Column(Modifier.padding(10.dp)) {
            Text(buildAnnotatedString {
                append("欢迎来到 ")
                withStyle(SpanStyle(fontWeight = FontWeight.W900, color = Color(0xFF4552B8))) { append("Jetpack Compose") }
            })
            Text(buildAnnotatedString {
                append("你现在观看的章节是 ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)) { append("Card") }
            })
        }
    }
}

@Preview
@Composable
fun FloatingActionButtonCase() {
    FloatingActionButton(shape = CircleShape, containerColor = Color.Black, onClick = { /*TODO*/ }) {
        Icon(Icons.Filled.Add, null, tint = Color.White)
    }
}

@Preview
@Composable
fun ExtendedFloatingActionButtonCase() {
    ExtendedFloatingActionButton(
        icon = { Icon(Icons.Filled.Add, null) },
        text = { Text("添加我喜欢的") },
        shape = RoundedCornerShape(30.dp),
        containerColor = Color.White,
        onClick = { /*TODO*/ })
}

@Preview(showBackground = true)
@Composable
fun IconButtonCase() {
    Row {
        IconButton(onClick = { /*TODO*/ }) { Icon(Icons.Filled.Search, null) }
        IconButton(onClick = { /*TODO*/ }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
        IconButton(onClick = { /*TODO*/ }) { Icon(Icons.Filled.Done, null) }
    }
}

@Composable
fun ImageCase() {
    var toggle by remember { mutableStateOf(false) }
    val sizeAnim by animateDpAsState(if (toggle) 450.dp else 50.dp, label = "")

    val context = LocalContext.current
    val imageLoader = remember {// 加载svg需要配置imageLoader
        ImageLoader.Builder(context).components { add(SvgDecoder.Factory()) }.build()
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CoilImage(
            "https://coil-kt.github.io/coil/images/coil_logo_black.svg",
            Modifier
                .size(sizeAnim)
                .clickable(
                    onClick = { toggle = !toggle },
                    indication = null,// 去除水波纹效果
                    interactionSource = remember { MutableInteractionSource() }
                ),
            imageLoader = imageLoader
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SliderCase() {
    var progress by remember { mutableFloatStateOf(0f) }
    Column {
        Text("${progress.roundToInt()}", Modifier.align(Alignment.CenterHorizontally))
        Slider(
            value = progress,
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = Color(0xFF0079D3)),
            onValueChange = { progress = it }
        )
    }
}

/**
 * 深度自定义使用：BasicTextField
 */
@Preview(showBackground = true)
@Composable
fun TextFieldCase() {
    var text by remember { mutableStateOf("") }
    var hidePwd by remember { mutableStateOf(false) }
    val textFieldColors = TextFieldDefaults.colors(
        cursorColor = Color.Red
    )

    TextField(
        text,
        colors = textFieldColors,
        onValueChange = { text = it },
        singleLine = true,
        label = { Text("邮箱") },
        leadingIcon = { Icon(Icons.Filled.Search, null) },
        trailingIcon = {
            IconButton({ hidePwd = !hidePwd }) {
                val imageVector = if (hidePwd) Icons.Filled.Lock else Icons.Outlined.Lock
                Icon(imageVector, null)
            }
        },
        visualTransformation = if (hidePwd) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Preview
@Composable
fun BasicTextFieldCase() {
    var text by remember { mutableStateOf("") }
    BasicTextField(
        text,
        onValueChange = { text = it },
        Modifier.background(Color.White, CircleShape).fillMaxWidth(),
        decorationBox = { innerTextField ->// 自定义输入栏样式
            Row(Modifier.padding(10.dp, 5.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {/*TODO*/ }) { Icon(Icons.Filled.Face, null) }
                Box(Modifier.weight(1f), Alignment.CenterStart) { innerTextField() }
                IconButton(onClick = {/*TODO*/ }) { Icon(Icons.AutoMirrored.Filled.Send, null) }
            }
        }
    )
}

/**
 * 滑动删除
 */
@Preview
@Composable
fun SwipeToDismissCase() {
    val state = rememberSwipeToDismissBoxState()
    val backgroundContent: @Composable RowScope.() -> Unit = {
        Box(
            Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.Red),
            Alignment.Center
        ) {
            Text("Delete", color = Color.White)
        }
    }
    SwipeToDismissBox(state, backgroundContent) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.LightGray),
            Alignment.Center
        ) {
            Text("Swipe to dismiss", color = Color.Black)
        }
    }
}