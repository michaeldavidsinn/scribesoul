package com.scribesoul.app.models

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

// --- 1. FillStyle DTOs ---
@Serializable
sealed class FillStyleDTO

@Serializable
@SerialName("solid_color")
data class SolidColorDTO(
    @Serializable(with = ColorSerializer::class) val color: Color
) : FillStyleDTO()

@Serializable
@SerialName("linear_gradient")
data class LinearGradientDTO(
    val colors: List<@Serializable(with = ColorSerializer::class) Color>
) : FillStyleDTO()

@Serializable
@SerialName("radial_gradient")
data class RadialGradientDTO(
    val colors: List<@Serializable(with = ColorSerializer::class) Color>
) : FillStyleDTO()

// --- 2. Canvas Item DTOs ---
@Serializable
data class DrawablePathDTO(
    val offsets: List<@Serializable(with = OffsetSerializer::class) Offset>,
    val toolMode: String, // Save ToolMode enum as String
    val thickness: Float,
    val fill: FillStyleDTO
)

@Serializable
data class ShapeItemDTO(
    val type: String,
    @Serializable(with = OffsetSerializer::class) val offset: Offset,
    val rotation: Float,
    @Serializable(with = SizeSerializer::class) val size: Size,
    val fill: FillStyleDTO,
    val cornerRadius: Float
)

@Serializable
data class EditableTextDTO(
    val text: String,
    @Serializable(with = OffsetSerializer::class) val offset: Offset,
    val rotation: Float,
    val fill: FillStyleDTO,
    val fontSize: Int,
    @Serializable(with = SizeSerializer::class) val size: Size
)

@Serializable
data class ImageLayerDTO(
    val uriStr: String, // Save Uri as a simple String
    @Serializable(with = OffsetSerializer::class) val offset: Offset,
    val rotation: Float,
    @Serializable(with = SizeSerializer::class) val size: Size
)