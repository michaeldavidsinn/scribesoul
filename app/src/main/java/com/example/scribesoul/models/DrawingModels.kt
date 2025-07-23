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

interface Colorable {
    var color: Color
}

// Enum untuk mode alat gambar
enum class ToolMode {
    DRAW, ERASE, Highlighter, Lasso
}

// Data class untuk setiap jenis objek di kanvas
data class DrawablePath(
    var offsets: List<Offset>,
    val toolMode: ToolMode,
    val thickness: Float = 8f
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
    override var rotation: Float = 0f,
    var size: Size = Size(100f, 100f), // TAMBAHKAN ukuran
    override var color: Color // TAMBAHKAN warna
) : Movable, Colorable // Implementasikan Colorable

data class EditableText(
    var text: String,
    override var offset: Offset,
    override var rotation: Float = 0f,
    var isEditing: Boolean = false,
    override var color: Color = Color.Black, // Jadikan override dari Colorable
    var fontSize: Int = 18,
    var size: Size = Size.Zero
) : Movable, Colorable

data class ItemGroup(
    val items: MutableList<Movable> = mutableListOf(),
    override var offset: Offset, // Posisi grup (titik tengah)
    override var rotation: Float = 0f // Rotasi grup
) : Movable