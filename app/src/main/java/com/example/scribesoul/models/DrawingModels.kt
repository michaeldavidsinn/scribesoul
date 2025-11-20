package com.example.scribesoul.models // Sesuaikan dengan nama package Anda

import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color


sealed interface FillStyle
data class SolidColor(val color: Color) : FillStyle
data class LinearGradient(val colors: List<Color>) : FillStyle
data class RadialGradient(val colors: List<Color>) : FillStyle

// Interface untuk semua objek yang bisa dipindahkan
interface Movable {
    var offset: Offset
    var rotation: Float
}

interface Colorable {
    var fill: FillStyle
}

// Enum untuk mode alat gambar
enum class ToolMode {
    DRAW, ERASE, Highlighter, Lasso, SHAPE, TEXT
}

// Data class untuk setiap jenis objek di kanvas
data class DrawablePath(
    var offsets: List<Offset>,
    val toolMode: ToolMode,
    val thickness: Float = 8f,
    override var fill: FillStyle = SolidColor(Color.Black)
) : Colorable

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
    var size: Size = Size(100f, 100f),
    override var fill: FillStyle, // <-- Diubah dari color
    var cornerRadius: Float = 0f
) : Movable, Colorable

data class EditableText(
    var text: String,
    override var offset: Offset,
    override var rotation: Float = 0f,
    var isEditing: Boolean = false,
    override var fill: FillStyle = SolidColor(Color.Black), // <-- Diubah dari color
    var fontSize: Int = 18,
    var size: Size = Size.Zero
) : Movable, Colorable{
    val bounds: Rect
        get() = Rect(
            offset.x,
            offset.y,
            offset.x + size.width,
            offset.y + size.height
        )
}

data class ItemGroup(
    val items: MutableList<Movable> = mutableListOf(),
    override var offset: Offset, // Posisi grup (titik tengah)
    override var rotation: Float = 0f // Rotasi grup
) : Movable