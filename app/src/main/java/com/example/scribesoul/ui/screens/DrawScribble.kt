package com.example.scribesoul.ui.screens

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Workspaces
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor as SolidColorBrush
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.scribesoul.R
import com.example.scribesoul.commands.*
import com.example.scribesoul.models.*
import com.example.scribesoul.models.LinearGradient as LinearGradientFill
import com.example.scribesoul.models.RadialGradient as RadialGradientFill
import com.example.scribesoul.models.SolidColor as SolidColorFill
import com.example.scribesoul.utils.*
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

data class GuideLine(val start: Offset, val end: Offset)

enum class ColorPickerTarget {
    DRAW_STROKE,
    EDIT_SELECTION,
    ADD_SHAPE
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun DrawScribbleScreen(navController: NavController) {
    val undoStack = remember { mutableStateListOf<Command>() }
    val redoStack = remember { mutableStateListOf<Command>() }
    val paths = remember { mutableStateListOf<DrawablePath>() }
    val shapes = remember { mutableStateListOf<ShapeItem>() }
    val texts = remember { mutableStateListOf<EditableText>() }
    val imageLayers = remember { mutableStateListOf<ImageLayer>() }
    val groups = remember { mutableStateListOf<ItemGroup>() }
    val currentPath = remember { mutableStateListOf<Offset>() }
    var toolMode by remember { mutableStateOf(ToolMode.DRAW) }
    var isAddingText by remember { mutableStateOf(false) }
    val selectedItems = remember { mutableStateListOf<Movable>() }
    val showShapeMenu = remember { mutableStateOf(false) }
    val showLayerMenu = remember { mutableStateOf(false) }
    val canvasCenter = remember { mutableStateOf(Offset(500f, 500f)) }
    var drawThickness by remember { mutableFloatStateOf(8f) }
    var eraseThickness by remember { mutableFloatStateOf(40f) }
    var drawColor by remember { mutableStateOf(Color.Black) }
    val selectedPaths = remember { mutableStateListOf<DrawablePath>() }
    var colorPickerTarget by remember { mutableStateOf<ColorPickerTarget?>(null) }
    var pendingShapeType by remember { mutableStateOf<String?>(null) }
    val guideLines = remember { mutableStateListOf<GuideLine>() }
    var showGradientPicker by remember { mutableStateOf(false) }

    fun executeCommand(command: Command) {
        command.execute()
        undoStack.add(command)
        redoStack.clear()
        selectedItems.clear()
        selectedPaths.clear()
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            executeCommand(AddImageCommand(ImageLayer(uri = it, offset = canvasCenter.value), imageLayers))
        }
    }

    val density = LocalDensity.current

    if (colorPickerTarget != null) {
        val initialColor = when (colorPickerTarget) {
            ColorPickerTarget.DRAW_STROKE -> drawColor
            ColorPickerTarget.EDIT_SELECTION -> {
                val firstSelected = (selectedItems.firstOrNull() as? Colorable)
                    ?: selectedPaths.firstOrNull()
                (firstSelected?.fill as? SolidColorFill)?.color ?: Color.Black
            }
            ColorPickerTarget.ADD_SHAPE -> Color.Red
            else -> Color.Black
        }

        ColorPickerDialog(
            initialColor = initialColor,
            onDismissRequest = { colorPickerTarget = null },
            onColorSelected = { selectedColor ->
                when (colorPickerTarget) {
                    ColorPickerTarget.DRAW_STROKE -> drawColor = selectedColor
                    ColorPickerTarget.EDIT_SELECTION -> {
                        val allTargets = selectedItems.toList() + selectedPaths.toList()
                        executeCommand(ChangeFillStyleCommand(allTargets, SolidColorFill(selectedColor)))
                    }
                    ColorPickerTarget.ADD_SHAPE -> {
                        pendingShapeType?.let { shapeType ->
                            executeCommand(AddShapeCommand(ShapeItem(shapeType, canvasCenter.value, fill = SolidColorFill(selectedColor)), shapes))
                        }
                    }
                    null -> {}
                }
                colorPickerTarget = null
                pendingShapeType = null
            }
        )
    }

    if (showGradientPicker) {
        GradientPickerDialog(
            onDismissRequest = { showGradientPicker = false },
            onGradientSelected = { colors ->
                val allTargets = selectedItems.toList() + selectedPaths.toList()
                executeCommand(ChangeFillStyleCommand(allTargets, LinearGradientFill(colors)))
                showGradientPicker = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(toolMode, drawThickness, eraseThickness, drawColor) {
                detectDragGestures(
                    onDragStart = { currentPath.clear(); currentPath.add(it) },
                    onDrag = { change, _ -> currentPath.add(change.position) },
                    onDragEnd = {
                        when (toolMode) {
                            ToolMode.ERASE -> {
                                val originalPaths = paths.toList()
                                val pathsAfterErase = mutableListOf<DrawablePath>()
                                originalPaths.forEach { drawablePath ->
                                    if (drawablePath.toolMode == ToolMode.Highlighter) {
                                        pathsAfterErase.add(drawablePath)
                                        return@forEach
                                    }
                                    val pointErasureStatus = drawablePath.offsets.map { point ->
                                        currentPath.any { eraserPoint -> distance(point, eraserPoint) < eraseThickness }
                                    }
                                    var currentSegment = mutableListOf<Offset>()
                                    pointErasureStatus.forEachIndexed { index, isErased ->
                                        if (!isErased) {
                                            currentSegment.add(drawablePath.offsets[index])
                                        } else {
                                            if (currentSegment.size > 1) {
                                                pathsAfterErase.add(drawablePath.copy(offsets = currentSegment.toList()))
                                            }
                                            currentSegment = mutableListOf()
                                        }
                                    }
                                    if (currentSegment.size > 1) {
                                        pathsAfterErase.add(drawablePath.copy(offsets = currentSegment.toList()))
                                    }
                                }
                                if (originalPaths != pathsAfterErase) {
                                    executeCommand(EraseCommand(originalPaths, pathsAfterErase, paths))
                                }
                            }
                            ToolMode.Lasso -> {
                                if (currentPath.size > 2) {
                                    val polygon = currentPath.toList()
                                    selectedItems.clear()
                                    selectedPaths.clear()
                                    val allMovables = texts + shapes + imageLayers + groups
                                    allMovables.forEach { item ->
                                        if (isMovableInPolygon(item, polygon, density)) {
                                            selectedItems.add(item)
                                        }
                                    }
                                    paths.forEach { path ->
                                        if (path.offsets.any { point -> isPointInPolygon(point, polygon) }) {
                                            selectedPaths.add(path)
                                        }
                                    }
                                }
                            }
                            else -> {
                                if (currentPath.isNotEmpty()) {
                                    val newPath = DrawablePath(
                                        offsets = currentPath.toList(),
                                        toolMode = toolMode,
                                        thickness = drawThickness,
                                        fill = SolidColorFill(if (toolMode == ToolMode.Highlighter) Color.Yellow.copy(alpha = 0.5f) else drawColor)
                                    )
                                    executeCommand(AddDrawableCommand(newPath, paths))
                                }
                            }
                        }
                        currentPath.clear()
                    }
                )
            }
            .pointerInput(isAddingText) {
                detectTapGestures { offset ->
                    if (isAddingText) {
                        executeCommand(AddTextCommand(EditableText(text = "New Text", offset = offset), texts))
                        isAddingText = false
                    }
                    texts.forEach { it.isEditing = false }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            paths.forEach { path ->
                drawPathFromFill(path.offsets, path.fill, path.toolMode, path.thickness)
            }
            drawPathFromFill(currentPath, SolidColorFill(drawColor), toolMode, drawThickness)
            selectedPaths.forEach { drawSelectionBorder(it.offsets) }
            if (toolMode == ToolMode.ERASE && currentPath.isNotEmpty()) {
                val lastPoint = currentPath.last()
                drawCircle(color = Color.LightGray.copy(alpha = 0.5f), center = lastPoint, radius = eraseThickness, style = Stroke(width = 2.dp.toPx()))
            }
            guideLines.forEach { line ->
                drawLine(color = Color.Cyan, start = line.start, end = line.end, strokeWidth = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
            }
        }

        val allMovables = remember(texts, shapes, imageLayers, groups) { texts + shapes + imageLayers + groups }
        val individualItems = remember(texts, shapes, imageLayers) { texts + shapes + imageLayers }

        individualItems.forEach { item ->
            key(item) {
                RenderMovableItem(
                    item = item,
                    isSelected = selectedItems.contains(item),
                    onSelect = {
                        selectedItems.clear()
                        selectedPaths.clear()
                        selectedItems.add(item)
                        texts.forEach { it.isEditing = false }
                    },
                    onDoubleClick = { if (it is EditableText) it.isEditing = true },
                    executeCommand = { executeCommand(it) },
                    allItems = allMovables,
                    guideLines = guideLines
                )
            }
        }

        groups.forEach { group ->
            key(group) {
                group.items.forEach { item ->
                    key(item) {
                        RenderMovableItem(
                            item = item,
                            isSelected = selectedItems.contains(group),
                            isGrouped = true,
                            groupOffset = group.offset,
                            groupRotation = group.rotation,
                            onSelect = {
                                selectedItems.clear()
                                selectedItems.add(group)
                            },
                            onDoubleClick = {},
                            executeCommand = { executeCommand(it) },
                            allItems = null,
                            guideLines = null
                        )
                    }
                }
                GroupHandles(
                    group = group,
                    isSelected = selectedItems.contains(group),
                    executeCommand = { executeCommand(it) }
                )
            }
        }

        if (selectedItems.isNotEmpty() || selectedPaths.isNotEmpty()) {
            Box(modifier = Modifier.align(Alignment.TopCenter).padding(top = 120.dp)) {
                PropertiesToolbar(
                    selectedItems = selectedItems,
                    selectedPaths = selectedPaths,
                    executeCommand = { executeCommand(it) },
                    onClearSelection = {
                        selectedItems.clear()
                        selectedPaths.clear()
                    },
                    allLists = listOf(texts, shapes, imageLayers, groups),
                    onShowColorPicker = { colorPickerTarget = ColorPickerTarget.EDIT_SELECTION },
                    onShowGradientPicker = { showGradientPicker = true }
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 60.dp, start = 20.dp, end = 20.dp).align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // -- FIX: Logika Undo/Redo yang benar --
                    Image(painter = painterResource(id = R.drawable.arrow_left), contentDescription = "Undo",
                        modifier = Modifier.size(20.dp).clickable(enabled = undoStack.isNotEmpty()) {
                            val commandToUndo = undoStack.removeLastOrNull()
                            commandToUndo?.let {
                                it.undo()
                                redoStack.add(it)
                            }
                        })
                    Image(painter = painterResource(id = R.drawable.arrow_right), contentDescription = "Redo",
                        modifier = Modifier.size(20.dp).clickable(enabled = redoStack.isNotEmpty()) {
                            val commandToRedo = redoStack.removeLastOrNull()
                            commandToRedo?.let {
                                it.execute()
                                undoStack.add(it)
                            }
                        })
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.text), contentDescription = "Text", modifier = Modifier.size(18.dp).clickable { isAddingText = true })
                    Image(painter = painterResource(id = R.drawable.lassotool), contentDescription = "Lasso", modifier = Modifier.size(22.dp).clickable { toolMode = ToolMode.Lasso })
                    Image(painter = painterResource(id = R.drawable.image), contentDescription = "Image", modifier = Modifier.size(22.dp).clickable { imagePickerLauncher.launch("image/*") })
                    Box {
                        Image(painter = painterResource(id = R.drawable.shapeasset), contentDescription = "Shape", modifier = Modifier.size(22.dp).clickable { showShapeMenu.value = true })
                        DropdownMenu(expanded = showShapeMenu.value, onDismissRequest = { showShapeMenu.value = false }) {
                            DropdownMenuItem(onClick = { pendingShapeType = "Circle"; colorPickerTarget = ColorPickerTarget.ADD_SHAPE; showShapeMenu.value = false }, text = { Text("Circle") })
                            DropdownMenuItem(onClick = { pendingShapeType = "Rectangle"; colorPickerTarget = ColorPickerTarget.ADD_SHAPE; showShapeMenu.value = false }, text = { Text("Rectangle") })
                            DropdownMenuItem(onClick = { pendingShapeType = "Star"; colorPickerTarget = ColorPickerTarget.ADD_SHAPE; showShapeMenu.value = false }, text = { Text("Star") })
                            DropdownMenuItem(onClick = { pendingShapeType = "Triangle"; colorPickerTarget = ColorPickerTarget.ADD_SHAPE; showShapeMenu.value = false }, text = { Text("Triangle") })
                            DropdownMenuItem(onClick = { pendingShapeType = "Hexagon"; colorPickerTarget = ColorPickerTarget.ADD_SHAPE; showShapeMenu.value = false }, text = { Text("Hexagon") })
                        }
                    }
                    Image(painter = painterResource(id = R.drawable.pencil), contentDescription = "Pencil", modifier = Modifier.size(22.dp).clickable {
                        toolMode = ToolMode.DRAW; colorPickerTarget = ColorPickerTarget.DRAW_STROKE
                    })
                    Image(painter = painterResource(id = R.drawable.eraser), contentDescription = "Eraser", modifier = Modifier.size(22.dp).clickable { toolMode = ToolMode.ERASE })
                    Box {
                        val isLayerMenuEnabled = selectedItems.size == 1
                        Image(
                            painter = painterResource(id = R.drawable.layer), contentDescription = "Layer",
                            modifier = Modifier.size(22.dp).clickable(enabled = isLayerMenuEnabled) { showLayerMenu.value = true },
                            alpha = if (isLayerMenuEnabled) 1f else 0.4f
                        )
                        DropdownMenu(expanded = showLayerMenu.value, onDismissRequest = { showLayerMenu.value = false }) {
                            DropdownMenuItem(text = { Text("Bring to Front") }, onClick = {
                                if (isLayerMenuEnabled) {
                                    executeCommand(LayeringCommand(selectedItems.first(), listOf(texts, shapes, imageLayers, groups), LayerDirection.TO_FRONT))
                                }; showLayerMenu.value = false
                            })
                            DropdownMenuItem(text = { Text("Send to Back") }, onClick = {
                                if (isLayerMenuEnabled) {
                                    executeCommand(LayeringCommand(selectedItems.first(), listOf(texts, shapes, imageLayers, groups), LayerDirection.TO_BACK))
                                }; showLayerMenu.value = false
                            })
                        }
                    }
                    Image(painter = painterResource(id = R.drawable.dot3), contentDescription = "More", modifier = Modifier.size(22.dp))
                }
            }
        }

        AnimatedVisibility(
            visible = toolMode == ToolMode.DRAW || toolMode == ToolMode.ERASE,
            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp)
        ) {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f), RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (toolMode == ToolMode.DRAW) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painterResource(id = R.drawable.pencil), contentDescription = "Pencil", Modifier.size(24.dp))
                        Text("Draw", fontSize = 12.sp)
                        Slider(value = drawThickness, onValueChange = { drawThickness = it }, valueRange = 1f..50f, modifier = Modifier.width(80.dp).height(150.dp).rotate(270f))
                        Text("${drawThickness.toInt()}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
                if (toolMode == ToolMode.ERASE) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painterResource(id = R.drawable.eraser), contentDescription = "Eraser", Modifier.size(24.dp))
                        Text("Erase", fontSize = 12.sp)
                        Slider(value = eraseThickness, onValueChange = { eraseThickness = it }, valueRange = 10f..100f, modifier = Modifier.width(70.dp).height(150.dp).rotate(270f))
                        Text("${eraseThickness.toInt()}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            // Pastikan Anda memiliki definisi untuk Composable ini di proyek Anda.
             BottomBarScribble(navController = navController)
        }
    }
}

private fun calculateSnapping(draggedItem: Movable, allItems: List<Movable>, snapThreshold: Float = 10f): Pair<Offset, List<GuideLine>> {
    var snapCorrection = Offset.Zero
    val newGuideLines = mutableListOf<GuideLine>()
    fun getBounds(item: Movable): Rect? {
        val size = when (item) {
            is ShapeItem -> item.size
            is ImageLayer -> item.size
            is EditableText -> if (item.size == Size.Zero) Size(50f, 50f) else item.size
            else -> return null
        }
        return Rect(
            left = item.offset.x - size.width / 2,
            top = item.offset.y - size.height / 2,
            right = item.offset.x + size.width / 2,
            bottom = item.offset.y + size.height / 2
        )
    }
    val draggedBounds = getBounds(draggedItem) ?: return Pair(Offset.Zero, emptyList())
    val otherItems = allItems.filter { it != draggedItem && it !is ItemGroup }
    for (staticItem in otherItems) {
        val staticBounds = getBounds(staticItem) ?: continue
        val draggedXPoints = listOf(draggedBounds.left, draggedBounds.center.x, draggedBounds.right)
        val staticXPoints = listOf(staticBounds.left, staticBounds.center.x, staticBounds.right)
        for (draggedX in draggedXPoints) {
            for (staticX in staticXPoints) {
                if (abs(draggedX - staticX) < snapThreshold && snapCorrection.x == 0f) {
                    snapCorrection = snapCorrection.copy(x = staticX - draggedX)
                    val guideYStart = min(draggedBounds.top, staticBounds.top) - 20f
                    val guideYEnd = max(draggedBounds.bottom, staticBounds.bottom) + 20f
                    newGuideLines.add(GuideLine(Offset(staticX, guideYStart), Offset(staticX, guideYEnd)))
                }
            }
        }
        val draggedYPoints = listOf(draggedBounds.top, draggedBounds.center.y, draggedBounds.bottom)
        val staticYPoints = listOf(staticBounds.top, staticBounds.center.y, staticBounds.bottom)
        for (draggedY in draggedYPoints) {
            for (staticY in staticYPoints) {
                if (abs(draggedY - staticY) < snapThreshold && snapCorrection.y == 0f) {
                    snapCorrection = snapCorrection.copy(y = staticY - draggedY)
                    val guideXStart = min(draggedBounds.left, staticBounds.left) - 20f
                    val guideXEnd = max(draggedBounds.right, staticBounds.right) + 20f
                    newGuideLines.add(GuideLine(Offset(guideXStart, staticY), Offset(guideXEnd, staticY)))
                }
            }
        }
    }
    return Pair(snapCorrection, newGuideLines)
}

@Composable
fun RenderMovableItem(
    item: Movable,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDoubleClick: (Movable) -> Unit,
    executeCommand: (Command) -> Unit,
    isGrouped: Boolean = false,
    groupOffset: Offset = Offset.Zero,
    groupRotation: Float = 0f,
    allItems: List<Movable>?,
    guideLines: MutableList<GuideLine>?
) {
    var startOffset by remember { mutableStateOf(Offset.Zero) }
    val modifier = Modifier
        .graphicsLayer {
            val finalOffset = if (isGrouped) groupOffset else item.offset
            val finalRotation = if (isGrouped) groupRotation else item.rotation
            translationX = finalOffset.x
            translationY = finalOffset.y
            rotationZ = finalRotation
            if (isGrouped) {
                translationX += item.offset.x
                translationY += item.offset.y
                rotationZ += item.rotation
            }
        }
        .pointerInput(item, isGrouped) {
            if (!isGrouped) {
                detectTapGestures(onTap = { onSelect() }, onDoubleTap = { onDoubleClick(item) })
            }
        }
        .pointerInput(item, isGrouped, allItems, guideLines) {
            if (!isGrouped && allItems != null && guideLines != null) {
                detectDragGestures(
                    onDragStart = {
                        startOffset = item.offset
                        guideLines.clear()
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val newOffset = item.offset + dragAmount
                        val (snapCorrection, newGuides) = calculateSnapping(item.copyForDrag(newOffset), allItems)
                        item.offset = newOffset + snapCorrection
                        guideLines.clear()
                        guideLines.addAll(newGuides)
                    },
                    onDragEnd = {
                        guideLines.clear()
                        if (startOffset != item.offset) {
                            executeCommand(MoveCommand(item, startOffset, item.offset))
                        }
                    }
                )
            }
        }
        .then(if (isSelected && !isGrouped) Modifier.border(2.dp, Color.Blue, RoundedCornerShape(2.dp)) else Modifier)

    Box(modifier = modifier) {
        when (item) {
            is EditableText -> {
                val focusRequester = remember { FocusRequester() }
                val textStyle = TextStyle(
                    brush = when (val fill = item.fill) {
                        is SolidColorFill -> SolidColorBrush(fill.color)
                        is LinearGradientFill -> Brush.linearGradient(fill.colors)
                        is RadialGradientFill -> Brush.radialGradient(fill.colors)
                    },
                    fontSize = item.fontSize.sp
                )
                if (item.isEditing && isSelected) {
                    BasicTextField(
                        value = item.text,
                        onValueChange = { item.text = it },
                        textStyle = textStyle,
                        modifier = Modifier.focusRequester(focusRequester).padding(4.dp).widthIn(min = 50.dp)
                    )
                    LaunchedEffect(Unit) { focusRequester.requestFocus() }
                } else {
                    Text(
                        text = if (item.text.isEmpty()) "Type..." else item.text,
                        style = textStyle,
                        modifier = Modifier.padding(4.dp),
                        onTextLayout = { item.size = Size(it.size.width.toFloat(), it.size.height.toFloat()) }
                    )
                }
            }
            is ShapeItem -> {
                Box(modifier = Modifier.size(item.size.width.dp, item.size.height.dp)) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val brush = when (val fill = item.fill) {
                            is SolidColorFill -> SolidColorBrush(fill.color)
                            is LinearGradientFill -> Brush.linearGradient(fill.colors, start = Offset.Zero, end = Offset(size.width, size.height))
                            is RadialGradientFill -> Brush.radialGradient(fill.colors, center = center, radius = size.width / 2f)
                        }
                        when (item.type) {
                            "Circle" -> drawCircle(brush, radius = size.width / 2f, center = center)
                            "Star" -> drawStar(brush, center = center, radius = size.width / 2f)
                            "Rectangle" -> drawRoundRect(brush, size = this.size, cornerRadius = CornerRadius(item.cornerRadius, item.cornerRadius))
                            "Triangle" -> drawPolygon(3, brush, center = center, radius = size.width / 2f)
                            "Hexagon" -> drawPolygon(6, brush, center = center, radius = size.width / 2f)
                        }
                    }
                }
            }
            is ImageLayer -> {
                val context = LocalContext.current
                val painter = rememberAsyncImagePainter(model = ImageRequest.Builder(context).data(item.uri).size(coil.size.Size.ORIGINAL).build(),
                    onSuccess = { result ->
                        if (item.size == Size(100f, 100f)) {
                            val drawable = result.result.drawable
                            if (drawable.intrinsicHeight > 0) {
                                item.size = Size(300f, 300f / (drawable.intrinsicWidth.toFloat() / drawable.intrinsicHeight))
                            }
                        }
                    })
                Image(painter = painter, contentDescription = "Image Layer", modifier = Modifier.size(item.size.width.dp, item.size.height.dp))
            }
        }
        if (isSelected && !isGrouped) {
            InteractionHandles(item = item, executeCommand = { executeCommand(it) })
        }
    }
}

private fun Movable.copyForDrag(newOffset: Offset): Movable {
    return when (this) {
        is ShapeItem -> this.copy(offset = newOffset)
        is EditableText -> this.copy(offset = newOffset)
        is ImageLayer -> this.copy(offset = newOffset)
        is ItemGroup -> this.copy(offset = newOffset)
        else -> this
    }
}

@Composable
fun GroupHandles(group: ItemGroup, isSelected: Boolean, executeCommand: (Command) -> Unit) {
    var startOffset by remember { mutableStateOf(Offset.Zero) }
    var startRotation by remember { mutableFloatStateOf(0f) }
    if (isSelected) {
        val groupBounds = calculateGroupBounds(group)
        val density = LocalDensity.current
        Box(
            modifier = Modifier
                .size(width = (groupBounds.width / density.density).dp, height = (groupBounds.height / density.density).dp)
                .graphicsLayer {
                    val pivotX = -groupBounds.left
                    val pivotY = -groupBounds.top
                    transformOrigin = TransformOrigin(
                        pivotFractionX = if (groupBounds.width != 0f) pivotX / groupBounds.width else 0.5f,
                        pivotFractionY = if (groupBounds.height != 0f) pivotY / groupBounds.height else 0.5f
                    )
                    rotationZ = group.rotation
                    translationX = group.offset.x + groupBounds.left
                    translationY = group.offset.y + groupBounds.top
                }
                .border(2.dp, Color.Cyan, RoundedCornerShape(2.dp))
        )
    }
    val handleModifier = Modifier.size(24.dp).border(1.dp, Color.White, CircleShape)
    Box(
        modifier = Modifier
            .offset { IntOffset(group.offset.x.toInt() - 12, group.offset.y.toInt() - 12) }
            .then(handleModifier).background(Color.Cyan.copy(alpha = 0.8f), CircleShape)
            .pointerInput(group) {
                detectDragGestures(
                    onDragStart = { startOffset = group.offset },
                    onDrag = { _, dragAmount -> group.offset += dragAmount },
                    onDragEnd = { executeCommand(MoveCommand(group, startOffset, group.offset)) }
                )
            }
    )
    Box(
        modifier = Modifier
            .offset { IntOffset((group.offset.x + 80).toInt(), group.offset.y.toInt() - 12) }
            .then(handleModifier).background(Color.Magenta.copy(alpha = 0.8f), CircleShape)
            .pointerInput(group) {
                detectDragGestures(
                    onDragStart = { startRotation = group.rotation },
                    onDrag = { _, dragAmount -> group.rotation += dragAmount.x },
                    onDragEnd = { executeCommand(RotateCommand(group, startRotation, group.rotation)) }
                )
            }
    )
}

@Composable
fun BoxScope.InteractionHandles(item: Movable, executeCommand: (Command) -> Unit) {
    var dragStartSize by remember { mutableStateOf(Size.Zero) }
    var dragStartRotation by remember { mutableFloatStateOf(0f) }
    if (item is ShapeItem || item is ImageLayer) {
        val size = if (item is ShapeItem) item.size else (item as ImageLayer).size
        Box(
            modifier = Modifier.align(Alignment.BottomEnd).offset(x = 12.dp, y = 12.dp).size(24.dp)
                .background(Color.Blue, CircleShape).border(1.dp, Color.White, CircleShape)
                .pointerInput(item) {
                    detectDragGestures(
                        onDragStart = { dragStartSize = size },
                        onDrag = { _, dragAmount ->
                            val newSize = Size((size.width + dragAmount.x).coerceAtLeast(50f), (size.height + dragAmount.y).coerceAtLeast(50f))
                            if (item is ShapeItem) item.size = newSize else if (item is ImageLayer) item.size = newSize
                        },
                        onDragEnd = {
                            val finalSize = if (item is ShapeItem) item.size else (item as ImageLayer).size
                            if (dragStartSize != finalSize) {
                                executeCommand(ResizeCommand(item, dragStartSize, finalSize))
                            }
                        }
                    )
                }
        )
    }
    if (item is ShapeItem && item.type == "Rectangle") {
        var startRadius by remember { mutableFloatStateOf(0f) }
        Box(
            modifier = Modifier.align(Alignment.TopStart).offset(x = (-12).dp, y = (-12).dp).size(24.dp)
                .background(Color.Green, CircleShape).border(1.dp, Color.White, CircleShape)
                .pointerInput(item) {
                    detectDragGestures(
                        onDragStart = { startRadius = item.cornerRadius },
                        onDrag = { _, dragAmount ->
                            val newRadius = item.cornerRadius + dragAmount.x
                            item.cornerRadius = newRadius.coerceIn(0f, min(item.size.width, item.size.height) / 2)
                        },
                        onDragEnd = {
                            if (startRadius != item.cornerRadius) {
                                executeCommand(ChangeShapePropertyCommand(item, "cornerRadius", startRadius, item.cornerRadius))
                            }
                        }
                    )
                }
        )
    }
    Box(
        modifier = Modifier.align(Alignment.TopEnd).offset(x = 12.dp, y = (-12).dp).size(24.dp)
            .background(Color.Magenta, CircleShape).border(1.dp, Color.White, CircleShape)
            .pointerInput(item) {
                detectDragGestures(
                    onDragStart = { dragStartRotation = item.rotation },
                    onDrag = { _, dragAmount -> item.rotation += dragAmount.x },
                    onDragEnd = { executeCommand(RotateCommand(item, dragStartRotation, item.rotation)) }
                )
            }
    )
}

@Composable
fun BoxScope.PropertiesToolbar(
    selectedItems: List<Movable>,
    selectedPaths: List<DrawablePath>,
    executeCommand: (Command) -> Unit,
    onClearSelection: () -> Unit,
    allLists: List<MutableList<out Movable>>,
    onShowColorPicker: () -> Unit,
    onShowGradientPicker: () -> Unit
) {
    Surface(modifier = Modifier.align(Alignment.TopCenter).padding(top = 120.dp), shape = MaterialTheme.shapes.medium, shadowElevation = 4.dp) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (selectedItems.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Actions:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = {
                        if (selectedItems.size > 1) {
                            executeCommand(GroupCommand(selectedItems.toList(), allLists.take(3), allLists[3] as MutableList<ItemGroup>))
                        }
                    }) { Icon(Icons.Default.Workspaces, "Group") }
                    IconButton(onClick = { executeCommand(CopyCommand(selectedItems.toList(), allLists)) }) { Icon(Icons.Default.ContentCopy, "Copy") }
                    IconButton(onClick = { executeCommand(DeleteItemsCommand(selectedItems.toList(), allLists)) }) { Icon(Icons.Default.Delete, "Delete", tint = Color.Red) }
                }
            }
            val allColorables = selectedItems.filterIsInstance<Colorable>() + selectedPaths
            if (allColorables.isNotEmpty()) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onShowColorPicker) {
                        Icon(Icons.Default.Palette, contentDescription = "Change Solid Color")
                        Spacer(Modifier.width(8.dp))
                        Text("Solid")
                    }
                    Button(onClick = onShowGradientPicker) {
                        // -- FIX: Ganti dengan ikon bawaan untuk menghindari error resource --
                        Icon(Icons.Default.AutoAwesome, contentDescription = "Change Gradient")
                        Spacer(Modifier.width(8.dp))
                        Text("Gradient")
                    }
                }
            }
        }
    }
}

@Composable
fun GradientPickerDialog(
    onDismissRequest: () -> Unit,
    onGradientSelected: (colors: List<Color>) -> Unit
) {
    var color1 by remember { mutableStateOf(Color.Cyan) }
    var color2 by remember { mutableStateOf(Color.Magenta) }
    var activePicker by remember { mutableStateOf<Int?>(null) }
    if (activePicker != null) {
        ColorPickerDialog(
            initialColor = if (activePicker == 1) color1 else color2,
            onDismissRequest = { activePicker = null },
            onColorSelected = { selectedColor ->
                if (activePicker == 1) color1 = selectedColor else color2 = selectedColor
                activePicker = null
            }
        )
    }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Gradient Colors") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(12.dp)).border(1.dp, Color.Gray, RoundedCornerShape(12.dp)).background(Brush.linearGradient(listOf(color1, color2))))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
                    Text("Start Color")
                    Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(color1).border(1.dp, Color.Gray, CircleShape).clickable { activePicker = 1 })
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
                    Text("End Color")
                    Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(color2).border(1.dp, Color.Gray, CircleShape).clickable { activePicker = 2 })
                }
            }
        },
        confirmButton = { Button(onClick = { onGradientSelected(listOf(color1, color2)) }) { Text("OK") } },
        dismissButton = { TextButton(onClick = onDismissRequest) { Text("Cancel") } }
    )
}

@Composable
fun ColorPickerDialog(initialColor: Color, onDismissRequest: () -> Unit, onColorSelected: (Color) -> Unit) {
    var red by remember { mutableStateOf(initialColor.red * 255f) }
    var green by remember { mutableStateOf(initialColor.green * 255f) }
    var blue by remember { mutableStateOf(initialColor.blue * 255f) }
    val currentColor = Color(red / 255f, green / 255f, blue / 255f)
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Color") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(12.dp)).border(1.dp, Color.Gray, RoundedCornerShape(12.dp)).background(currentColor))
                ColorInputRow(label = "R", sliderColor = Color.Red, value = red, onValueChange = { red = it })
                ColorInputRow(label = "G", sliderColor = Color.Green, value = green, onValueChange = { green = it })
                ColorInputRow(label = "B", sliderColor = Color.Blue, value = blue, onValueChange = { blue = it })
            }
        },
        confirmButton = { Button(onClick = { onColorSelected(currentColor) }) { Text("OK") } },
        dismissButton = { TextButton(onClick = onDismissRequest) { Text("Cancel") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColorInputRow(label: String, sliderColor: Color, value: Float, onValueChange: (Float) -> Unit) {
    var textValue by remember { mutableStateOf(value.toInt().toString()) }
    LaunchedEffect(value) { if (value.toInt().toString() != textValue.ifEmpty { "0" }) { textValue = value.toInt().toString() } }
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(label, color = sliderColor, fontWeight = FontWeight.Bold)
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape).background(Brush.horizontalGradient(colors = listOf(Color.Black, sliderColor))))
            Slider(
                value = value, onValueChange = onValueChange, valueRange = 0f..255f, steps = 254,
                colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = Color.Transparent, inactiveTrackColor = Color.Transparent),
                modifier = Modifier.fillMaxWidth()
            )
        }
        OutlinedTextField(
            value = textValue,
            onValueChange = { newText: String ->
                if (newText.isEmpty()) {
                    textValue = ""; onValueChange(0f)
                } else if (newText.all { it.isDigit() }) {
                    val clampedValue = newText.toInt().coerceIn(0, 255)
                    textValue = if (newText.toInt() != clampedValue) clampedValue.toString() else newText
                    onValueChange(clampedValue.toFloat())
                }
            },
            modifier = Modifier.width(80.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

fun DrawScope.drawPathFromFill(offsets: List<Offset>, fill: FillStyle, mode: ToolMode, thickness: Float) {
    if (offsets.size < 2) return
    val path = Path().apply {
        moveTo(offsets.first().x, offsets.first().y)
        (1 until offsets.size).forEach { lineTo(offsets[it].x, offsets[it].y) }
    }
    val style = Stroke(
        width = when (mode) {
            ToolMode.Highlighter -> thickness * 2
            else -> thickness
        }
    )
    val brush = when (fill) {
        is SolidColorFill -> SolidColorBrush(fill.color.copy(alpha = if (mode == ToolMode.Highlighter) 0.4f else fill.color.alpha))
        is LinearGradientFill -> Brush.linearGradient(fill.colors)
        is RadialGradientFill -> Brush.radialGradient(fill.colors)
    }
    drawPath(path = path, brush = brush, style = style)
}

private fun DrawScope.drawStar(brush: Brush, center: Offset, radius: Float) {
    val path = Path()
    val outerRadius = radius
    val innerRadius = radius / 2.0f
    var angle = -Math.PI / 2
    path.moveTo(center.x + (outerRadius * cos(angle)).toFloat(), center.y + (outerRadius * sin(angle)).toFloat())
    for (i in 1 until 10) {
        val r = if (i % 2 == 0) outerRadius else innerRadius
        angle += Math.PI / 5
        path.lineTo(center.x + (r * cos(angle)).toFloat(), center.y + (r * sin(angle)).toFloat())
    }
    path.close()
    drawPath(path = path, brush = brush, style = Fill)
}

private fun DrawScope.drawPolygon(sides: Int, brush: Brush, center: Offset, radius: Float) {
    if (sides < 3) return
    val path = Path()
    val angle = 2.0 * Math.PI / sides
    path.moveTo(center.x + (radius * cos(0.0)).toFloat(), center.y + (radius * sin(0.0)).toFloat())
    for (i in 1 until sides) {
        path.lineTo(center.x + (radius * cos(angle * i)).toFloat(), center.y + (radius * sin(angle * i)).toFloat())
    }
    path.close()
    drawPath(path = path, brush = brush, style = Fill)
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Preview
@Composable
fun DrawScribblePreview() {
    val dummyController = rememberNavController()
    DrawScribbleScreen(navController = dummyController)
}