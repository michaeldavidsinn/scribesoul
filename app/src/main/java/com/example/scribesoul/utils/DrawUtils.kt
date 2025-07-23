package com.example.scribesoul.utils // Sesuaikan dengan nama package Anda

import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.unit.dp
import com.example.scribesoul.models.EditableText
import com.example.scribesoul.models.ImageLayer
import com.example.scribesoul.models.ItemGroup
import com.example.scribesoul.models.ShapeItem

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

fun DrawScope.drawPathFromOffsets(offsets: List<Offset>, mode: ToolMode, thickness: Float) {
    if (offsets.size < 2) return
    val path = Path().apply {
        moveTo(offsets.first().x, offsets.first().y)
        (1 until offsets.size).forEach { lineTo(offsets[it].x, offsets[it].y) }
    }
    val (color, strokeWidth) = when (mode) {
        ToolMode.DRAW -> Color.Black to thickness // Use passed thickness
        ToolMode.ERASE -> Color.Transparent to 0f // Eraser path is not drawn
        ToolMode.Highlighter -> Color.Yellow.copy(alpha = 0.5f) to thickness // Use passed thickness
        ToolMode.Lasso -> Color.Blue.copy(alpha = 0.3f) to 5f
    }
    drawPath(path = path, color = color, style = Stroke(width = strokeWidth))
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

fun DrawScope.drawStar(center: Offset, radius: Float, color: Color) {
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
    drawPath(path = path, color = color, style = Fill)
}

fun isRectInPolygon(rect: Rect, polygon: List<Offset>): Boolean {
    // Cek apakah salah satu dari 4 sudut ada di dalam poligon
    if (isPointInPolygon(rect.topLeft, polygon)) return true
    if (isPointInPolygon(rect.topRight, polygon)) return true
    if (isPointInPolygon(rect.bottomLeft, polygon)) return true
    if (isPointInPolygon(rect.bottomRight, polygon)) return true
    return false
}

fun isMovableInPolygon(movable: Movable, polygon: List<Offset>, density: androidx.compose.ui.unit.Density): Boolean {
    return when (movable) {
        is EditableText -> {
            // Logika baru: buat Rect dari offset dan ukuran, lalu cek dengan poligon
            val bounds = Rect(movable.offset, movable.size)
            isRectInPolygon(bounds, polygon)
        }
        is ShapeItem -> {
            val sizePx = with(density) { 100.dp.toPx() }
            val bounds = Rect(movable.offset, Size(sizePx, sizePx))
            isRectInPolygon(bounds, polygon)
        }
        is ImageLayer -> {
            val bounds = Rect(movable.offset, movable.size)
            isRectInPolygon(bounds, polygon)
        }
        is ItemGroup -> {
            val bounds = calculateGroupBounds(movable)
            isRectInPolygon(bounds, polygon)
        }
        else -> false
    }
}

fun calculateGroupBounds(group: ItemGroup): Rect {
    if (group.items.isEmpty()) return Rect.Zero

    var minX = Float.MAX_VALUE
    var minY = Float.MAX_VALUE
    var maxX = Float.MIN_VALUE
    var maxY = Float.MIN_VALUE

    group.items.forEach { item ->
        // Perhitungan simpel, belum menghitung rotasi item individual
        val itemSize = when (item) {
            is ShapeItem -> Size(100f, 100f) // Asumsi ukuran dalam px untuk kalkulasi
            is ImageLayer -> item.size
            else -> Size(50f, 50f) // Ukuran default untuk teks
        }
        minX = minOf(minX, item.offset.x)
        minY = minOf(minY, item.offset.y)
        maxX = maxOf(maxX, item.offset.x + itemSize.width)
        maxY = maxOf(maxY, item.offset.y + itemSize.height)
    }

    return Rect(left = minX, top = minY, right = maxX, bottom = maxY)
}

fun DrawScope.drawPolygon(sides: Int, radius: Float, center: Offset, color: Color) {
    if (sides < 3) return

    val path = Path()
    val angle = 2.0 * Math.PI / sides

    // Titik awal
    path.moveTo(
        center.x + (radius * cos(0.0)).toFloat(),
        center.y + (radius * sin(0.0)).toFloat()
    )

    // Gambar garis ke setiap sudut poligon
    for (i in 1 until sides) {
        path.lineTo(
            center.x + (radius * cos(angle * i)).toFloat(),
            center.y + (radius * sin(angle * i)).toFloat()
        )
    }
    path.close()
    drawPath(path = path, color = color, style = Fill)
}