package com.example.scribesoul.models // Sesuaikan dengan nama package Anda

import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

// Interface untuk semua objek yang bisa dipindahkan
interface Movable {
    var offset: Offset
    var rotation: Float
}

// Enum untuk mode alat gambar
enum class ToolMode {
    DRAW, ERASE, Highlighter, Lasso
}

// Data class untuk setiap jenis objek di kanvas
data class DrawablePath(
    var offsets: List<Offset>,
    val toolMode: ToolMode
)

data class ImageLayer(
    val uri: Uri,
    override var offset: Offset = Offset.Zero,
    override var rotation: Float = 0f,
    var size: Size = Size(100f, 100f),
    var isResizing: Boolean = false
) : Movable

data class ShapeItem(
    val type: String,
    override var offset: Offset,
    override var rotation: Float = 0f
) : Movable

data class EditableText(
    var text: String,
    override var offset: Offset,
    override var rotation: Float = 0f,
    var isEditing: Boolean = false,
    var color: Color = Color.Black,
    var fontSize: Int = 18,
    var size: Size = Size.Zero // <- TAMBAHKAN PROPERTI BARU INI
) : Movable

data class ItemGroup(
    val items: MutableList<Movable> = mutableListOf(),
    override var offset: Offset, // Posisi grup (titik tengah)
    override var rotation: Float = 0f // Rotasi grup
) : Movable