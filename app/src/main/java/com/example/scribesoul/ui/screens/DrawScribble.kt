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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.SolidColor as SolidColorBrush
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
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
import kotlin.math.absoluteValue
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

@OptIn(ExperimentalMaterial3Api::class)
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
    val showTextEditor = remember { mutableStateOf(false) }
    var editingText: EditableText? by remember { mutableStateOf(null) }
    var editingValue by remember { mutableStateOf("") }
    var editingFontSize by remember { mutableStateOf(18f) }


    // Preview state (important: mutableStateOf so Compose re-renders)
    var currentShape by remember { mutableStateOf<ShapeItem?>(null) }
    var dragStart by remember { mutableStateOf<Offset?>(null) }
    var pendingShapeFill by remember { mutableStateOf<FillStyle?>(null) }
    val allMovables = remember(texts, shapes, imageLayers, groups) { texts + shapes + imageLayers + groups }
    val individualItems = texts + shapes + imageLayers

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

    // Color/Gradient pickers (left as-is)
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
                            pendingShapeFill = SolidColorFill(selectedColor)
                            // set the tool mode so next drag will create shape at click
                            toolMode = ToolMode.SHAPE
                            // Do NOT add to shapes here — we only set pending fill and shape type.
                        }
                    }
                    null -> {}
                }
                colorPickerTarget = null
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
                if (toolMode != ToolMode.TEXT) {
                    detectDragGestures(
                        onDragStart = { startPoint ->
                            // common: start a free path for pen/highlighter/eraser
                            currentPath.clear()
                            currentPath.add(startPoint)

                            when (toolMode) {
                                ToolMode.SHAPE -> {
                                    // Prepare preview shape (do NOT add to shapes yet)
                                    dragStart = startPoint
                                    val fillForShape = pendingShapeFill ?: SolidColorFill(drawColor)
                                    currentShape = ShapeItem(
                                        type = pendingShapeType ?: "Rectangle",
                                        offset = startPoint, // we treat offset as top-left during preview
                                        fill = fillForShape,
                                        size = Size(1f, 1f),
                                        cornerRadius = 0f
                                    )
                                }

                                ToolMode.ERASE -> {
                                    // nothing extra here on start
                                }

                                else -> {
                                    // other tools keep existing behavior: gather path points
                                }
                            }
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            when (toolMode) {
                                ToolMode.SHAPE -> {
                                    // update preview from dragStart -> current position
                                    val start = dragStart ?: return@detectDragGestures
                                    val end = change.position
                                    val topLeft =
                                        Offset(minOf(start.x, end.x), minOf(start.y, end.y))
                                    val newSize = Size(
                                        (end.x - start.x).absoluteValue.coerceAtLeast(1f),
                                        (end.y - start.y).absoluteValue.coerceAtLeast(1f)
                                    )
                                    // use copy to ensure state object changes so Compose recomposes
                                    currentShape =
                                        currentShape?.copy(offset = topLeft, size = newSize)
                                }

                                ToolMode.ERASE -> {
                                    val point = change.position
                                    currentPath.add(point)

                                    // ✅ Real-time erase logic
                                    // 1. Remove intersecting SHAPES immediately
                                    val shapesToRemove = shapes.filter {
                                        shapeIntersectsPoint(it, point, eraseThickness)
                                    }
                                    if (shapesToRemove.isNotEmpty()) {
                                        shapes.removeAll(shapesToRemove)
                                        undoStack.add(
                                            DeleteItemsCommand(
                                                shapesToRemove,
                                                listOf(texts, shapes, imageLayers, groups)
                                            )
                                        )
                                        redoStack.clear()
                                    }

                                    // 2. Erase intersecting PATHS immediately
                                    val updatedPaths = mutableListOf<DrawablePath>()
                                    paths.forEach { drawablePath ->
                                        if (drawablePath.toolMode == ToolMode.Highlighter) {
                                            // skip highlighters (optional)
                                            updatedPaths.add(drawablePath)
                                            return@forEach
                                        }

                                        // keep only the parts that aren’t “erased”
                                        val remainingSegments = mutableListOf<Offset>()
                                        var segment = mutableListOf<Offset>()

                                        drawablePath.offsets.forEach { p ->
                                            val erased = distance(p, point) < eraseThickness
                                            if (!erased) {
                                                segment.add(p)
                                            } else {
                                                if (segment.size > 1) {
                                                    remainingSegments.addAll(segment)
                                                    remainingSegments.add(Offset.Unspecified) // marker for split
                                                }
                                                segment.clear()
                                            }
                                        }

                                        if (segment.size > 1) {
                                            remainingSegments.addAll(segment)
                                        }

                                        // Rebuild multiple mini paths if the path got split
                                        if (remainingSegments.isNotEmpty()) {
                                            var temp = mutableListOf<Offset>()
                                            for (offset in remainingSegments) {
                                                if (offset == Offset.Unspecified) {
                                                    if (temp.size > 1) updatedPaths.add(
                                                        drawablePath.copy(
                                                            offsets = temp.toList()
                                                        )
                                                    )
                                                    temp = mutableListOf()
                                                } else {
                                                    temp.add(offset)
                                                }
                                            }
                                            if (temp.size > 1) updatedPaths.add(
                                                drawablePath.copy(
                                                    offsets = temp.toList()
                                                )
                                            )
                                        }
                                    }

                                    // Replace existing paths with updated versions (real-time)
                                    paths.clear()
                                    paths.addAll(updatedPaths)
                                }

                                else -> {
                                    // Freehand drawing: collect points
                                    currentPath.add(change.position)
                                }
                            }
                        },
                        onDragEnd = {
                            when (toolMode) {
                                ToolMode.SHAPE -> {
                                    // finalize preview into real shape (undoable)
                                    currentShape?.let { shp ->
                                        // enforce minimum size
                                        val finalSize = Size(
                                            shp.size.width.coerceAtLeast(2f),
                                            shp.size.height.coerceAtLeast(2f)
                                        )
                                        val finalShape = shp.copy(size = finalSize)
                                        executeCommand(AddShapeCommand(finalShape, shapes))
                                    }
                                    // reset preview state
                                    currentShape = null
                                    dragStart = null
                                    // Optionally reset pendingShapeType/pendingShapeFill if you want single-use
                                    // pendingShapeType = null; pendingShapeFill = null
                                }

                                ToolMode.ERASE -> {
                                    currentPath.clear()
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
                                            if (path.offsets.any { point ->
                                                    isPointInPolygon(
                                                        point,
                                                        polygon
                                                    )
                                                }) {
                                                selectedPaths.add(path)
                                            }
                                        }
                                    }
                                    currentPath.clear()
                                }

                                else -> {
                                    // Freehand finalize
                                    if (currentPath.isNotEmpty()) {
                                        val newPath = DrawablePath(
                                            offsets = currentPath.toList(),
                                            toolMode = toolMode,
                                            thickness = drawThickness,
                                            fill = SolidColorFill(
                                                if (toolMode == ToolMode.Highlighter) Color.Yellow.copy(
                                                    alpha = 0.5f
                                                ) else drawColor
                                            )
                                        )
                                        executeCommand(AddDrawableCommand(newPath, paths))
                                    }
                                    currentPath.clear()
                                }
                            }
                        }
                    )
                }
            }
            .pointerInput(toolMode) {
                if (toolMode == ToolMode.TEXT) {
                    detectTapGestures { offset ->
                        if (toolMode == ToolMode.TEXT) {

                            val newText = EditableText(
                                text = "",
                                offset = offset,
                                fontSize = 28
                            )

                            texts.add(newText)


                            editingText = newText
                            editingValue = ""
                            editingFontSize = 28f
                            newText.isEditing = true
                            showTextEditor.value = true
                        }

                    }
                }
//                detectTapGestures { offset ->
//                    if (isAddingText) {
//                        executeCommand(AddTextCommand(EditableText(text = "New Text", offset = offset), texts))
//                        isAddingText = false
//                    }
////                    texts.forEach { it.isEditing = false }
//                }
            }
    ) {
        // Canvas area
        Canvas(modifier = Modifier.fillMaxSize()) {
            // draw freehand paths
            paths.forEach { path ->
                drawPathFromFill(path.offsets, path.fill, path.toolMode, path.thickness)
            }




            // draw stored shapes (top-left offset)
            shapes.forEach { shape ->
                val brush = when (val fill = shape.fill) {
                    is SolidColorFill -> Brush.verticalGradient(listOf(fill.color, fill.color)) // solid via gradient brush
                    is LinearGradientFill -> Brush.linearGradient(colors = fill.colors)
                    is RadialGradientFill -> Brush.radialGradient(colors = fill.colors)
                    else -> SolidColor(Color.Black)
                }

                when (shape.type) {
                    "Star" -> drawStar(
                        brush = brush,
                        center = Offset(shape.offset.x + shape.size.width / 2, shape.offset.y + shape.size.height / 2),
                        radius = maxOf(shape.size.width, shape.size.height) / 2f
                    )
                    "Rectangle" -> drawRect(
                        brush = brush,
                        topLeft = shape.offset,
                        size = shape.size
                    )
                    "Circle" -> drawOval(
                        brush = brush,
                        topLeft = shape.offset,
                        size = shape.size
                    )
                    "Triangle" -> drawPolygon(
                        sides = 3,
                        brush = brush,
                        center = Offset(shape.offset.x + shape.size.width / 2, shape.offset.y + shape.size.height / 2),
                        radius = maxOf(shape.size.width, shape.size.height) / 2f
                    )
                    "Hexagon" -> drawPolygon(
                        sides = 6,
                        brush = brush,
                        center = Offset(shape.offset.x + shape.size.width / 2, shape.offset.y + shape.size.height / 2),
                        radius = maxOf(shape.size.width, shape.size.height) / 2f
                    )
                    else -> {
                        // fallback rectangle
                        drawRect(brush = brush, topLeft = shape.offset, size = shape.size)
                    }
                }
            }


            // draw current preview shape (not yet added to shapes)
            currentShape?.let { shape ->
                // semi-transparent preview to indicate it's a temporary preview
                val previewBrush = when (val fill = shape.fill) {
                    is SolidColorFill -> SolidColor(fill.color.copy(alpha = 0.45f))
                    is LinearGradientFill -> Brush.linearGradient(fill.colors)
                    is RadialGradientFill -> Brush.radialGradient(fill.colors)
                    else -> SolidColor(Color.Black.copy(alpha = 0.45f))
                }

                when (shape.type) {
                    "Rectangle" -> drawRect(
                        brush = previewBrush,
                        topLeft = shape.offset,
                        size = shape.size,
                        style = Fill
                    )
                    "Circle" -> drawOval(
                        brush = previewBrush,
                        topLeft = shape.offset,
                        size = shape.size,
                        style = Fill
                    )
                    "Star" -> drawStar(
                        brush = previewBrush,
                        center = Offset(shape.offset.x + shape.size.width / 2, shape.offset.y + shape.size.height / 2),
                        radius = maxOf(shape.size.width, shape.size.height) / 2f
                    )
                    "Triangle" -> drawPolygon(
                        sides = 3,
                        brush = previewBrush,
                        center = Offset(shape.offset.x + shape.size.width / 2, shape.offset.y + shape.size.height / 2),
                        radius = maxOf(shape.size.width, shape.size.height) / 2f
                    )
                    "Hexagon" -> drawPolygon(
                        sides = 6,
                        brush = previewBrush,
                        center = Offset(shape.offset.x + shape.size.width / 2, shape.offset.y + shape.size.height / 2),
                        radius = maxOf(shape.size.width, shape.size.height) / 2f
                    )
                    else -> drawRect(brush = previewBrush, topLeft = shape.offset, size = shape.size, style = Fill)
                }
            }

            // draw current freehand path while drawing
            if (toolMode != ToolMode.ERASE && toolMode != ToolMode.Lasso) {
                drawPathFromFill(currentPath, SolidColorFill(drawColor), toolMode, drawThickness)
            }

            if (toolMode == ToolMode.Lasso && currentPath.isNotEmpty()) {
                val lassoPath = Path().apply {
                    moveTo(currentPath.first().x, currentPath.first().y)
                    for (point in currentPath.drop(1)) {
                        lineTo(point.x, point.y)
                    }
                }

                // Optional: close the path if the user finishes the loop
                if (currentPath.size > 2) {
                    lassoPath.close()
                }

                drawPath(
                    path = lassoPath,
                    color = Color.Black,
                    style = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                    )
                )
            }




            // selection borders, eraser cursor, guides, etc.
            selectedPaths.forEach { drawSelectionBorder(it.offsets) }
            if (toolMode == ToolMode.ERASE && currentPath.isNotEmpty()) {
                val lastPoint = currentPath.last()
                drawCircle(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    center = lastPoint,
                    radius = eraseThickness,
                    style = Stroke(width = 2.dp.toPx())
                )
            }
            guideLines.forEach { line ->
                drawLine(color = Color.Cyan, start = line.start, end = line.end, strokeWidth = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
            }
        }

        // RenderMovableItem usage (unchanged)


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
                    guideLines = guideLines,
                    onUpdate = {
                            newItem ->
                        when (newItem) {
                            is EditableText -> {
                                (item as EditableText).apply {
                                    text = newItem.text
                                    offset = newItem.offset
                                    rotation = newItem.rotation
                                    fill = newItem.fill
                                    fontSize = newItem.fontSize
                                    size = newItem.size
                                }
                            }
                            is ShapeItem -> {
                                val idx = shapes.indexOf(item)
                                if (idx != -1) shapes[idx] = newItem
                            }
                            is ImageLayer -> {
                                val idx = imageLayers.indexOf(item)
                                if (idx != -1) imageLayers[idx] = newItem
                            }
                        }
                    }
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
                            guideLines = null,
                            onUpdate={

                            }
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
                    Image(painter = painterResource(id = R.drawable.text), contentDescription = "Text", modifier = Modifier.size(18.dp).clickable { toolMode = ToolMode.TEXT })
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
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 10.dp)
        ) {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f), RoundedCornerShape(8.dp)).padding(horizontal = 0.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (toolMode == ToolMode.DRAW) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painterResource(id = R.drawable.pencil), contentDescription = "Pencil", Modifier.size(24.dp))
                        Text("Draw", fontSize = 12.sp)
                        Slider(value = drawThickness, onValueChange = { drawThickness = it }, valueRange = 1f..50f, modifier = Modifier.width(70.dp).height(150.dp).rotate(270f))
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
    if (showTextEditor.value && editingText != null) {

        ModalBottomSheet(
            onDismissRequest = {
                editingText?.isEditing = false
                showTextEditor.value = false
            },
            sheetState = rememberModalBottomSheetState()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Edit Text",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                // TEXT INPUT
                OutlinedTextField(
                    value = editingValue,
                    onValueChange = { newValue ->
                        editingValue = newValue
                        editingText?.text = newValue },
                    modifier = Modifier.fillMaxWidth()
                )

                // FONT SIZE SLIDER
                Column {
                    Text("Font Size: ${editingFontSize.toInt()}")
                    Slider(
                        value = editingFontSize,
                        onValueChange = { editingFontSize = it },
                        valueRange = 10f..120f
                    )
                }

                // DONE BUTTON
                Button(
                    onClick = {
                        editingText?.apply {
                            text = editingValue
                            isEditing = false
                        }
                        showTextEditor.value = false
                        editingText = null
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Done")
                }
            }
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
    onUpdate: (Movable) -> Unit,
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
                        val movedItem = when(item) {
                            is ShapeItem -> item.copy(offset = newOffset + snapCorrection)
                            is EditableText -> item.copy(offset = newOffset + snapCorrection)
                            is ImageLayer -> item.copy(offset = newOffset + snapCorrection)
                            else -> item
                        }
                        onUpdate(movedItem)
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
                        onValueChange = { newText ->
                            onUpdate(item.copy(text = newText))
                        },
                        textStyle = textStyle,
                        modifier = Modifier.focusRequester(focusRequester).padding(4.dp).widthIn(min = 50.dp)
                    )
                    LaunchedEffect(Unit) { focusRequester.requestFocus() }
                } else {
                    Text(
                        text = if (item.text.isEmpty()) "" else item.text,
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
                        when (item.type.lowercase()) {
                            "circle" -> drawCircle(brush, radius = minOf(size.width, size.height) / 2f, center = center)
                            "star" -> drawStar(brush, center = center, radius = minOf(size.width, size.height) / 2f)
                            "rectangle" -> drawRoundRect(brush, size = this.size, cornerRadius = CornerRadius(item.cornerRadius, item.cornerRadius))
                            "triangle" -> drawPolygon(3, brush, center = center, radius = minOf(size.width, size.height) / 2f)
                            "hexagon" -> drawPolygon(6, brush, center = center, radius = minOf(size.width, size.height) / 2f)
                            else -> drawRect(brush, size = this.size) // fallback if unknown type
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
            InteractionHandles(item = item, executeCommand = { executeCommand(it) }, onUpdate = onUpdate)
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
fun BoxScope.InteractionHandles(item: Movable, executeCommand: (Command) -> Unit, onUpdate: (Movable) -> Unit) {
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
                            val newWidth = (size.width + dragAmount.x).coerceAtLeast(50f)
                            val newHeight = (size.height + dragAmount.y).coerceAtLeast(50f)
                            val newSize = Size(newWidth, newHeight)

                            val newItem = when (item) {
                                is ShapeItem -> item.copy(size = newSize)
                                is ImageLayer -> item.copy(size = newSize)
                                else -> item
                            }
                            onUpdate(newItem)
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
                            val newRadius = (item.cornerRadius + dragAmount.x)
                                .coerceIn(0f, min(item.size.width, item.size.height) / 2)
                            val newItem = item.copy(cornerRadius = newRadius)
                            onUpdate(newItem)
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
                    onDrag = { _, dragAmount ->
                        val newRotation = item.rotation + dragAmount.x

                        // Create COPY and Update
                        val newItem = when (item) {
                            is ShapeItem -> item.copy(rotation = newRotation)
                            is EditableText -> item.copy(rotation = newRotation)
                            is ImageLayer -> item.copy(rotation = newRotation)
                            is ItemGroup -> item.copy(rotation = newRotation)
                            else -> item
                        }
                        onUpdate(newItem)
                    },
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

private fun shapeIntersectsPoint(shape: ShapeItem, point: Offset, threshold: Float): Boolean {
    // simple bbox-based check; inflate bbox by threshold
    val left = shape.offset.x - threshold
    val top = shape.offset.y - threshold
    val right = shape.offset.x + shape.size.width + threshold
    val bottom = shape.offset.y + shape.size.height + threshold
    return point.x in left..right && point.y in top..bottom
}

private fun shapeIntersectsPath(shape: ShapeItem, path: List<Offset>, threshold: Float): Boolean {
    // return true if any path point intersects the inflated bbox
    val left = shape.offset.x - threshold
    val top = shape.offset.y - threshold
    val right = shape.offset.x + shape.size.width + threshold
    val bottom = shape.offset.y + shape.size.height + threshold
    return path.any { p -> p.x in left..right && p.y in top..bottom }
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




@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Preview
@Composable
fun DrawScribblePreview() {
    val dummyController = rememberNavController()
    DrawScribbleScreen(navController = dummyController)
}