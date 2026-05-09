package com.scribesoul.app.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

// --- 1. Root DTOs ---
@Serializable
data class JournalDTO(
    val id: Int,
    val uid: Int,
    val name: String,
    val sections: List<JournalSectionDTO>
)

@Serializable
data class JournalSectionDTO(
    val id: Int,
    val jid: Int,
    val type: String,
    @Serializable(with = ColorSerializer::class) val color: Color,
    val pages: List<JournalPageDTO>
)


// --- 4. Page DTOs ---
@Serializable
sealed class JournalPageDTO {
    abstract val id: Int
    abstract val paths: List<DrawablePathDTO>
    abstract val shapes: List<ShapeItemDTO>
    abstract val imageLayers: List<ImageLayerDTO>
    abstract val texts: List<EditableTextDTO>
}

@Serializable @SerialName("todo_item")
data class TodoItemDTO(val text: String, val isChecked: Boolean)

@Serializable @SerialName("plain_page")
data class PlainPageDTO(override val id: Int, val name: String, override val paths: List<DrawablePathDTO> = emptyList(), override val shapes: List<ShapeItemDTO> = emptyList(), override val imageLayers: List<ImageLayerDTO> = emptyList(), override val texts: List<EditableTextDTO> = emptyList()) : JournalPageDTO()

@Serializable @SerialName("todo_page")
data class TodoPageDTO(override val id: Int, val todoList: List<TodoItemDTO> = emptyList(), override val paths: List<DrawablePathDTO> = emptyList(), override val shapes: List<ShapeItemDTO> = emptyList(), override val imageLayers: List<ImageLayerDTO> = emptyList(), override val texts: List<EditableTextDTO> = emptyList()) : JournalPageDTO()

@Serializable @SerialName("habits_page")
data class HabitsPageDTO(override val id: Int, val habits: List<String> = emptyList(), override val paths: List<DrawablePathDTO> = emptyList(), override val shapes: List<ShapeItemDTO> = emptyList(), override val imageLayers: List<ImageLayerDTO> = emptyList(), override val texts: List<EditableTextDTO> = emptyList()) : JournalPageDTO()

@Serializable @SerialName("calendar_page")
data class CalendarPageDTO(override val id: Int, val initialMonthStr: String, val reminders: Map<String, List<String>> = emptyMap(), override val paths: List<DrawablePathDTO> = emptyList(), override val shapes: List<ShapeItemDTO> = emptyList(), override val imageLayers: List<ImageLayerDTO> = emptyList(), override val texts: List<EditableTextDTO> = emptyList()) : JournalPageDTO()

@Serializable @SerialName("moods_page")
data class MoodsPageDTO(override val id: Int, val initialMonthStr: String, val moods: Map<String, Map<Int, Float>> = emptyMap(), override val paths: List<DrawablePathDTO> = emptyList(), override val shapes: List<ShapeItemDTO> = emptyList(), override val imageLayers: List<ImageLayerDTO> = emptyList(), override val texts: List<EditableTextDTO> = emptyList()) : JournalPageDTO()

// --- Layout Page DTOs (Copy-paste structure) ---
@Serializable @SerialName("wide_lined") data class WideLinedPageDTO(override val id: Int, val name: String, override val paths: List<DrawablePathDTO> = emptyList(), override val shapes: List<ShapeItemDTO> = emptyList(), override val imageLayers: List<ImageLayerDTO> = emptyList(), override val texts: List<EditableTextDTO> = emptyList()) : JournalPageDTO()
@Serializable @SerialName("wide_lined_sm") data class WideLinedSmallMarginPageDTO(override val id: Int, val name: String, override val paths: List<DrawablePathDTO> = emptyList(), override val shapes: List<ShapeItemDTO> = emptyList(), override val imageLayers: List<ImageLayerDTO> = emptyList(), override val texts: List<EditableTextDTO> = emptyList()) : JournalPageDTO()
@Serializable @SerialName("wide_lined_lg") data class WideLinedLargeMarginPageDTO(override val id: Int, val name: String, override val paths: List<DrawablePathDTO> = emptyList(), override val shapes: List<ShapeItemDTO> = emptyList(), override val imageLayers: List<ImageLayerDTO> = emptyList(), override val texts: List<EditableTextDTO> = emptyList()) : JournalPageDTO()
@Serializable @SerialName("narrow_lined") data class NarrowLinedPageDTO(override val id: Int, val name: String, override val paths: List<DrawablePathDTO> = emptyList(), override val shapes: List<ShapeItemDTO> = emptyList(), override val imageLayers: List<ImageLayerDTO> = emptyList(), override val texts: List<EditableTextDTO> = emptyList()) : JournalPageDTO()
@Serializable @SerialName("narrow_lined_sm") data class NarrowLinedSmallMarginPageDTO(override val id: Int, val name: String, override val paths: List<DrawablePathDTO> = emptyList(), override val shapes: List<ShapeItemDTO> = emptyList(), override val imageLayers: List<ImageLayerDTO> = emptyList(), override val texts: List<EditableTextDTO> = emptyList()) : JournalPageDTO()
@Serializable @SerialName("narrow_lined_lg") data class NarrowLinedLargeMarginPageDTO(override val id: Int, val name: String, override val paths: List<DrawablePathDTO> = emptyList(), override val shapes: List<ShapeItemDTO> = emptyList(), override val imageLayers: List<ImageLayerDTO> = emptyList(), override val texts: List<EditableTextDTO> = emptyList()) : JournalPageDTO()
@Serializable @SerialName("small_grid") data class SmallGridPageDTO(override val id: Int, val name: String, override val paths: List<DrawablePathDTO> = emptyList(), override val shapes: List<ShapeItemDTO> = emptyList(), override val imageLayers: List<ImageLayerDTO> = emptyList(), override val texts: List<EditableTextDTO> = emptyList()) : JournalPageDTO()
@Serializable @SerialName("large_grid") data class LargeGridPageDTO(override val id: Int, val name: String, override val paths: List<DrawablePathDTO> = emptyList(), override val shapes: List<ShapeItemDTO> = emptyList(), override val imageLayers: List<ImageLayerDTO> = emptyList(), override val texts: List<EditableTextDTO> = emptyList()) : JournalPageDTO()
@Serializable @SerialName("dotted_page") data class DottedPageDTO(override val id: Int, val name: String, override val paths: List<DrawablePathDTO> = emptyList(), override val shapes: List<ShapeItemDTO> = emptyList(), override val imageLayers: List<ImageLayerDTO> = emptyList(), override val texts: List<EditableTextDTO> = emptyList()) : JournalPageDTO()