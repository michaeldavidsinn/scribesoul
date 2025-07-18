package com.example.scribesoul.ui.screens

import android.os.Build
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.scribesoul.R

enum class ToolMode {
    DRAW, ERASE, Highlighter, Lasso
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun DrawScribbleScreen(navController: NavController? = null) {

    val paths = remember { mutableStateListOf<Pair<List<Offset>, ToolMode>>() }
    var currentPath by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var toolMode by remember { mutableStateOf(ToolMode.DRAW) }
    val undonePaths = remember { mutableStateListOf<Pair<List<Offset>, ToolMode>>() }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val showShapeMenu = remember { mutableStateOf(false) }

    data class ShapeItem(val type: String, var offset: Offset)

    val shapes = remember { mutableStateListOf<ShapeItem>() }

    var isAddingText by remember { mutableStateOf(false) }
    val canvasCenter = remember { mutableStateOf(Offset(500f, 500f)) }

    data class EditableText(var text: String, var offset: Offset, var isEditing: Boolean = false)
    val texts = remember { mutableStateListOf<EditableText>() }
    val selectedTexts = remember { mutableStateListOf<EditableText>() }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri.value = uri }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(toolMode) {
                detectDragGestures(
                    onDragStart = { currentPath = listOf(it) },
                    onDrag = { change, _ -> currentPath = currentPath + change.position },
                    onDragEnd = {
                        if (toolMode == ToolMode.Lasso && currentPath.size > 2) {
                            val polygon = currentPath
                            selectedTexts.clear()
                            texts.forEach { text ->
                                if (isPointInPolygon(text.offset, polygon)) {
                                    selectedTexts.add(text)
                                }
                            }
                        } else if (currentPath.isNotEmpty()) {
                            paths.add(currentPath to toolMode)
                        }
                        currentPath = emptyList()
                    }
                )
            }


            .pointerInput(isAddingText) {
                detectTapGestures { offset ->
                    if (isAddingText) {
                        texts.add(EditableText("", offset, true))
                        isAddingText = false
                    }
                }
            }
    ) {

        imageUri.value?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(model = uri),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize()
            )
        }

        Canvas(modifier = Modifier.fillMaxSize()) {

            paths.forEach { (pathList, mode) -> drawPathFromOffsets(pathList, mode) }
            drawPathFromOffsets(currentPath, toolMode)
        }

            shapes.forEach { shape ->
                val localOffset = shape.offset

                Box(
                    modifier = Modifier
                        .absoluteOffset { IntOffset(localOffset.x.toInt(), localOffset.y.toInt()) }
                        .size(100.dp)
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                shape.offset += dragAmount
                                shape.offset = localOffset
                            }
                        }
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        when (shape.type) {
                            "Circle" -> drawCircle(Color.Red, radius = 50f, center = Offset(50f, 50f))
                            "Star" -> drawStar(center = Offset(50f, 50f), radius = 50f, color = Color.Yellow)
                            "GIF" -> drawRect(
                                color = Color.Magenta,
                                topLeft = Offset(25f, 25f),
                                size = androidx.compose.ui.geometry.Size(50f, 50f)
                            )
                        }
                    }
                }
            }

        texts.forEach { editableText ->
            var localOffset by remember { mutableStateOf(editableText.offset) }
            val focusRequester = remember { FocusRequester() }
            val keyboardController = LocalSoftwareKeyboardController.current
            val isSelected = selectedTexts.contains(editableText)

            Box(
                modifier = Modifier
                    .absoluteOffset(localOffset.x.dp, localOffset.y.dp)
                    .then(
                        if (!editableText.isEditing) {
                            Modifier.pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    localOffset += dragAmount
                                    editableText.offset = localOffset
                                }
                            }
                        } else Modifier
                    )
                    .clickable {
                        texts.forEach { it.isEditing = false }
                        editableText.isEditing = true
                    }
                    .background(if (isSelected) Color.LightGray else Color.Transparent)
                    .padding(2.dp)
            ) {
                if (editableText.isEditing) {
                    LaunchedEffect(editableText) {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }
                    OutlinedTextField(
                        value = editableText.text,
                        onValueChange = { editableText.text = it },
                        singleLine = true,
                        modifier = Modifier
                            .widthIn(min = 40.dp, max = 200.dp)
                            .focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            editableText.isEditing = false
                            keyboardController?.hide()
                        })
                    )
                } else {
                    Text(
                        text = editableText.text,
                        color = Color.Black,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }

        if (selectedTexts.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${selectedTexts.size} item selected", color = Color.White)
                TextButton(onClick = {
                    texts.removeAll(selectedTexts)
                    selectedTexts.clear()
                }) {
                    Text("Delete", color = Color.Red)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_left),
                    contentDescription = "Undo",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            if (paths.isNotEmpty()) {
                                val removed = paths.removeLast()
                                undonePaths.add(removed)
                            }
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = "Redo",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            if (undonePaths.isNotEmpty()) {
                                val restored = undonePaths.removeLast()
                                paths.add(restored)
                            }
                        }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.text),
                    contentDescription = "Text",
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { isAddingText = true }
                )
                Image(
                    painter = painterResource(id = R.drawable.lassotool),
                    contentDescription = "Lasso Tool",
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { toolMode = ToolMode.Lasso }
                )
                Image(
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = "Image",
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { imagePickerLauncher.launch("image/*") }
                )
                imageUri.value?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(model = uri),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(top = 16.dp)
                    )
                }
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.shapeasset),
                        contentDescription = "Shape",
                        modifier = Modifier
                            .size(22.dp)
                            .clickable { showShapeMenu.value = true }
                    )
                    DropdownMenu(
                        expanded = showShapeMenu.value,
                        onDismissRequest = { showShapeMenu.value = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                shapes.add(ShapeItem("Circle", canvasCenter.value))
                                showShapeMenu.value = false
                            },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.RadioButtonUnchecked, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Circle")
                                }
                            }
                        )
                        DropdownMenuItem(
                            onClick = {
                                shapes.add(ShapeItem("Star", canvasCenter.value))
                                showShapeMenu.value = false
                            },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Star, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Star")
                                }
                            }
                        )

                        DropdownMenuItem(
                            onClick = {
                                shapes.add(ShapeItem("GIF", canvasCenter.value))
                                showShapeMenu.value = false
                            },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.PlayArrow, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("GIF Asset")
                                }
                            }
                        )
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.pencil),
                    contentDescription = "Pencil",
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { toolMode = ToolMode.DRAW }
                )
                Image(
                    painter = painterResource(id = R.drawable.eraser),
                    contentDescription = "Eraser",
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { toolMode = ToolMode.ERASE }
                )
                Image(
                    painter = painterResource(id = R.drawable.layer),
                    contentDescription = "Layer",
                    modifier = Modifier.size(22.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.dot3),
                    contentDescription = "More",
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        ) {
            BottomBarScribble()
        }
    }
}

fun DrawScope.drawPathFromOffsets(offsets: List<Offset>, mode: ToolMode) {
    if (offsets.size < 2) return
    val path = Path().apply {
        moveTo(offsets.first().x, offsets.first().y)
        for (i in 1 until offsets.size) {
            lineTo(offsets[i].x, offsets[i].y)
        }
    }
    val color = when (mode) {
        ToolMode.DRAW -> Color.Black
        ToolMode.ERASE -> Color.White
        ToolMode.Highlighter -> Color.Yellow.copy(alpha = 0.5f)
        ToolMode.Lasso -> Color.Blue.copy(alpha = 0.3f)
    }
    drawPath(path = path, color = color, style = Stroke(width = if (mode == ToolMode.Highlighter) 20f else 8f))
}

fun isPointInPolygon(point: Offset, polygon: List<Offset>): Boolean {
    var crossings = 0
    for (i in polygon.indices) {
        val a = polygon[i]
        val b = polygon[(i + 1) % polygon.size]
        if ((a.y > point.y) != (b.y > point.y)) {
            val atX = (b.x - a.x) * (point.y - a.y) / (b.y - a.y + 0.00001f) + a.x
            if (point.x < atX) crossings++
        }
    }
    return crossings % 2 == 1
}

fun DrawScope.drawStar(center: Offset, radius: Float, color: Color) {
    val path = Path()
    val angle = (2.0 * Math.PI / 5).toFloat()
    val innerRadius = radius * 0.5f

    for (i in 0..9) {
        val r = if (i % 2 == 0) radius else innerRadius
        val x = center.x + (r * kotlin.math.cos(i * angle)).toFloat()
        val y = center.y + (r * kotlin.math.sin(i * angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path = path, color = color)
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Preview(showBackground = true)
@Composable
fun DrawScribblePreview() {
    DrawScribbleScreen()
}
