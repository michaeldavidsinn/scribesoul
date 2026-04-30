package com.scribesoul.app.utils

import JournalPage
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.scribesoul.app.commands.*
import com.scribesoul.app.models.*
import com.scribesoul.app.ui.screens.GuideLine
import com.scribesoul.app.viewModels.DrawingViewModel
import kotlin.math.*
import com.scribesoul.app.models.LinearGradient as LinearGradientFill
import com.scribesoul.app.models.RadialGradient as RadialGradientFill
import com.scribesoul.app.models.SolidColor as SolidColorFill

// --- HELPER FUNCTIONS ---

fun distance(p1: Offset, p2: Offset): Float {
    return sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2))
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

fun calculateGroupBounds(group: ItemGroup): Rect {
    if (group.items.isEmpty()) return Rect.Zero
    var minX = Float.MAX_VALUE
    var minY = Float.MAX_VALUE
    var maxX = Float.MIN_VALUE
    var maxY = Float.MIN_VALUE

    group.items.forEach { item ->
        val itemCorners = when (item) {
            is EditableText -> getTransformedCorners(item.offset, item.size, item.rotation)
            is ShapeItem -> getTransformedCorners(item.offset, item.size, item.rotation)
            is ImageLayer -> getTransformedCorners(item.offset, item.size, item.rotation)
            else -> emptyList()
        }
        itemCorners.forEach { corner ->
            minX = minOf(minX, corner.x)
            minY = minOf(minY, corner.y)
            maxX = maxOf(maxX, corner.x)
            maxY = maxOf(maxY, corner.y)
        }
    }
    return Rect(left = minX, top = minY, right = maxX, bottom = maxY)
}

private fun getTransformedCorners(offset: Offset, size: Size, rotationDegrees: Float): List<Offset> {
    val center = Offset(size.width / 2f, size.height / 2f)
    val corners = listOf(
        Offset(0f, 0f),
        Offset(size.width, 0f),
        Offset(size.width, size.height),
        Offset(0f, size.height)
    )
    val rotationRadians = Math.toRadians(rotationDegrees.toDouble()).toFloat()
    val cos = cos(rotationRadians)
    val sin = sin(rotationRadians)

    return corners.map { corner ->
        val cornerRelativeToCenter = corner - center
        val rotatedX = cornerRelativeToCenter.x * cos - cornerRelativeToCenter.y * sin
        val rotatedY = cornerRelativeToCenter.x * sin + cornerRelativeToCenter.y * cos
        Offset(rotatedX, rotatedY) + center + offset
    }
}

fun isMovableInPolygon(movable: Movable, polygon: List<Offset>, density: androidx.compose.ui.unit.Density): Boolean {
    val corners = when (movable) {
        is EditableText -> getTransformedCorners(movable.offset, movable.size, movable.rotation)
        is ShapeItem -> getTransformedCorners(movable.offset, movable.size, movable.rotation)
        is ImageLayer -> getTransformedCorners(movable.offset, movable.size, movable.rotation)
        is ItemGroup -> {
            val groupBounds = calculateGroupBounds(movable)
            getTransformedCorners(movable.offset + groupBounds.topLeft, groupBounds.size, movable.rotation)
        }
        else -> return false
    }
    return corners.any { isPointInPolygon(it, polygon) }
}

fun isPointInPolygon(point: Offset, polygon: List<Offset>): Boolean {
    var crossings = 0
    for (i in polygon.indices) {
        val a = polygon[i]
        val b = polygon[(i + 1) % polygon.size]
        if ((a.y > point.y) != (b.y > point.y)) {
            val atX = (b.x - a.x) * (point.y - a.y) / (b.y - a.y + 1e-5f) + a.x
            if (point.x < atX) crossings++
        }
    }
    return crossings % 2 == 1
}

private fun shapeIntersectsPoint(shape: ShapeItem, point: Offset, threshold: Float): Boolean {
    val left = shape.offset.x - threshold
    val top = shape.offset.y - threshold
    val right = shape.offset.x + shape.size.width + threshold
    val bottom = shape.offset.y + shape.size.height + threshold
    return point.x in left..right && point.y in top..bottom
}

// --- DRAWING HELPERS ---

fun DrawScope.drawPathFromFill(offsets: List<Offset>, fill: FillStyle, mode: ToolMode, thickness: Float) {
    if (offsets.size < 2) return
    val path = Path().apply {
        moveTo(offsets.first().x, offsets.first().y)
        (1 until offsets.size).forEach { lineTo(offsets[it].x, offsets[it].y) }
    }
    val style = Stroke(
        width = if (mode == ToolMode.Highlighter) thickness * 2 else thickness,
        cap = StrokeCap.Round,
        join = StrokeJoin.Round
    )
    val brush = when (fill) {
        is SolidColorFill -> SolidColor(fill.color.copy(alpha = if (mode == ToolMode.Highlighter) 0.4f else fill.color.alpha))
        is LinearGradientFill -> Brush.linearGradient(fill.colors)
        is RadialGradientFill -> Brush.radialGradient(fill.colors)
    }
    drawPath(path = path, brush = brush, style = style)
}

fun DrawScope.drawSelectionBorder(offsets: List<Offset>) {
    if (offsets.isEmpty()) return
    var minX = Float.MAX_VALUE
    var minY = Float.MAX_VALUE
    var maxX = Float.MIN_VALUE
    var maxY = Float.MIN_VALUE
    offsets.forEach { minX = minOf(minX, it.x); minY = minOf(minY, it.y); maxX = maxOf(maxX, it.x); maxY = maxOf(maxY, it.y) }
    drawRect(
        Color.Blue,
        Offset(minX, minY),
        Size(maxX - minX, maxY - minY),
        style = Stroke(width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f))
    )
}

fun DrawScope.drawStar(brush: Brush, center: Offset, radius: Float) {
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

fun DrawScope.drawPolygon(sides: Int, brush: Brush, center: Offset, radius: Float) {
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

// --- MAIN COMPOSABLE ---

@Composable
fun DrawCanvas(
    drawingViewModel: DrawingViewModel,
    page: JournalPage
) {
    val currentPath = remember { mutableStateListOf<Offset>() }
    var currentShape by remember { mutableStateOf<ShapeItem?>(null) }
    val density = LocalDensity.current
    var dragStart by remember { mutableStateOf<Offset?>(null) }
    var refreshTrigger by remember { mutableIntStateOf(0) }

    val allMovables = remember(page.texts, page.shapes, page.imageLayers, drawingViewModel.groups) {
        page.texts + page.shapes + page.imageLayers + drawingViewModel.groups
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(
                drawingViewModel.toolMode,
                drawingViewModel.drawThickness,
                drawingViewModel.eraseThickness,
                drawingViewModel.drawColor,
                page // Add page to keys to ensure updates if page ref changes
            ) {
                if (drawingViewModel.toolMode != ToolMode.TEXT) {
                    detectDragGestures(
                        onDragStart = { start ->
                            currentPath.clear()
                            currentPath.add(start)
                            if (drawingViewModel.toolMode == ToolMode.SHAPE) {
                                dragStart = start
                                val fillForShape = drawingViewModel.pendingShapeFill ?: SolidColorFill(drawingViewModel.drawColor)
                                currentShape = ShapeItem(
                                    type = drawingViewModel.pendingShapeType ?: "Rectangle",
                                    offset = start,
                                    fill = fillForShape,
                                    size = Size(1f, 1f),
                                    cornerRadius = 0f
                                )
                            }
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            when (drawingViewModel.toolMode) {
                                ToolMode.SHAPE -> {
                                    val start = dragStart ?: return@detectDragGestures
                                    val end = change.position
                                    val topLeft = Offset(minOf(start.x, end.x), minOf(start.y, end.y))
                                    val newSize = Size(
                                        (end.x - start.x).absoluteValue.coerceAtLeast(1f),
                                        (end.y - start.y).absoluteValue.coerceAtLeast(1f)
                                    )
                                    currentShape = currentShape?.copy(offset = topLeft, size = newSize)
                                }
                                ToolMode.ERASE -> {
                                    val point = change.position
                                    currentPath.add(point)
                                    val shapesToRemove = page.shapes.filter { shapeIntersectsPoint(it, point, drawingViewModel.eraseThickness) }
                                    if (shapesToRemove.isNotEmpty()) {
                                        page.shapes.removeAll(shapesToRemove)
                                        page.undoStack.add(DeleteItemsCommand(shapesToRemove, listOf(page.texts, page.shapes, page.imageLayers, drawingViewModel.groups)))
                                        refreshTrigger++
                                    }

                                    val updatedPaths = mutableListOf<DrawablePath>()
                                    var pathChanged = false
                                    page.paths.forEach { drawablePath ->
                                        val remainingSegments = mutableListOf<Offset>()
                                        var segment = mutableListOf<Offset>()
                                        var wasErased = false
                                        drawablePath.offsets.forEach { p ->
                                            if (distance(p, point) < drawingViewModel.eraseThickness) {
                                                wasErased = true
                                                if (segment.size > 1) {
                                                    remainingSegments.addAll(segment)
                                                    remainingSegments.add(Offset.Unspecified)
                                                }
                                                segment.clear()
                                            } else {
                                                segment.add(p)
                                            }
                                        }
                                        if (segment.size > 1) remainingSegments.addAll(segment)

                                        if (wasErased) {
                                            pathChanged = true
                                            var temp = mutableListOf<Offset>()
                                            for (offset in remainingSegments) {
                                                if (offset == Offset.Unspecified) {
                                                    if (temp.size > 1) updatedPaths.add(drawablePath.copy(offsets = temp.toList()))
                                                    temp = mutableListOf()
                                                } else {
                                                    temp.add(offset)
                                                }
                                            }
                                            if (temp.size > 1) updatedPaths.add(drawablePath.copy(offsets = temp.toList()))
                                        } else {
                                            updatedPaths.add(drawablePath)
                                        }
                                    }

                                    if (pathChanged) {
                                        page.paths.clear()
                                        page.paths.addAll(updatedPaths)
                                        refreshTrigger++
                                    }
                                }
                                else -> currentPath.add(change.position)
                            }
                        },
                        onDragEnd = {
                            if (drawingViewModel.toolMode == ToolMode.SHAPE) {
                                currentShape?.let { shp ->
                                    val finalSize = Size(shp.size.width.coerceAtLeast(2f), shp.size.height.coerceAtLeast(2f))
                                    val finalShape = shp.copy(size = finalSize)
                                    drawingViewModel.executeCommand(AddShapeCommand(finalShape, page.shapes), page)
                                }
                                currentShape = null
                            } else if (drawingViewModel.toolMode == ToolMode.Lasso) {
                                if (currentPath.size > 2) {
                                    val polygon = currentPath.toList()
                                    drawingViewModel.selectedItems.clear()
                                    drawingViewModel.selectedPaths.clear()

                                    // FIX: Get FRESH list of items so we check against their CURRENT position
                                    val freshMovables = page.texts + page.shapes + page.imageLayers + drawingViewModel.groups

                                    freshMovables.forEach { item ->
                                        if (isMovableInPolygon(item, polygon, density)) drawingViewModel.selectedItems.add(item)
                                    }

                                    page.paths.forEach { path ->
                                        // Optimization: check every 5th point to save performance
                                        val isSelected = path.offsets.filterIndexed { index, _ -> index % 5 == 0 }
                                            .any { point -> isPointInPolygon(point, polygon) }

                                        if (isSelected) {
                                            drawingViewModel.selectedPaths.add(path)
                                        }
                                    }
                                }
                            } else if (drawingViewModel.toolMode == ToolMode.DRAW || drawingViewModel.toolMode == ToolMode.Highlighter) {
                                if (currentPath.size > 1) {
                                    val newPath = DrawablePath(
                                        offsets = currentPath.toList(),
                                        toolMode = drawingViewModel.toolMode,
                                        thickness = drawingViewModel.drawThickness,
                                        fill = SolidColorFill(if (drawingViewModel.toolMode == ToolMode.Highlighter) Color.Yellow.copy(alpha = 0.5f) else drawingViewModel.drawColor)
                                    )
                                    drawingViewModel.executeCommand(AddDrawableCommand(newPath, page.paths), page)
                                }
                            }
                            currentPath.clear()
                        }
                    )
                }
            }
            .pointerInput(drawingViewModel.toolMode) {
                if (drawingViewModel.toolMode == ToolMode.TEXT) {
                    detectTapGestures { offset ->
                        val newText = EditableText(text = "Type here", offset = offset, fontSize = 28, isEditing = true)
                        page.texts.add(newText)
                        drawingViewModel.selectedItems.clear()
                        drawingViewModel.selectedItems.add(newText)
                    }
                }
            }
    ) {
        // --- LAYER 1: BACKGROUND PATHS ---
        Canvas(modifier = Modifier.fillMaxSize()) {
            refreshTrigger.let { _ ->
                page.paths.forEach { path ->
                    drawPathFromFill(path.offsets, path.fill, path.toolMode, path.thickness)
                }
            }
        }

        // --- LAYER 2: INTERACTIVE ITEMS ---

        drawingViewModel.groups.forEachIndexed { index, group ->
            key(index) {
                RenderMovableItem(
                    item = group,
                    isSelected = drawingViewModel.selectedItems.contains(group),
                    onSelect = {
                        drawingViewModel.selectedItems.clear(); drawingViewModel.selectedPaths.clear()
                        drawingViewModel.selectedItems.add(group)
                    },
                    onUpdate = { updated ->
                        drawingViewModel.groups[index] = updated as ItemGroup
                        if(drawingViewModel.selectedItems.contains(group)) {
                            drawingViewModel.selectedItems.remove(group); drawingViewModel.selectedItems.add(updated)
                        }
                    },
                    onDelete = { drawingViewModel.executeCommand(DeleteItemsCommand(listOf(group), listOf(drawingViewModel.groups)), page) },
                    onDoubleClick = {},
                    executeCommand = { cmd -> drawingViewModel.executeCommand(cmd, page) },
                    allItems = allMovables,
                    guideLines = drawingViewModel.guideLines
                )
                GroupHandles(group, drawingViewModel.selectedItems.contains(group)) { cmd -> drawingViewModel.executeCommand(cmd, page) }
            }
        }

        page.shapes.forEachIndexed { index, item ->
            key(index) {
                RenderMovableItem(
                    item = item,
                    isSelected = drawingViewModel.selectedItems.contains(item),
                    onSelect = {
                        drawingViewModel.selectedItems.clear(); drawingViewModel.selectedPaths.clear()
                        drawingViewModel.selectedItems.add(item)
                        page.texts.forEachIndexed { i, t -> if(t.isEditing) page.texts[i] = t.copy(isEditing=false) }
                    },
                    onUpdate = { updated ->
                        page.shapes[index] = updated as ShapeItem
                        if(drawingViewModel.selectedItems.contains(item)) {
                            drawingViewModel.selectedItems.remove(item); drawingViewModel.selectedItems.add(updated)
                        }
                    },
                    onDelete = { drawingViewModel.executeCommand(DeleteItemsCommand(listOf(item), listOf(page.shapes)), page) },
                    onDoubleClick = {},
                    executeCommand = { cmd -> drawingViewModel.executeCommand(cmd, page) },
                    allItems = allMovables,
                    guideLines = drawingViewModel.guideLines
                )
            }
        }

        page.imageLayers.forEachIndexed { index, item ->
            key(index) {
                RenderMovableItem(
                    item = item,
                    isSelected = drawingViewModel.selectedItems.contains(item),
                    onSelect = {
                        drawingViewModel.selectedItems.clear(); drawingViewModel.selectedPaths.clear()
                        drawingViewModel.selectedItems.add(item)
                        page.texts.forEachIndexed { i, t -> if(t.isEditing) page.texts[i] = t.copy(isEditing=false) }
                    },
                    onUpdate = { updated ->
                        page.imageLayers[index] = updated as ImageLayer
                        if(drawingViewModel.selectedItems.contains(item)) {
                            drawingViewModel.selectedItems.remove(item); drawingViewModel.selectedItems.add(updated)
                        }
                    },
                    onDelete = { drawingViewModel.executeCommand(DeleteItemsCommand(listOf(item), listOf(page.imageLayers)), page) },
                    onDoubleClick = {},
                    executeCommand = { cmd -> drawingViewModel.executeCommand(cmd, page) },
                    allItems = allMovables,
                    guideLines = drawingViewModel.guideLines
                )
            }
        }

        page.texts.forEachIndexed { index, item ->
            key(index) {
                RenderMovableItem(
                    item = item,
                    isSelected = drawingViewModel.selectedItems.contains(item),
                    onSelect = {
                        drawingViewModel.selectedItems.clear(); drawingViewModel.selectedPaths.clear()
                        drawingViewModel.selectedItems.add(item)
                        page.texts.forEachIndexed { i, t -> if(i != index && t.isEditing) page.texts[i] = t.copy(isEditing=false) }
                    },
                    onUpdate = { updated ->
                        page.texts[index] = updated as EditableText
                        if(drawingViewModel.selectedItems.contains(item)) {
                            drawingViewModel.selectedItems.remove(item); drawingViewModel.selectedItems.add(updated)
                        }
                    },
                    onDelete = { drawingViewModel.executeCommand(DeleteItemsCommand(listOf(item), listOf(page.texts)), page) },
                    onDoubleClick = {
                        val editing = item.copy(isEditing = true)
                        page.texts[index] = editing
                        drawingViewModel.selectedItems.clear(); drawingViewModel.selectedItems.add(editing)
                        page.texts.forEachIndexed { i, t -> if(i != index) page.texts[i] = t.copy(isEditing=false) }
                    },
                    executeCommand = { cmd -> drawingViewModel.executeCommand(cmd, page) },
                    allItems = allMovables,
                    guideLines = drawingViewModel.guideLines
                )
            }
        }

        // --- LAYER 3: FOREGROUND ---
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (drawingViewModel.toolMode != ToolMode.SHAPE && currentPath.isNotEmpty()) {
                if (drawingViewModel.toolMode == ToolMode.ERASE) {
                    val lastPoint = currentPath.last()
                    drawCircle(Color.Black.copy(alpha = 0.5f), center = lastPoint, radius = drawingViewModel.eraseThickness, style = Stroke(2.dp.toPx()))
                } else if (drawingViewModel.toolMode == ToolMode.Lasso) {
                    val lassoPath = Path().apply {
                        moveTo(currentPath.first().x, currentPath.first().y)
                        currentPath.drop(1).forEach { lineTo(it.x, it.y) }
                    }
                    drawPath(lassoPath, Color.Black, style = Stroke(width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)))
                } else {
                    drawPathFromFill(currentPath, SolidColorFill(drawingViewModel.drawColor), drawingViewModel.toolMode, drawingViewModel.drawThickness)
                }
            }

            drawingViewModel.guideLines.forEach { line ->
                drawLine(Color.Cyan, start = line.start, end = line.end, strokeWidth = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
            }
            drawingViewModel.selectedPaths.forEach { drawSelectionBorder(it.offsets) }

            currentShape?.let { shape ->
                val previewBrush = when (val fill = shape.fill) {
                    is SolidColorFill -> SolidColor(fill.color.copy(alpha = 0.45f))
                    is LinearGradientFill -> Brush.linearGradient(fill.colors)
                    is RadialGradientFill -> Brush.radialGradient(fill.colors)
                    else -> SolidColor(Color.Black.copy(alpha = 0.45f))
                }
                when (shape.type) {
                    "Circle" -> drawOval(brush = previewBrush, topLeft = shape.offset, size = shape.size, style = Fill)
                    "Rectangle" -> drawRect(brush = previewBrush, topLeft = shape.offset, size = shape.size, style = Fill)
                    "Star" -> {
                        val cx = shape.offset.x + shape.size.width/2; val cy = shape.offset.y + shape.size.height/2
                        withTransform({ scale(1f, shape.size.height/shape.size.width, Offset(cx, cy)) }) {
                            drawStar(previewBrush, Offset(cx, cy), shape.size.width/2f)
                        }
                    }
                    "Triangle" -> {
                        val cx = shape.offset.x + shape.size.width/2; val cy = shape.offset.y + shape.size.height/2
                        withTransform({ scale(1f, shape.size.height/shape.size.width, Offset(cx, cy)) }) {
                            drawPolygon(3, previewBrush, Offset(cx, cy), shape.size.width/2f)
                        }
                    }
                    "Hexagon" -> {
                        val cx = shape.offset.x + shape.size.width/2; val cy = shape.offset.y + shape.size.height/2
                        withTransform({ scale(1f, shape.size.height/shape.size.width, Offset(cx, cy)) }) {
                            drawPolygon(6, previewBrush, Offset(cx, cy), shape.size.width/2f)
                        }
                    }
                    else -> drawRect(brush = previewBrush, topLeft = shape.offset, size = shape.size, style = Fill)
                }
            }
        }
    }
}

// --- INTERACTIVE COMPONENTS ---

@Composable
fun RenderMovableItem(
    item: Movable,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onUpdate: (Movable) -> Unit,
    onDelete: () -> Unit,
    onDoubleClick: (Movable) -> Unit,
    executeCommand: (Command) -> Unit,
    isGrouped: Boolean = false,
    groupOffset: Offset = Offset.Zero,
    groupRotation: Float = 0f,
    allItems: List<Movable>?,
    guideLines: MutableList<GuideLine>?
) {
    val currentItem by rememberUpdatedState(item)
    val currentAllItems by rememberUpdatedState(allItems)
    val currentGuideLines by rememberUpdatedState(guideLines)
    val density = LocalDensity.current

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
        .pointerInput(isGrouped, isSelected) {
            if (!isGrouped) {
                detectTapGestures(
                    onTap = { if (isSelected && item is EditableText) onDoubleClick(item) else onSelect() },
                    onDoubleTap = { onDoubleClick(item) }
                )
            }
        }
        .pointerInput(Unit) {
            if (!isGrouped) {
                detectTransformGestures(panZoomLock = false) { _, pan, zoom, rotation ->
                    val capturedItem = currentItem
                    val angleRad = capturedItem.rotation * (PI / 180)
                    val rotX = pan.x * cos(-angleRad) - pan.y * sin(-angleRad)
                    val rotY = pan.x * sin(-angleRad) + pan.y * cos(-angleRad)
                    val rotatedPan = Offset(rotX.toFloat(), rotY.toFloat())
                    val rawNewOffset = capturedItem.offset + rotatedPan

                    var newSize = Size.Zero
                    var newFontSize = 0f
                    if (capturedItem is ShapeItem) newSize = capturedItem.size * zoom
                    else if (capturedItem is ImageLayer) newSize = capturedItem.size * zoom
                    else if (capturedItem is EditableText) newFontSize = capturedItem.fontSize * zoom

                    val (snapCorrection, newGuides) = if (currentAllItems != null)
                        calculateSnapping(capturedItem.copyForDrag(rawNewOffset), currentAllItems!!)
                    else Pair(Offset.Zero, emptyList())
                    currentGuideLines?.clear(); currentGuideLines?.addAll(newGuides)

                    val finalOffset = rawNewOffset + snapCorrection
                    val finalRotation = capturedItem.rotation + rotation

                    val updatedItem = when (capturedItem) {
                        is ShapeItem -> capturedItem.copy(offset = finalOffset, rotation = finalRotation, size = if (newSize != Size.Zero) newSize else capturedItem.size)
                        is EditableText -> capturedItem.copy(offset = finalOffset, rotation = finalRotation, fontSize = if (newFontSize != 0f) newFontSize.toInt() else capturedItem.fontSize)
                        is ImageLayer -> capturedItem.copy(offset = finalOffset, rotation = finalRotation, size = if (newSize != Size.Zero) newSize else capturedItem.size)
                        else -> capturedItem
                    }
                    onUpdate(updatedItem)
                }
            }
        }

    Box(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(IntrinsicSize.Min)) {
            Box(modifier = Modifier.then(if (isSelected && !isGrouped) Modifier.border(2.dp, Color.Blue, RoundedCornerShape(2.dp)) else Modifier)) {
                when (item) {
                    is EditableText -> {
                        val textStyle = TextStyle(
                            brush = when (val fill = item.fill) {
                                is SolidColorFill -> SolidColor(fill.color)
                                is LinearGradientFill -> Brush.linearGradient(fill.colors)
                                is RadialGradientFill -> Brush.radialGradient(fill.colors)
                            },
                            fontSize = item.fontSize.sp,
                            fontWeight = FontWeight.Normal
                        )
                        if (item.isEditing) {
                            val focusRequester = remember { FocusRequester() }
                            BasicTextField(
                                value = item.text,
                                onValueChange = { newText -> onUpdate(item.copy(text = newText)) },
                                textStyle = textStyle,
                                singleLine = false,
                                modifier = Modifier.focusRequester(focusRequester).padding(4.dp).wrapContentWidth(Alignment.Start).widthIn(min = 50.dp)
                            )
                            LaunchedEffect(Unit) { focusRequester.requestFocus() }
                        } else {
                            Text(
                                text = if (item.text.isEmpty()) "Tap to edit" else item.text,
                                style = textStyle,
                                modifier = Modifier.padding(4.dp),
                                onTextLayout = { item.size = Size(it.size.width.toFloat(), it.size.height.toFloat()) }
                            )
                        }
                    }
                    is ShapeItem -> {
                        val widthDp = with(density) { item.size.width.toDp() }
                        val heightDp = with(density) { item.size.height.toDp() }
                        Box(modifier = Modifier.size(widthDp, heightDp)) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val brush = when (val fill = item.fill) {
                                    is SolidColorFill -> SolidColor(fill.color)
                                    is LinearGradientFill -> Brush.linearGradient(fill.colors, start = Offset.Zero, end = Offset(size.width, size.height))
                                    is RadialGradientFill -> Brush.radialGradient(fill.colors, center = center, radius = size.width / 2f)
                                }
                                when (item.type) {
                                    "Circle" -> drawOval(brush = brush, size = size)
                                    "Rectangle" -> drawRoundRect(brush = brush, size = size, cornerRadius = CornerRadius(item.cornerRadius, item.cornerRadius))
                                    "Star" -> withTransform({ scale(1f, size.height/size.width, center) }) { drawStar(brush, center, size.width/2f) }
                                    "Triangle" -> withTransform({ scale(1f, size.height/size.width, center) }) { drawPolygon(3, brush, center, size.width/2f) }
                                    "Hexagon" -> withTransform({ scale(1f, size.height/size.width, center) }) { drawPolygon(6, brush, center, size.width/2f) }
                                    else -> drawRect(brush = brush, size = size)
                                }
                            }
                        }
                    }
                    is ImageLayer -> {
                        val widthDp = with(density) { item.size.width.toDp() }
                        val heightDp = with(density) { item.size.height.toDp() }
                        val context = LocalContext.current
                        val painter = rememberAsyncImagePainter(model = ImageRequest.Builder(context).data(item.uri).size(coil.size.Size.ORIGINAL).build(),
                            onSuccess = { result ->
                                val intrinsicWidth = result.result.drawable.intrinsicWidth.toFloat()
                                val intrinsicHeight = result.result.drawable.intrinsicHeight.toFloat()
                                if (item.size.width <= 1f || item.size.height <= 1f) {
                                    val ratio = intrinsicHeight / intrinsicWidth
                                    val newWidth = 300f
                                    val newHeight = 300f * ratio
                                    onUpdate(item.copy(size = Size(newWidth, newHeight)))
                                }
                            })
                        Image(painter = painter, contentDescription = null, modifier = Modifier.size(widthDp, heightDp), contentScale = androidx.compose.ui.layout.ContentScale.FillBounds)
                    }
                }
                if (isSelected && !isGrouped) {
                    InteractionHandles(item = item, executeCommand = { executeCommand(it) }, onUpdate = onUpdate)
                }
            }
        }
    }
}

@Composable
fun BoxScope.InteractionHandles(item: Movable, executeCommand: (Command) -> Unit, onUpdate: (Movable) -> Unit) {
    val currentItem by rememberUpdatedState(item)
    if (item is ShapeItem || item is ImageLayer || item is EditableText) {
        Box(
            modifier = Modifier.align(Alignment.BottomEnd).offset(12.dp, 12.dp).size(24.dp).background(Color.Blue, CircleShape).border(1.dp, Color.White, CircleShape)
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        val captured = currentItem
                        when(captured) {
                            is EditableText -> {
                                val newSize = (captured.fontSize * (1 + dragAmount.x / 100f)).coerceIn(10f, 400f)
                                if (newSize.toInt() != captured.fontSize) onUpdate(captured.copy(fontSize = newSize.toInt()))
                            }
                            is ShapeItem -> {
                                val newSize = Size((captured.size.width + dragAmount.x).coerceAtLeast(20f), (captured.size.height + dragAmount.y).coerceAtLeast(20f))
                                onUpdate(captured.copy(size = newSize))
                            }
                            is ImageLayer -> {
                                val newSize = Size((captured.size.width + dragAmount.x).coerceAtLeast(20f), (captured.size.height + dragAmount.y).coerceAtLeast(20f))
                                onUpdate(captured.copy(size = newSize))
                            }
                            else -> {}
                        }
                    }
                }
        )
    }
    Box(
        modifier = Modifier.align(Alignment.TopEnd).offset(12.dp, (-12).dp).size(24.dp).background(Color.Magenta, CircleShape).border(1.dp, Color.White, CircleShape)
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    val captured = currentItem
                    val newRotation = captured.rotation + dragAmount.x
                    val newItem = when(captured) {
                        is ShapeItem -> captured.copy(rotation = newRotation)
                        is EditableText -> captured.copy(rotation = newRotation)
                        is ImageLayer -> captured.copy(rotation = newRotation)
                        is ItemGroup -> captured.copy(rotation = newRotation)
                        else -> captured
                    }
                    onUpdate(newItem)
                }
            }
    )
    if (item is ShapeItem && item.type == "Rectangle") {
        Box(modifier = Modifier.align(Alignment.TopStart).offset((-12).dp, (-12).dp).size(24.dp).background(Color.Green, CircleShape).border(1.dp, Color.White, CircleShape)
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    val captured = currentItem as ShapeItem
                    val newRadius = (captured.cornerRadius + dragAmount.x).coerceIn(0f, min(captured.size.width, captured.size.height)/2)
                    onUpdate(captured.copy(cornerRadius = newRadius))
                }
            }
        )
    }
}

@Composable
fun GroupHandles(group: ItemGroup, isSelected: Boolean, executeCommand: (Command) -> Unit) {
    val currentGroup by rememberUpdatedState(group)
    if (isSelected) {
        val groupBounds = calculateGroupBounds(group)
        val density = LocalDensity.current
        Box(modifier = Modifier.size((groupBounds.width/density.density).dp, (groupBounds.height/density.density).dp).graphicsLayer {
            val pivotX = -groupBounds.left; val pivotY = -groupBounds.top
            transformOrigin = TransformOrigin(if (groupBounds.width != 0f) pivotX / groupBounds.width else 0.5f, if (groupBounds.height != 0f) pivotY / groupBounds.height else 0.5f)
            rotationZ = group.rotation; translationX = group.offset.x + groupBounds.left; translationY = group.offset.y + groupBounds.top
        }.border(2.dp, Color.Cyan, RoundedCornerShape(2.dp)))
    }
    val handleModifier = Modifier.size(24.dp).border(1.dp, Color.White, CircleShape)
    Box(modifier = Modifier.offset { IntOffset(group.offset.x.toInt() - 12, group.offset.y.toInt() - 12) }.then(handleModifier).background(Color.Cyan.copy(alpha=0.8f), CircleShape)
        .pointerInput(Unit) { detectDragGestures { _, drag -> currentGroup.offset += drag } })
    Box(modifier = Modifier.offset { IntOffset((group.offset.x + 80).toInt(), group.offset.y.toInt() - 12) }.then(handleModifier).background(Color.Magenta.copy(alpha=0.8f), CircleShape)
        .pointerInput(Unit) { detectDragGestures { _, drag -> currentGroup.rotation += drag.x } })
}