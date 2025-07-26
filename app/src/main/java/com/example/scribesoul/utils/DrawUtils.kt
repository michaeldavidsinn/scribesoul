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
import androidx.compose.ui.graphics.PathEffect
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

fun DrawScope.drawPathFromOffsets(offsets: List<Offset>, color: Color, mode: ToolMode, thickness: Float) {
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
        style = Stroke(width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f))
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

private fun getTransformedCorners(offset: Offset, size: Size, rotationDegrees: Float): List<Offset> {
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

fun isMovableInPolygon(movable: Movable, polygon: List<Offset>, density: androidx.compose.ui.unit.Density): Boolean {
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