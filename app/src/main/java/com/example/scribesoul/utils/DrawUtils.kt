package com.example.scribesoul.utils // Sesuaikan dengan nama package Anda

import JournalPage
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.example.scribesoul.models.Movable
import com.example.scribesoul.models.ToolMode
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.scribesoul.commands.AddDrawableCommand
import com.example.scribesoul.commands.AddShapeCommand
import com.example.scribesoul.commands.DeleteItemsCommand
import com.example.scribesoul.commands.EraseCommand
import com.example.scribesoul.models.DrawablePath
import com.example.scribesoul.models.EditableText
import com.example.scribesoul.models.FillStyle
import com.example.scribesoul.models.ImageLayer
import com.example.scribesoul.models.ItemGroup
import com.example.scribesoul.models.LinearGradient
import com.example.scribesoul.models.RadialGradient
import com.example.scribesoul.models.ShapeItem
import com.example.scribesoul.utils.drawPathFromFill
import com.example.scribesoul.utils.drawSelectionBorder
import com.example.scribesoul.viewModels.DrawingViewModel
import com.example.scribesoul.viewModels.JournalViewModel
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.min
import com.example.scribesoul.models.LinearGradient as LinearGradientFill
import com.example.scribesoul.models.RadialGradient as RadialGradientFill
import com.example.scribesoul.models.SolidColor as SolidColorFill

// Custom PointerInputModifier untuk reusable drag logic dengan commands
fun Modifier.detectDragGesturesWithCommand(
    movable: Movable,
    onDragStart: (Movable) -> Unit,
    onDragEnd: (Movable) -> Unit
): Modifier = pointerInput(movable) { // Changed key to movable
    detectDragGestures(
        onDragStart = { onDragStart(movable) },
        onDrag = { change, dragAmount ->
            change.consume()
            movable.offset += dragAmount
        },
        onDragEnd = { onDragEnd(movable) }
    )
}

fun distance(p1: Offset, p2: Offset): Float {
    return sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2))
}

fun DrawScope.drawPathFromFill(
    offsets: List<Offset>,
    fill: FillStyle,
    mode: ToolMode,
    thickness: Float
) {
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
        is SolidColorFill -> SolidColor(fill.color.copy(alpha = if (mode == ToolMode.Highlighter) 0.4f else fill.color.alpha))
        is LinearGradientFill -> Brush.linearGradient(fill.colors)
        is RadialGradientFill -> Brush.radialGradient(fill.colors)
    }
    drawPath(path = path, brush = brush, style = style)
}

fun DrawScope.drawPathFromOffsets(
    offsets: List<Offset>,
    color: Color,
    mode: ToolMode,
    thickness: Float
) {
    if (offsets.size < 2) return
    val path = Path().apply {
        moveTo(offsets.first().x, offsets.first().y)
        (1 until offsets.size).forEach { lineTo(offsets[it].x, offsets[it].y) }
    }
    // Logika warna sekarang lebih dinamis
    val (strokeColor, strokeWidth) = when (mode) {
        ToolMode.DRAW -> color to thickness // Gunakan warna yang diberikan
        ToolMode.ERASE -> Color.Transparent to 0f
        ToolMode.Highlighter -> color.copy(alpha = 0.5f) to thickness // Gunakan warna yang diberikan
        ToolMode.Lasso -> Color.Blue.copy(alpha = 0.3f) to 5f
        ToolMode.SHAPE -> TODO()
        ToolMode.TEXT -> TODO()
    }
    drawPath(path = path, color = strokeColor, style = Stroke(width = strokeWidth))
}

// Helper untuk menggambar border seleksi pada path
fun DrawScope.drawSelectionBorder(offsets: List<Offset>) {
    if (offsets.isEmpty()) return
    var minX = Float.MAX_VALUE
    var minY = Float.MAX_VALUE
    var maxX = Float.MIN_VALUE
    var maxY = Float.MIN_VALUE

    offsets.forEach {
        minX = minOf(minX, it.x)
        minY = minOf(minY, it.y)
        maxX = maxOf(maxX, it.x)
        maxY = maxOf(maxY, it.y)
    }
    drawRect(
        color = Color.Blue,
        topLeft = Offset(minX, minY),
        size = Size(maxX - minX, maxY - minY),
        style = Stroke(
            width = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)
        )
    )
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

fun DrawScope.drawStar(brush: Brush, center: Offset, radius: Float) {
    val path = Path()
    val outerRadius = radius
    val innerRadius = radius / 2.0f
    var angle = -Math.PI / 2
    path.moveTo(
        center.x + (outerRadius * cos(angle)).toFloat(),
        center.y + (outerRadius * sin(angle)).toFloat()
    )
    for (i in 1 until 10) {
        val r = if (i % 2 == 0) outerRadius else innerRadius
        angle += Math.PI / 5
        path.lineTo(center.x + (r * cos(angle)).toFloat(), center.y + (r * sin(angle)).toFloat())
    }
    path.close()
    drawPath(path = path, brush = brush, style = Fill)
}

private fun getTransformedCorners(
    offset: Offset,
    size: Size,
    rotationDegrees: Float
): List<Offset> {
    // Titik pusat objek, relatif terhadap pojok kiri atasnya
    val center = Offset(size.width / 2f, size.height / 2f)

    // Posisi 4 sudut relatif terhadap pojok kiri atas (0,0)
    val corners = listOf(
        Offset(0f, 0f),                      // Kiri-atas
        Offset(size.width, 0f),             // Kanan-atas
        Offset(size.width, size.height),    // Kanan-bawah
        Offset(0f, size.height)             // Kiri-bawah
    )

    // Ubah derajat ke radian untuk fungsi sin/cos
    val rotationRadians = Math.toRadians(rotationDegrees.toDouble()).toFloat()
    val cos = cos(rotationRadians)
    val sin = sin(rotationRadians)

    return corners.map { corner ->
        // 1. Geser sudut agar rotasi berpusat di tengah objek
        val cornerRelativeToCenter = corner - center

        // 2. Terapkan rumus rotasi 2D
        val rotatedX = cornerRelativeToCenter.x * cos - cornerRelativeToCenter.y * sin
        val rotatedY = cornerRelativeToCenter.x * sin + cornerRelativeToCenter.y * cos
        val rotatedCorner = Offset(rotatedX, rotatedY)

        // 3. Geser kembali sudut yang telah dirotasi dan terapkan offset utama objek
        rotatedCorner + center + offset
    }
}

fun isMovableInPolygon(
    movable: Movable,
    polygon: List<Offset>,
    density: androidx.compose.ui.unit.Density
): Boolean {
    val corners = when (movable) {
        // Untuk item tunggal, langsung hitung sudutnya
        is EditableText -> getTransformedCorners(movable.offset, movable.size, movable.rotation)
        is ShapeItem -> getTransformedCorners(movable.offset, movable.size, movable.rotation)
        is ImageLayer -> getTransformedCorners(movable.offset, movable.size, movable.rotation)

        // Untuk grup, prosesnya sedikit lebih kompleks
        is ItemGroup -> {
            // 1. Dapatkan kotak pembatas (bounding box) yang ketat di sekitar semua item anak
            val groupBounds = calculateGroupBounds(movable)

            // 2. Dapatkan sudut-sudut dari kotak pembatas tersebut, lalu rotasikan sesuai rotasi grup
            getTransformedCorners(
                offset = movable.offset + groupBounds.topLeft, // Offset grup + offset relatif kotak
                size = groupBounds.size,
                rotationDegrees = movable.rotation
            )
        }

        else -> return false
    }

    // Sebuah objek dianggap terseleksi jika salah satu sudutnya berada di dalam poligon lasso.
    // NOTE: Pengecekan ini sudah jauh lebih baik. Untuk akurasi 100% (misal: lasso kecil di dalam objek besar),
    // diperlukan juga pengecekan interseksi garis, namun untuk sebagian besar kasus, ini sudah sangat memadai.
    return corners.any { isPointInPolygon(it, polygon) }
}

fun calculateGroupBounds(group: ItemGroup): Rect {
    if (group.items.isEmpty()) return Rect.Zero

    var minX = Float.MAX_VALUE
    var minY = Float.MAX_VALUE
    var maxX = Float.MIN_VALUE
    var maxY = Float.MIN_VALUE

    group.items.forEach { item ->
        // Dapatkan sudut absolut dari setiap item anak.
        // Ingat, offset item anak adalah RELATIF terhadap pusat grup.
        val itemCorners = when (item) {
            is EditableText -> getTransformedCorners(item.offset, item.size, item.rotation)
            is ShapeItem -> getTransformedCorners(item.offset, item.size, item.rotation)
            is ImageLayer -> getTransformedCorners(item.offset, item.size, item.rotation)
            else -> emptyList()
        }

        // Cari nilai x,y min/max dari semua sudut yang didapat
        itemCorners.forEach { corner ->
            minX = minOf(minX, corner.x)
            minY = minOf(minY, corner.y)
            maxX = maxOf(maxX, corner.x)
            maxY = maxOf(maxY, corner.y)
        }
    }

    // Mengembalikan Rect yang berisi semua item, relatif terhadap titik pusat grup (0,0)
    return Rect(left = minX, top = minY, right = maxX, bottom = maxY)
}

fun DrawScope.drawPolygon(sides: Int, brush: Brush, center: Offset, radius: Float) {
    if (sides < 3) return
    val path = Path()
    val angle = 2.0 * Math.PI / sides
    path.moveTo(center.x + (radius * cos(0.0)).toFloat(), center.y + (radius * sin(0.0)).toFloat())
    for (i in 1 until sides) {
        path.lineTo(
            center.x + (radius * cos(angle * i)).toFloat(),
            center.y + (radius * sin(angle * i)).toFloat()
        )
    }
    path.close()
    drawPath(path = path, brush = brush, style = Fill)
}

@Composable
fun DrawCanvas(
    drawingViewModel: DrawingViewModel,
    page: JournalPage
) {
    val currentPath = remember { mutableStateListOf<Offset>() }
    var currentShape by remember { mutableStateOf<ShapeItem?>(null) }

    val density = LocalDensity.current

    var dragStart by remember { mutableStateOf<Offset?>(null) }
    var shapeEnd by remember { mutableStateOf<Offset?>(null) }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(
                drawingViewModel.toolMode,
                drawingViewModel.drawThickness,
                drawingViewModel.eraseThickness,
                drawingViewModel.drawColor,
                drawingViewModel.pendingShapeType
            ) {
                detectDragGestures(
                    onDragStart = { start ->
                        currentPath.clear()
                        currentPath.add(start)
                        when (drawingViewModel.toolMode) {
                            ToolMode.SHAPE -> {
                                // Prepare preview shape (do NOT add to shapes yet)
                                dragStart = start
                                val fillForShape = drawingViewModel.pendingShapeFill ?: SolidColorFill(drawingViewModel.drawColor)
                                currentShape = ShapeItem(
                                    type = drawingViewModel.pendingShapeType ?: "Rectangle",
                                    offset = start, // we treat offset as top-left during preview
                                    fill = fillForShape,
                                    size = Size(1f, 1f),
                                    cornerRadius = 0f
                                )
                            }
                            else -> Unit
                        }
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        when (drawingViewModel.toolMode) {
                            ToolMode.SHAPE -> {
                                // update preview from dragStart -> current position
                                val start = dragStart ?: return@detectDragGestures
                                val end = change.position
                                val topLeft = Offset(minOf(start.x, end.x), minOf(start.y, end.y))
                                val newSize = Size(
                                    (end.x - start.x).absoluteValue.coerceAtLeast(1f),
                                    (end.y - start.y).absoluteValue.coerceAtLeast(1f)
                                )
                                // use copy to ensure state object changes so Compose recomposes
                                currentShape = currentShape?.copy(offset = topLeft, size = newSize)
                            }


                            ToolMode.ERASE -> {
                                val point = change.position
                                currentPath.add(point)

                                // ✅ Real-time erase logic
                                // 1. Remove intersecting SHAPES immediately
                                val shapesToRemove = page.shapes.filter {
                                    shapeIntersectsPoint(it, point, drawingViewModel.eraseThickness)
                                }
                                if (shapesToRemove.isNotEmpty()) {
                                    page.shapes.removeAll(shapesToRemove)
                                    page.undoStack.add(DeleteItemsCommand(shapesToRemove, listOf(drawingViewModel.texts, page.shapes, drawingViewModel.imageLayers, drawingViewModel.groups)))
                                    page.redoStack.clear()
                                }

                                // 2. Erase intersecting PATHS immediately
                                val updatedPaths = mutableListOf<DrawablePath>()
                                page.paths.forEach { drawablePath ->
                                    if (drawablePath.toolMode == ToolMode.Highlighter) {
                                        // skip highlighters (optional)
                                        updatedPaths.add(drawablePath)
                                        return@forEach
                                    }

                                    // keep only the parts that aren’t “erased”
                                    val remainingSegments = mutableListOf<Offset>()
                                    var segment = mutableListOf<Offset>()

                                    drawablePath.offsets.forEach { p ->
                                        val erased = distance(p, point) < drawingViewModel.eraseThickness
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
                                                if (temp.size > 1) updatedPaths.add(drawablePath.copy(offsets = temp.toList()))
                                                temp = mutableListOf()
                                            } else {
                                                temp.add(offset)
                                            }
                                        }
                                        if (temp.size > 1) updatedPaths.add(drawablePath.copy(offsets = temp.toList()))
                                    }
                                }

                                // Replace existing paths with updated versions (real-time)
                                page.paths.clear()
                                page.paths.addAll(updatedPaths)
                            }


                            ToolMode.Lasso -> currentPath.add(change.position)

                            else -> currentPath.add(change.position)
                        }
                    },
                    onDragEnd = {
                        when (drawingViewModel.toolMode) {
                            ToolMode.SHAPE -> {
                                // finalize preview into real shape (undoable)
                                currentShape?.let { shp ->
                                    // enforce minimum size
                                    val finalSize = Size(
                                        shp.size.width.coerceAtLeast(2f),
                                        shp.size.height.coerceAtLeast(2f)
                                    )
                                    val finalShape = shp.copy(size = finalSize)
                                    drawingViewModel.executeCommand(AddShapeCommand(finalShape, page.shapes), page)
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
                                    drawingViewModel.selectedItems.clear()
                                    drawingViewModel.selectedPaths.clear()

                                    val allMovables = drawingViewModel.texts +
                                            page.shapes +
                                            drawingViewModel.imageLayers +
                                            drawingViewModel.groups

                                    allMovables.forEach { item ->
                                        if (isMovableInPolygon(item, polygon, density))
                                            drawingViewModel.selectedItems.add(item)
                                    }

                                    page.paths.forEach { path ->
                                        if (path.offsets.any { point -> isPointInPolygon(point, polygon) })
                                            drawingViewModel.selectedPaths.add(path)
                                    }
                                }
                            }

                            else -> {
                                if (currentPath.size > 1) {
                                    val newPath = DrawablePath(
                                        offsets = currentPath.toList(),
                                        toolMode = drawingViewModel.toolMode,
                                        thickness = drawingViewModel.drawThickness,
                                        fill = SolidColorFill(
                                            if (drawingViewModel.toolMode == ToolMode.Highlighter)
                                                Color.Yellow.copy(alpha = 0.5f)
                                            else drawingViewModel.drawColor
                                        )
                                    )
                                    drawingViewModel.executeCommand(
                                        AddDrawableCommand(newPath, page.paths),
                                        page
                                    )
                                }
                            }
                        }
                        currentPath.clear()
                    }
                )
            }
    ) {
        // 🖌️ Draw saved paths
        page.paths.forEach { path ->
            drawPathFromFill(path.offsets, path.fill, path.toolMode, path.thickness)
        }

        page.shapes.forEach { shape ->
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


        // 🧱 Draw shape preview
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

        if (drawingViewModel.toolMode != ToolMode.ERASE && drawingViewModel.toolMode != ToolMode.Lasso) {
            drawPathFromFill(currentPath, SolidColorFill(drawingViewModel.drawColor), drawingViewModel.toolMode, drawingViewModel.drawThickness)
        }

//        // ✏️ Real-time path
//        if (drawingViewModel.toolMode == ToolMode.DRAW ||
//            drawingViewModel.toolMode == ToolMode.Highlighter
//        ) {
//            drawPathFromFill(
//                currentPath,
//                SolidColorFill(drawingViewModel.drawColor),
//                drawingViewModel.toolMode,
//                drawingViewModel.drawThickness
//            )
//        }
        if (drawingViewModel.toolMode == ToolMode.ERASE && currentPath.isNotEmpty()) {
            val lastPoint = currentPath.last()
            drawCircle(
                color = Color.LightGray.copy(alpha = 0.3f),
                center = lastPoint,
                radius = drawingViewModel.eraseThickness,
                style = Stroke(width = 2.dp.toPx())
            )
        }


        if (drawingViewModel.toolMode == ToolMode.Lasso && currentPath.isNotEmpty()) {
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

        // ✨ Selection & guide lines
        drawingViewModel.selectedPaths.forEach { drawSelectionBorder(it.offsets) }
        drawingViewModel.guideLines.forEach { line ->
            drawLine(
                color = Color.Cyan,
                start = line.start,
                end = line.end,
                strokeWidth = 1.5f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
            )
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
