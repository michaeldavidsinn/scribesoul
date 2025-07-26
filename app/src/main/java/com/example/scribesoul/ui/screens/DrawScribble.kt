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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Workspaces
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
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
import com.example.scribesoul.utils.*
import androidx.compose.material.icons.filled.Palette
import androidx.compose.ui.draw.rotate


private enum class ColorPickerTarget {
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
    var dragStartOffset by remember { mutableStateOf(Offset.Zero) }
    var dragStartRotation by remember { mutableFloatStateOf(0f) }

    var drawThickness by remember { mutableFloatStateOf(8f) }
    var eraseThickness by remember { mutableFloatStateOf(40f) }

    var drawColor by remember { mutableStateOf(Color.Black) }
    val selectedPaths = remember { mutableStateListOf<DrawablePath>() }

    var colorPickerTarget by remember { mutableStateOf<ColorPickerTarget?>(null) }
    var pendingShapeType by remember { mutableStateOf<String?>(null) }

    fun executeCommand(command: Command) {
        command.execute()
        undoStack.add(command)
        redoStack.clear()
        // Hapus seleksi setelah aksi dilakukan
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
            ColorPickerTarget.EDIT_SELECTION ->
                (selectedItems.filterIsInstance<Colorable>().firstOrNull()?.color)
                    ?: (selectedPaths.firstOrNull()?.color)
                    ?: Color.Black
            ColorPickerTarget.ADD_SHAPE -> Color.Red
            else -> Color.Black
        }

        ColorPickerDialog(
            initialColor = initialColor,
            onDismissRequest = { colorPickerTarget = null },
            onColorSelected = { selectedColor ->
                when (colorPickerTarget) {
                    ColorPickerTarget.DRAW_STROKE -> {
                        drawColor = selectedColor
                    }
                    ColorPickerTarget.EDIT_SELECTION -> {
                        val allTargets = selectedItems.toList() + selectedPaths.toList()
                        executeCommand(ChangeColorCommand(allTargets, selectedColor))
                    }
                    ColorPickerTarget.ADD_SHAPE -> {
                        pendingShapeType?.let { shapeType ->
                            executeCommand(AddShapeCommand(ShapeItem(shapeType, canvasCenter.value, color = selectedColor), shapes))
                        }
                    }
                    null -> {
                        // This case logically won't happen here but is required for an exhaustive 'when'.
                    }
                }
                colorPickerTarget = null // Close dialog after selection
                pendingShapeType = null  // Reset state
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(toolMode, drawThickness, eraseThickness, drawColor) {
                detectDragGestures(
                    onDragStart = {
                        currentPath.clear()
                        currentPath.add(it)
                    },
                    onDrag = { change, _ ->
                        currentPath.add(change.position)
                    },
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
                                        color = if (toolMode == ToolMode.Highlighter) Color.Yellow else drawColor
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
                }
            }
    ) {
        // --- RENDERING SECTION ---
        Canvas(modifier = Modifier.fillMaxSize()) {

            paths.filter { it.toolMode == ToolMode.Highlighter }.forEach {
                drawPathFromOffsets(it.offsets, it.color, it.toolMode, it.thickness)
            }

            paths.filter { it.toolMode != ToolMode.Highlighter }.forEach {
                drawPathFromOffsets(it.offsets, it.color, it.toolMode, it.thickness)
            }
            drawPathFromOffsets(currentPath, drawColor, toolMode, drawThickness)

            selectedPaths.forEach {
                drawSelectionBorder(it.offsets)
            }

            if (toolMode == ToolMode.ERASE && currentPath.isNotEmpty()) {
                val lastPoint = currentPath.last()
                drawCircle(
                    color = Color.LightGray.copy(alpha = 0.5f),
                    center = lastPoint,
                    radius = eraseThickness,
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }

        val individualItems = remember(texts, shapes, imageLayers) {
            texts + shapes + imageLayers
        }

        individualItems.forEach { item ->
            key(item) {
                RenderMovableItem(
                    item = item,
                    isSelected = selectedItems.contains(item),
                    onSelect = {
                        selectedItems.clear()
                        selectedItems.add(item)
                    },
                    executeCommand = ::executeCommand
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
                            onSelect = {},
                            executeCommand = ::executeCommand
                        )
                    }
                }
                GroupHandles(
                    group = group,
                    isSelected = selectedItems.contains(group),
                    onMoveStart = { dragStartOffset = it },
                    onMoveEnd = { newOffset -> executeCommand(MoveCommand(group, dragStartOffset, newOffset)) },
                    onRotateStart = { dragStartRotation = it },
                    onRotateEnd = { newRotation -> executeCommand(RotateCommand(group, dragStartRotation, newRotation)) },
                    onSelect = {
                        selectedItems.clear()
                        selectedItems.add(group)
                    }
                )
            }
        }

        // --- UI / TOOLBAR SECTION ---
        if (selectedItems.isNotEmpty() || selectedPaths.isNotEmpty()) {
            Box(modifier = Modifier.align(Alignment.TopCenter).padding(top = 120.dp)) {
                PropertiesToolbar(
                    selectedItems = selectedItems,
                    selectedPaths = selectedPaths,
                    executeCommand = ::executeCommand,
                    onClearSelection = {
                        selectedItems.clear()
                        selectedPaths.clear()
                    },
                    allLists = listOf(texts, shapes, imageLayers, groups),
                    onShowColorPicker = {
                        colorPickerTarget = ColorPickerTarget.EDIT_SELECTION
                    }
                )
            }
        }

        // Top Toolbar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 20.dp, end = 20.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Undo/Redo
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Image(painter = painterResource(id = R.drawable.arrow_left), contentDescription = "Undo",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(enabled = undoStack.isNotEmpty()) {
                                val commandToUndo = undoStack.removeLast()
                                commandToUndo.undo()
                                redoStack.add(commandToUndo)
                            })
                    Image(painter = painterResource(id = R.drawable.arrow_right), contentDescription = "Redo",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(enabled = redoStack.isNotEmpty()) {
                                val commandToRedo = redoStack.removeLast()
                                commandToRedo.execute()
                                undoStack.add(commandToRedo)
                            })
                }

                // Main Tools
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.text), contentDescription = "Text", modifier = Modifier.size(18.dp).clickable { isAddingText = true })
                    Image(painter = painterResource(id = R.drawable.lassotool), contentDescription = "Lasso Tool", modifier = Modifier.size(22.dp).clickable { toolMode = ToolMode.Lasso })
                    Image(painter = painterResource(id = R.drawable.image), contentDescription = "Image", modifier = Modifier.size(22.dp).clickable { imagePickerLauncher.launch("image/*") })
                    Box {
                        Image(painter = painterResource(id = R.drawable.shapeasset), contentDescription = "Shape", modifier = Modifier.size(22.dp).clickable { showShapeMenu.value = true })
                        DropdownMenu(expanded = showShapeMenu.value, onDismissRequest = { showShapeMenu.value = false }) {
                            DropdownMenuItem(onClick = {
                                pendingShapeType = "Circle"
                                colorPickerTarget = ColorPickerTarget.ADD_SHAPE
                                showShapeMenu.value = false
                            }, text = { Text("Circle") })
                            DropdownMenuItem(onClick = {
                                pendingShapeType = "Rectangle"
                                colorPickerTarget = ColorPickerTarget.ADD_SHAPE
                                showShapeMenu.value = false
                            }, text = { Text("Rectangle") })
                            DropdownMenuItem(onClick = {
                                pendingShapeType = "Star"
                                colorPickerTarget = ColorPickerTarget.ADD_SHAPE
                                showShapeMenu.value = false
                            }, text = { Text("Star") })
                            DropdownMenuItem(onClick = {
                                pendingShapeType = "Triangle"
                                colorPickerTarget = ColorPickerTarget.ADD_SHAPE
                                showShapeMenu.value = false
                            }, text = { Text("Triangle") })
                            DropdownMenuItem(onClick = {
                                pendingShapeType = "Hexagon"
                                colorPickerTarget = ColorPickerTarget.ADD_SHAPE
                                showShapeMenu.value = false
                            }, text = { Text("Hexagon") })
                        }
                    }
                    Image(painter = painterResource(id = R.drawable.pencil), contentDescription = "Pencil", modifier = Modifier.size(22.dp).clickable {
                        toolMode = ToolMode.DRAW
                        colorPickerTarget = ColorPickerTarget.DRAW_STROKE
                    })

                    Image(painter = painterResource(id = R.drawable.eraser), contentDescription = "Eraser", modifier = Modifier.size(22.dp).clickable { toolMode = ToolMode.ERASE })
                    Box {
                        val isLayerMenuEnabled = selectedItems.size == 1
                        Image(
                            painter = painterResource(id = R.drawable.layer),
                            contentDescription = "Layer",
                            modifier = Modifier
                                .size(22.dp)
                                .clickable(enabled = isLayerMenuEnabled) { showLayerMenu.value = true },
                            alpha = if (isLayerMenuEnabled) 1f else 0.4f
                        )
                        DropdownMenu(
                            expanded = showLayerMenu.value,
                            onDismissRequest = { showLayerMenu.value = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Bring to Front") },
                                onClick = {
                                    if (isLayerMenuEnabled) {
                                        val item = selectedItems.first()
                                        val allLists = listOf(texts, shapes, imageLayers, groups)
                                        executeCommand(LayeringCommand(item, allLists, LayerDirection.TO_FRONT))
                                    }
                                    showLayerMenu.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Send to Back") },
                                onClick = {
                                    if (isLayerMenuEnabled) {
                                        val item = selectedItems.first()
                                        val allLists = listOf(texts, shapes, imageLayers, groups)
                                        executeCommand(LayeringCommand(item, allLists, LayerDirection.TO_BACK))
                                    }
                                    showLayerMenu.value = false
                                }
                            )
                        }
                    }
                    Image(painter = painterResource(id = R.drawable.dot3), contentDescription = "More", modifier = Modifier.size(22.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        AnimatedVisibility(
            visible = toolMode == ToolMode.DRAW || toolMode == ToolMode.ERASE,
            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (toolMode == ToolMode.DRAW) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painterResource(id = R.drawable.pencil), contentDescription = "Pencil Icon", Modifier.size(24.dp))
                        Text("Draw", fontSize = 12.sp)
                        Slider(
                            value = drawThickness,
                            onValueChange = { drawThickness = it },
                            valueRange = 1f..50f,
                            steps = 49,
                            modifier = Modifier.width(80.dp).height(150.dp).rotate(270f)
                        )
                        Text("${drawThickness.toInt()}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
                if (toolMode == ToolMode.ERASE) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painterResource(id = R.drawable.eraser), contentDescription = "Eraser Icon", Modifier.size(24.dp))
                        Text("Erase", fontSize = 12.sp)
                        Slider(
                            value = eraseThickness,
                            onValueChange = { eraseThickness = it },
                            valueRange = 10f..100f,
                            steps = 90,
                            modifier = Modifier.width(70.dp).height(150.dp).rotate(270f)
                        )
                        Text("${eraseThickness.toInt()}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            BottomBarScribble(navController = navController)
        }
    }
}

@Composable
fun RenderMovableItem(
    item: Movable,
    isSelected: Boolean,
    isGrouped: Boolean = false,
    groupOffset: Offset = Offset.Zero,
    groupRotation: Float = 0f,
    onSelect: () -> Unit,
    executeCommand: (Command) -> Unit
) {
    val finalOffset = if (isGrouped) groupOffset else item.offset
    val finalRotation = if (isGrouped) groupRotation else item.rotation

    val modifier = Modifier
        .graphicsLayer {
            translationX = finalOffset.x
            translationY = finalOffset.y
            rotationZ = finalRotation

            if (isGrouped) {
                translationX += item.offset.x
                translationY += item.offset.y
                rotationZ += item.rotation
            }
        }
        .clickable(enabled = !isGrouped) { onSelect() }
        .then(if (isSelected && !isGrouped) Modifier.border(2.dp, Color.Blue) else Modifier)

    Box(modifier = modifier) {
        when (item) {
            is EditableText -> {
                Text(
                    text = item.text,
                    color = item.color,
                    fontSize = item.fontSize.sp,
                    modifier = Modifier.padding(4.dp),
                    onTextLayout = { textLayoutResult ->
                        val newSize = Size(
                            width = textLayoutResult.size.width.toFloat(),
                            height = textLayoutResult.size.height.toFloat()
                        )
                        if (item.size != newSize) {
                            item.size = newSize
                        }
                    }
                )
            }
            is ShapeItem -> {
                Box(modifier = Modifier.size(item.size.width.dp, item.size.height.dp)) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val shapeCenter = Offset(size.width / 2f, size.height / 2f)
                        val shapeRadius = size.width / 2f
                        when (item.type) {
                            "Circle" -> drawCircle(item.color, radius = shapeRadius, center = shapeCenter)
                            "Star" -> drawStar(center = shapeCenter, radius = shapeRadius, color = item.color)
                            "Rectangle" -> drawRect(color = item.color, size = this.size)
                            "Triangle" -> drawPolygon(sides = 3, radius = shapeRadius, center = shapeCenter, color = item.color)
                            "Hexagon" -> drawPolygon(sides = 6, radius = shapeRadius, center = shapeCenter, color = item.color)
                        }
                    }
                }
            }
            is ImageLayer -> {
                val context = LocalContext.current
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(item.uri)
                        .size(coil.size.Size.ORIGINAL)
                        .build(),
                    onSuccess = { result ->
                        if (item.size == Size(100f, 100f)) {
                            val drawable = result.result.drawable
                            val intrinsicWidth = drawable.intrinsicWidth
                            val intrinsicHeight = drawable.intrinsicHeight
                            if (intrinsicHeight > 0) {
                                val aspectRatio = intrinsicWidth / intrinsicHeight.toFloat()
                                val newWidth = 300f
                                item.size = Size(newWidth, newWidth / aspectRatio)
                            }
                        }
                    }
                )
                Image(
                    painter = painter,
                    contentDescription = "Image Layer",
                    modifier = Modifier.size(item.size.width.dp, item.size.height.dp)
                )
            }
        }

        if (isSelected && !isGrouped) {
            InteractionHandles(item = item, executeCommand = executeCommand)
        }
    }
}


@Composable
fun GroupHandles(
    group: ItemGroup,
    isSelected: Boolean,
    onMoveStart: (Offset) -> Unit,
    onMoveEnd: (Offset) -> Unit,
    onRotateStart: (Float) -> Unit,
    onRotateEnd: (Float) -> Unit,
    onSelect: () -> Unit
) {
    if (isSelected) {
        val groupBounds = calculateGroupBounds(group)
        val density = LocalDensity.current

        Box(
            modifier = Modifier
                .size(
                    width = (groupBounds.width / density.density).dp,
                    height = (groupBounds.height / density.density).dp
                )
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
                .border(2.dp, Color.Cyan)
        )
    }

    val handleModifier = Modifier.size(24.dp).border(1.dp, Color.White, CircleShape)

    Box(
        modifier = Modifier
            .offset { IntOffset(group.offset.x.toInt() - 12, group.offset.y.toInt() - 12) }
            .then(handleModifier)
            .background(Color.Cyan.copy(alpha = 0.8f), CircleShape)
            .pointerInput(group) {
                detectDragGestures(
                    onDragStart = { onMoveStart(group.offset) },
                    onDrag = { _, dragAmount -> group.offset += dragAmount },
                    onDragEnd = { onMoveEnd(group.offset) }
                )
            }
    )

    Box(
        modifier = Modifier
            .offset { IntOffset((group.offset.x + 80).toInt(), group.offset.y.toInt() - 12) }
            .then(handleModifier)
            .background(Color.Magenta.copy(alpha = 0.8f), CircleShape)
            .pointerInput(group) {
                detectDragGestures(
                    onDragStart = { onRotateStart(group.rotation) },
                    onDrag = { _, dragAmount -> group.rotation += dragAmount.x },
                    onDragEnd = { onRotateEnd(group.rotation) }
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
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 12.dp, y = 12.dp)
                .size(24.dp)
                .background(Color.Blue, CircleShape)
                .border(1.dp, Color.White, CircleShape)
                .pointerInput(item) {
                    detectDragGestures(
                        onDragStart = { dragStartSize = size },
                        onDrag = { _, dragAmount ->
                            val newSize = Size(
                                width = (size.width + dragAmount.x).coerceAtLeast(50f),
                                height = (size.height + dragAmount.y).coerceAtLeast(50f)
                            )
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

    Box(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .offset(x = 12.dp, y = (-12).dp)
            .size(24.dp)
            .background(Color.Magenta, CircleShape)
            .border(1.dp, Color.White, CircleShape)
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
    onShowColorPicker: () -> Unit
) {

    Surface(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 120.dp),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (selectedItems.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Actions:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = {
                        if (selectedItems.size > 1) {
                            executeCommand(GroupCommand(selectedItems.toList(), allLists.take(3), allLists[3] as MutableList<ItemGroup>))
                        }
                    }) { Icon(Icons.Default.Workspaces, "Group") }

                    IconButton(onClick = {
                        executeCommand(CopyCommand(selectedItems.toList(), allLists))
                    }) { Icon(Icons.Default.ContentCopy, "Copy") }

                    IconButton(onClick = {
                        executeCommand(DeleteItemsCommand(selectedItems.toList(), allLists))
                    }) { Icon(Icons.Default.Delete, "Delete", tint = Color.Red) }
                }
            }

            val allColorables = selectedItems.filterIsInstance<Colorable>() + selectedPaths
            if (allColorables.isNotEmpty()) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Button(onClick = {
                    onShowColorPicker()
                }) {
                    Icon(Icons.Default.Palette, contentDescription = "Change Color")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Change Color")
                }
            }
        }
    }
}

/**
 * A dialog that allows the user to pick a color using RGB sliders,
 * based on the provided UI screenshot. It features a color preview,
 * gradient sliders, and a text field for direct numeric input.
 */
@Composable
fun ColorPickerDialog(
    initialColor: Color,
    onDismissRequest: () -> Unit,
    onColorSelected: (Color) -> Unit
) {
    var red by remember { mutableFloatStateOf(initialColor.red * 255f) }
    var green by remember { mutableFloatStateOf(initialColor.green * 255f) }
    var blue by remember { mutableFloatStateOf(initialColor.blue * 255f) }

    val currentColor = Color(red / 255f, green / 255f, blue / 255f)

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Color") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Preview Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                        .background(currentColor)
                )
                // Color Sliders
                ColorInputRow(label = "R", sliderColor = Color.Red, value = red, onValueChange = { red = it })
                ColorInputRow(label = "G", sliderColor = Color.Green, value = green, onValueChange = { green = it })
                ColorInputRow(label = "B", sliderColor = Color.Blue, value = blue, onValueChange = { blue = it })
            }
        },
        confirmButton = {
            Button(onClick = { onColorSelected(currentColor) }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) { Text("Cancel") }
        }
    )
}

/**
 * A helper Composable for a single color input row (e.g., for Red).
 * It contains a label, a custom gradient slider, and an OutlinedTextField
 * for direct numeric input (0-255).
 */
@OptIn(ExperimentalMaterial3Api::class) // ✅ FIX 1: Corrected typo from ...3ai to ...3Api
@Composable
private fun ColorInputRow(
    label: String,
    sliderColor: Color,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    var textValue by remember { mutableStateOf(value.toInt().toString()) }

    // Update text when the slider value changes (e.g., from dragging)
    LaunchedEffect(value) {
        if (value.toInt().toString() != textValue.ifEmpty { "0" }) {
            textValue = value.toInt().toString()
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(label, color = sliderColor, fontWeight = FontWeight.Bold)

        // Custom Slider with Gradient Background
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            // 1. The gradient background track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color.Black, sliderColor)
                        )
                    )
            )
            // 2. The invisible slider on top that provides the thumb and logic
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 0f..255f,
                steps = 254,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Text Field for numeric input
        OutlinedTextField(
            value = textValue,
            onValueChange = { newText: String ->
                if (newText.isEmpty()) {
                    textValue = ""
                    onValueChange(0f)
                }
                else if (newText.all { it.isDigit() }) {
                    val intValue = newText.toInt()
                    val clampedValue = intValue.coerceIn(0, 255)
                    textValue = if (intValue != clampedValue) {
                        clampedValue.toString()
                    } else {
                        newText
                    }
                    onValueChange(clampedValue.toFloat())
                }
            },
            modifier = Modifier.width(80.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            shape = RoundedCornerShape(8.dp)
            // ✅ FIX 2: Removed the unsupported 'contentPadding' parameter
        )
    }
}


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Preview
@Composable
fun DrawScribblePreview() {
    val dummyController = rememberNavController()
    DrawScribbleScreen(navController = dummyController)
}