package com.scribesoul.app.viewModels

import JournalPage
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
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.scribesoul.app.commands.Command
import com.scribesoul.app.models.DrawablePath
import com.scribesoul.app.models.FillStyle
import com.scribesoul.app.models.ItemGroup
import com.scribesoul.app.models.Movable
import com.scribesoul.app.models.ToolMode
import com.scribesoul.app.ui.screens.ColorPickerTarget
import com.scribesoul.app.ui.screens.GuideLine

class DrawingViewModel: ViewModel() {
    val selectedPaths = mutableStateListOf<DrawablePath>()
//    val shapes = mutableStateListOf<ShapeItem>()
//    val texts = mutableStateListOf<EditableText>()
//    val imageLayers = mutableStateListOf<ImageLayer>()
    val groups = mutableStateListOf<ItemGroup>()
    val selectedItems = mutableStateListOf<Movable>()
    val guideLines = mutableStateListOf<GuideLine>()


    var toolMode by mutableStateOf(ToolMode.DRAW)
    var drawColor by mutableStateOf(Color.Black)
    var drawThickness by mutableFloatStateOf(8f)
    var eraseThickness by mutableFloatStateOf(40f)

    var pendingShapeType by mutableStateOf<String?>(null)
    var pendingShapeFill by  mutableStateOf<FillStyle?>(null)

    var colorPickerTarget by mutableStateOf<ColorPickerTarget?>(null)
    var showGradientPicker by mutableStateOf(false)

    val canvasCenter = mutableStateOf(Offset(500f, 500f))



    fun executeCommand(command: Command, page: JournalPage?) {
        command.execute()
        if(page is JournalPage){
            page.undoStack.add(command)
            page.redoStack.clear()
            selectedItems.clear()
            selectedPaths.clear()
        }

    }

    fun changeToolMode(mode: ToolMode) { toolMode = mode }
    fun updateDrawThickness(value: Float) { drawThickness = value }
    fun updateEraseThickness(value: Float) { eraseThickness = value }

    fun pickImage(uri: Uri) {
//        executeCommand(AddImageCommand(ImageLayer(uri = uri, offset = canvasCenter.value), imageLayers))
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { DrawingViewModel() }
        }
    }
}

