package com.scribesoul.app.models

import Journal
import JournalPage
import JournalSection
import androidx.compose.runtime.mutableStateListOf

// 1. UI Model -> DTO (Called when SAVING)
fun Journal.toDTO(): JournalDTO {
    return JournalDTO(
        id = this.id,
        uid = this.uid,
        name = this.name,
        sections = this.sections.map { it.toDTO() }
    )
}

fun JournalSection.toDTO(): JournalSectionDTO {
    return JournalSectionDTO(
        id = this.id,
        jid = this.jid,
        type = this.type.javaClass.simpleName, // Converts object Plain to "Plain"
        color = this.color,
        pages = this.pages.map { it.toDTO() }
    )
}

fun JournalPage.toDTO(): JournalPageDTO {
    return when (this) {
        is JournalPage.PlainPage -> PlainPageDTO(this.id, this.name)
        is JournalPage.CalendarPage -> CalendarPageDTO(this.id, this.initialMonth.toString())
        // Map all your other page types here...
        else -> throw IllegalArgumentException("Unknown page type")
    }
}

// 2. DTO -> UI Model (Called when LOADING)
fun JournalDTO.toUIModel(): Journal {
    val snapshotSections = mutableStateListOf<JournalSection>()
    snapshotSections.addAll(this.sections.map { it.toUIModel() })

    return Journal(
        id = this.id,
        uid = this.uid,
        name = this.name,
        sections = snapshotSections
    )
}

fun JournalSectionDTO.toUIModel(): JournalSection {
    val snapshotPages = mutableStateListOf<JournalPage>()
    snapshotPages.addAll(this.pages.map { it.toUIModel() })

    // Convert string type back to SectionType object
    val sectionType = when(this.type) {
        "Plain" -> SectionType.Plain
        "Calendar" -> SectionType.Calendar
        // map others...
        else -> SectionType.Plain
    }

    return JournalSection(
        id = this.id,
        jid = this.jid,
        type = sectionType,
        color = this.color,
        pages = snapshotPages
    )
}

fun JournalPageDTO.toUIModel(): JournalPage {
    return when (this) {
        is PlainPageDTO -> JournalPage.PlainPage(this.id, this.name)
        is CalendarPageDTO -> JournalPage.CalendarPage(this.id) // Parse initialMonthStr here
        // Map all your other page types...
    }
}