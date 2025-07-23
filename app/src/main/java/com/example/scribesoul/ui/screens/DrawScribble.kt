package com.example.scribesoul.ui.screens

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Workspaces
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
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

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun DrawScribbleScreen(navController: NavController) { // NavController dihapus karena tidak digunakan

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

    fun executeCommand(command: Command) {
        command.execute()
        undoStack.add(command)
        redoStack.clear()
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            executeCommand(AddImageCommand(ImageLayer(uri = it, offset = canvasCenter.value), imageLayers))
        }
    }

    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(toolMode) {
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
                                    if (drawablePath.toolMode != ToolMode.Highlighter) {
                                        val remainingOffsets = drawablePath.offsets.filter { point ->
                                            currentPath.none { eraserPoint -> distance(point, eraserPoint) < 40f }
                                        }
                                        if (remainingOffsets.isNotEmpty()) {
                                            pathsAfterErase.add(drawablePath.copy(offsets = remainingOffsets))
                                        }
                                    } else {
                                        pathsAfterErase.add(drawablePath)
                                    }
                                }
                                executeCommand(EraseCommand(originalPaths, pathsAfterErase, paths))
                            }
                            ToolMode.Lasso -> {
                                if (currentPath.size > 2) {
                                    val polygon = currentPath.toList()
                                    selectedItems.clear()
                                    val allMovables = texts + shapes + imageLayers + groups
                                    allMovables.forEach { item ->
                                        if (isMovableInPolygon(item, polygon, density)) {
                                            selectedItems.add(item)
                                        }
                                    }
                                }
                            }
                            else -> {
                                if (currentPath.isNotEmpty()) {
                                    executeCommand(AddDrawableCommand(DrawablePath(currentPath.toList(), toolMode), paths))
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
            paths.filter { it.toolMode == ToolMode.Highlighter }.forEach { drawPathFromOffsets(it.offsets, it.toolMode) }
            paths.filter { it.toolMode != ToolMode.Highlighter }.forEach { drawPathFromOffsets(it.offsets, it.toolMode) }
            drawPathFromOffsets(currentPath, toolMode)
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
                            onSelect = {}, // Item dalam grup tidak bisa dipilih
                            executeCommand = ::executeCommand // Parameter harus tetap diberikan
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
        if (selectedItems.isNotEmpty()) {
            Box(modifier = Modifier.align(Alignment.TopCenter).padding(top = 120.dp)) {
                SelectionToolbar(
                    onGroup = {
                        if (selectedItems.size > 1) {
                            val allLists = listOf(texts, shapes, imageLayers)
                            executeCommand(GroupCommand(selectedItems.toList(), allLists, groups))
                            selectedItems.clear()
                        }
                    },
                    onCopy = {
                        val allLists = listOf(texts, shapes, imageLayers, groups)
                        executeCommand(CopyCommand(selectedItems.toList(), allLists))
                        selectedItems.clear()
                    },
                    onDelete = {
                        val allLists = listOf(texts, shapes, imageLayers, groups)
                        executeCommand(DeleteItemsCommand(selectedItems.toList(), allLists))
                        selectedItems.clear()
                    }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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

            Spacer(modifier = Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.text), contentDescription = "Text", modifier = Modifier.size(18.dp).clickable { isAddingText = true })
                Image(painter = painterResource(id = R.drawable.lassotool), contentDescription = "Lasso Tool", modifier = Modifier.size(22.dp).clickable { toolMode = ToolMode.Lasso })
                Image(painter = painterResource(id = R.drawable.image), contentDescription = "Image", modifier = Modifier.size(22.dp).clickable { imagePickerLauncher.launch("image/*") })
                Box {
                    Image(painter = painterResource(id = R.drawable.shapeasset), contentDescription = "Shape", modifier = Modifier.size(22.dp).clickable { showShapeMenu.value = true })
                    DropdownMenu(expanded = showShapeMenu.value, onDismissRequest = { showShapeMenu.value = false }) {
                        DropdownMenuItem(onClick = { executeCommand(AddShapeCommand(ShapeItem("Circle", canvasCenter.value), shapes)); showShapeMenu.value = false },
                            text = { Row { Icon(Icons.Filled.RadioButtonUnchecked, null); Spacer(Modifier.width(8.dp)); Text("Circle") } })
                        DropdownMenuItem(onClick = { executeCommand(AddShapeCommand(ShapeItem("Star", canvasCenter.value), shapes)); showShapeMenu.value = false },
                            text = { Row { Icon(Icons.Filled.Star, null); Spacer(Modifier.width(8.dp)); Text("Star") } })
                        DropdownMenuItem(onClick = { executeCommand(AddShapeCommand(ShapeItem("Rectangle", canvasCenter.value), shapes)); showShapeMenu.value = false },
                            text = { Row { Icon(Icons.Filled.PlayArrow, null); Spacer(Modifier.width(8.dp)); Text("Rectangle") } })
                    }
                }
                Image(painter = painterResource(id = R.drawable.pencil), contentDescription = "Pencil", modifier = Modifier.size(22.dp).clickable { toolMode = ToolMode.DRAW })
                Image(painter = painterResource(id = R.drawable.eraser), contentDescription = "Eraser", modifier = Modifier.size(22.dp).clickable { toolMode = ToolMode.ERASE })
                Box {
                    Image(painter = painterResource(id = R.drawable.layer), contentDescription = "Layer", modifier = Modifier.size(22.dp).clickable { showLayerMenu.value = true })
                    DropdownMenu(expanded = showLayerMenu.value, onDismissRequest = { showLayerMenu.value = false }) {
                        DropdownMenuItem(text = { Text("Bring to Front") }, onClick = { /* TODO */ showLayerMenu.value = false })
                        DropdownMenuItem(text = { Text("Send to Back") }, onClick = { /* TODO */ showLayerMenu.value = false })
                    }
                }
                Image(painter = painterResource(id = R.drawable.dot3), contentDescription = "More", modifier = Modifier.size(22.dp))
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {

            BottomBarScribble(
                navController = navController,
            )
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

    var dragStartSize by remember { mutableStateOf(Size.Zero) }

    // Modifier dasar untuk transformasi dan seleksi
    var modifier = Modifier
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

    // Tambahkan border jika terpilih (dan bukan bagian dari grup)
    if (isSelected && !isGrouped) {
        modifier = modifier.border(2.dp, Color.Blue)
    }

    // Box pembungkus untuk menampung item dan handle-nya jika ada
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
                Box(modifier = Modifier.size(100.dp)) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        when (item.type) {
                            "Circle" -> drawCircle(Color.Red, radius = 50f, center = Offset(50f, 50f))
                            "Star" -> drawStar(center = Offset(50f, 50f), radius = 50f, color = Color.Yellow)
                            "Rectangle" -> drawRect(color = Color.Magenta, size = this.size)
                        }
                    }
                }
            }
            is ImageLayer -> {
                val context = LocalContext.current
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(item.uri)
                        .size(coil.size.Size.ORIGINAL) // Muat ukuran asli untuk kalkulasi
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

                if (isSelected && !isGrouped) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 12.dp, y = 12.dp)
                            .size(24.dp)
                            .background(Color.Blue, CircleShape)
                            .border(1.dp, Color.White, CircleShape)
                            .pointerInput(item) {
                                detectDragGestures(
                                    onDragStart = {
                                        dragStartSize = item.size
                                    },
                                    onDrag = { _, dragAmount ->
                                        item.size = Size(
                                            width = (item.size.width + dragAmount.x).coerceAtLeast(50f),
                                            height = (item.size.height + dragAmount.y).coerceAtLeast(50f)
                                        )
                                    },
                                    onDragEnd = {
                                        if (dragStartSize != item.size) {
                                            executeCommand(ResizeCommand(item, dragStartSize, item.size))
                                        }
                                    }
                                )
                            }
                    )
                }
            }
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
        Box(
            modifier = Modifier
                .offset { IntOffset(groupBounds.left.toInt(), groupBounds.top.toInt()) }
                .size(width = (groupBounds.width / LocalDensity.current.density).dp, height = (groupBounds.height / LocalDensity.current.density).dp)
                .border(2.dp, Color.Cyan)
                .clickable { onSelect() }
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
fun SelectionToolbar(
    onGroup: () -> Unit,
    onCopy: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onGroup) {
                Icon(Icons.Default.Workspaces, contentDescription = "Group Items")
            }
            IconButton(onClick = onCopy) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Copy Items")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Items", tint = Color.Red)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Preview
@Composable
fun DrawScribblePreview() {
    val dummyController = rememberNavController() // <-- Tambahkan kembali
    DrawScribbleScreen(navController = dummyController) // <-- Tambahkan kembali
}