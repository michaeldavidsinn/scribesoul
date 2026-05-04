package com.scribesoul.app.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class JournalDTO(
    val id: Int,
    val uid: Int,
    val name: String,
    val sections: List<JournalSectionDTO> // Standard List!
)

@Serializable
data class JournalSectionDTO(
    val id: Int,
    val jid: Int,
    val type: String, // e.g., "Plain", "Dotted"
    @Serializable(with = ColorSerializer::class) val color: androidx.compose.ui.graphics.Color,
    val pages: List<JournalPageDTO>
)

// --- Handling the Polymorphic Pages ---
@Serializable
sealed class JournalPageDTO {
    abstract val id: Int
    // Add other common properties here if you want them saved (like paths/texts)
}

@Serializable
@SerialName("plain_page")
data class PlainPageDTO(
    override val id: Int,
    val name: String
    // val paths: List<DrawablePathDTO> // You would add your canvas elements here
) : JournalPageDTO()

@Serializable
@SerialName("calendar_page")
data class CalendarPageDTO(
    override val id: Int,
    val initialMonthStr: String // YearMonth mapped to a string like "2024-04"
) : JournalPageDTO()

// (You will do this for all your page types: WideLined, Grid, etc.)