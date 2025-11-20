package com.example.scribesoul.viewModels

import Journal
import JournalPage
import JournalPage.*
import JournalSection
import SectionType
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.example.scribesoul.commands.Command
import com.example.scribesoul.models.DrawablePath
import com.example.scribesoul.models.EditableText
import com.example.scribesoul.models.ImageLayer
import com.example.scribesoul.models.ItemGroup
import com.example.scribesoul.models.Movable
import com.example.scribesoul.models.ShapeItem
import com.example.scribesoul.models.ToolMode
import com.example.scribesoul.ui.components.journalPages.MoodPage
import com.example.scribesoul.ui.screens.ColorPickerTarget
import com.example.scribesoul.ui.screens.GuideLine
import java.time.LocalDate
import java.time.YearMonth

//import com.example.scribesoul.ScribeSoulApplication

class JournalViewModel : ViewModel() {
    // --- Sections and Pages ---
    private var _sections = mutableStateListOf<JournalSection>()
    val sections: List<JournalSection> = _sections

    private var nextGlobalPageId = 0


    var journalId by mutableStateOf(0)
        private set

    var selectedSectionIndex by mutableStateOf(0)
        private set
    var selectedPageIndex by mutableStateOf(0)
        private set

    fun changeSelectedPageIndex(page: Int){
        selectedPageIndex = page
    }

    fun changeJournalId(jid: Int){
        journalId = jid
    }

    // --- UI Flags ---
    var showLayerMenu = mutableStateOf(false)
    var showShapeMenu = mutableStateOf(false)
    var colorPickerTarget by mutableStateOf<ColorPickerTarget?>(null)
    var isAddingText by mutableStateOf(false)
    var showGradientPicker by mutableStateOf(false)

    private val sectionColors = listOf(
        Color(0xFF9DF7FF), // #9DF7FF
        Color(0xFFD4B8FF), // #D4B8FF
        Color(0xFFFFCCE3), // #FFCCE3
        Color(0xFFFFB4B4), // #FFB4B4
        Color(0xFFFFD6B4), // #FFD6B4
        Color(0xFFFFFDB4),  // #FFFDB4
        Color(0xFF74A8FF),
    )
    private var nextColorIndex = 0

    // --- Init ---


    fun init(){
        if(_sections.size == 0) {
            _sections.add(
                JournalSection(
                    id = 0,
                    jid = journalId,
                    type = SectionType.Creation,
                    pages = mutableStateListOf(),
                    Color(0xFF74A8FF)
                )
            )
        }
    }

    fun loadJournal(journal: Journal, navController: NavController){
        _sections.clear()
        _sections.addAll(journal.sections)

        changeJournalId(journal.id)
        init()
        changeSelectedSection(0)
        navController.navigate("journal")
    }

    // --- Commands ---


    // --- Actions ---
    fun changeSelectedSection(section: Int) {
        selectedSectionIndex = section
    }

    fun syncBackTo(journalListViewModel: JournalListViewModel) {
        val journal = journalListViewModel.journals.find { it.id == journalId } ?: return

        journal.sections.clear()
        journal.sections.addAll(_sections)
    }

    fun addSection(type: SectionType) {
        val color = sectionColors[_sections.size % sectionColors.size]
        val firstPageId = nextGlobalPageId++
        val newSection = JournalSection(
            id = _sections.size,
            jid = 1,
            type = type,
            pages = mutableStateListOf(
                when (type) {
                    SectionType.Plain -> PlainPage(
                        0,
                        name = ""
                    )
                    SectionType.Habits -> HabitsPage(firstPageId)
                    SectionType.Calendar -> CalendarPage(firstPageId)
                    SectionType.Mood -> MoodsPage(firstPageId, currentMonth =  YearMonth.now())
                    SectionType.Todo -> TodoPage(id = firstPageId)
                    SectionType.Creation -> TODO()
                    SectionType.WideLined -> WideLinedPage(firstPageId)
                    SectionType.WideLinedSmallMargin -> WideLinedSmallMarginPage(firstPageId)
                    SectionType.SmallGrid -> SmallGridPage(firstPageId)
                    SectionType.LargeGrid -> LargeGridPage(firstPageId)
                    SectionType.NarrowLined -> NarrowLinedPage(firstPageId)
                    SectionType.WideLinedLargeMargin -> WideLinedLargeMarginPage(firstPageId)
                    SectionType.NarrowLinedSmallMargin -> NarrowLinedSmallMarginPage(firstPageId)
                    SectionType.NarrowLinedLargeMargin -> NarrowLinedLargeMarginPage(firstPageId)
                }
            ),
            color = color
        )
        _sections.add(newSection)
        selectedSectionIndex = _sections.lastIndex
        selectedPageIndex = 0

    }

    fun addPageToSection(sectionIndex: Int) {
        val section = _sections[sectionIndex]
        val newPageId = nextGlobalPageId++
        val newPage = when (section.type) {
            SectionType.Plain -> PlainPage(newPageId)
            SectionType.Habits -> HabitsPage(newPageId)
            SectionType.Calendar -> CalendarPage(newPageId)
            SectionType.Todo -> TodoPage(newPageId)
            SectionType.Mood -> MoodsPage(newPageId, currentMonth =  YearMonth.now())
            SectionType.Creation -> TODO()
            SectionType.WideLined -> WideLinedPage(newPageId)
            SectionType.LargeGrid -> LargeGridPage(newPageId)
            SectionType.SmallGrid -> SmallGridPage(newPageId)
            SectionType.NarrowLined -> NarrowLinedPage(newPageId)
            SectionType.NarrowLinedLargeMargin -> NarrowLinedLargeMarginPage(newPageId)
            SectionType.NarrowLinedSmallMargin -> NarrowLinedSmallMarginPage(newPageId)
            SectionType.WideLinedLargeMargin -> WideLinedLargeMarginPage(newPageId)
            SectionType.WideLinedSmallMargin -> WideLinedSmallMarginPage(newPageId)
        }
        section.pages.add(newPage)
        selectedPageIndex = section.pages.lastIndex
    }


    fun saveJournal(){

    }

    fun convertToJournalUpload(){

    }



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { JournalViewModel() }
        }
    }
}
