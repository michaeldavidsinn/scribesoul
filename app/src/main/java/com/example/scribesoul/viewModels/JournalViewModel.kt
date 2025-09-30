package com.example.scribesoul.viewModels

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
import com.example.scribesoul.commands.Command
import com.example.scribesoul.models.DrawablePath
import com.example.scribesoul.models.EditableText
import com.example.scribesoul.models.ImageLayer
import com.example.scribesoul.models.ItemGroup
import com.example.scribesoul.models.Movable
import com.example.scribesoul.models.ShapeItem
import com.example.scribesoul.models.ToolMode
import com.example.scribesoul.ui.screens.ColorPickerTarget
import com.example.scribesoul.ui.screens.GuideLine

//import com.example.scribesoul.ScribeSoulApplication

class JournalViewModel : ViewModel() {
    // --- Sections and Pages ---
    private val _sections = mutableStateListOf<JournalSection>()
    val sections: List<JournalSection> = _sections

    var selectedSectionIndex by mutableStateOf(0)
        private set
    var selectedPageIndex by mutableStateOf(0)
        private set

    fun changeSelectedPageIndex(page: Int){
        selectedPageIndex = page
    }

    // --- Drawing State ---
    val selectedPaths = mutableStateListOf<DrawablePath>()
    val paths = mutableStateListOf<DrawablePath>()
    var toolMode by mutableStateOf(ToolMode.DRAW)
    var currentPath by mutableStateOf<List<Offset>>(emptyList())

    var drawColor by mutableStateOf(Color.Black)
    var drawThickness by mutableFloatStateOf(8f)
    var eraseThickness by mutableFloatStateOf(40f)
    val guideLines = mutableStateListOf<GuideLine>()

    // --- Layers ---
    val shapes = mutableStateListOf<ShapeItem>()
    val texts = mutableStateListOf<EditableText>()
    val groups = mutableStateListOf<ItemGroup>()
    val imageLayers = mutableStateListOf<ImageLayer>()
    val selectedItems = mutableStateListOf<Movable>()
    val canvasCenter = mutableStateOf(Offset(500f, 500f))

    // --- UI Flags ---
    var showLayerMenu = mutableStateOf(false)
    var showShapeMenu = mutableStateOf(false)
    var colorPickerTarget by mutableStateOf<ColorPickerTarget?>(null)
    var pendingShapeType by mutableStateOf<String?>(null)
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
    init {
        _sections.add(
            JournalSection(
                id = 0,
                type = SectionType.Creation,
                pages = mutableStateListOf(),
                Color(0xFF74A8FF)
            )
        )
    }

    // --- Commands ---
    fun executeCommand(command: Command, page: JournalPage?) {
        command.execute()
        if(page is PlainPage){
            page.undoStack.add(command)
            page.redoStack.clear()
            selectedItems.clear()
            selectedPaths.clear()
        }

    }

    // --- Actions ---
    fun changeSelectedSection(section: Int) { selectedSectionIndex = section }

    fun addSection(type: SectionType) {
        val newSection = JournalSection(
            id = _sections.size,
            type = type,
            pages = mutableStateListOf(
                when (type) {
                    SectionType.Plain -> PlainPage(
                        0,
                        name = ""
                    )
                    SectionType.Habits -> HabitsPage(0)
                    SectionType.Calendar -> CalendarPage(0)
                    SectionType.Creation -> TODO()
                }
            ),
            color = sectionColors[nextColorIndex]
        )
        _sections.add(newSection)
        selectedSectionIndex = _sections.lastIndex
        selectedPageIndex = 0

        nextColorIndex = (nextColorIndex + 1) % sectionColors.size
    }

    fun addPageToSection(sectionIndex: Int) {
        val section = _sections[sectionIndex]
        val newPageId = section.pages.size
        val newPage = when (section.type) {
            SectionType.Plain -> PlainPage(newPageId)
            SectionType.Habits -> HabitsPage(newPageId)
            SectionType.Calendar -> CalendarPage(newPageId)
            SectionType.Creation -> TODO()
        }
        section.pages.add(newPage)
        selectedPageIndex = section.pages.lastIndex
    }


    fun pickImage(uri: Uri) {
//        executeCommand(AddImageCommand(ImageLayer(uri = uri, offset = canvasCenter.value), imageLayers))
    }

    // --- Tools ---
    fun changeToolMode(mode: ToolMode) { toolMode = mode }
    fun updateDrawThickness(value: Float) { drawThickness = value }
    fun updateEraseThickness(value: Float) { eraseThickness = value }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { JournalViewModel() }
        }
    }
}
