package com.scribesoul.app.ui.screens



import JournalPage
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.scribesoul.R
import com.scribesoul.app.ui.components.journalPages.CreationPage
import com.scribesoul.app.ui.components.journalPages.PlainPage
import com.scribesoul.app.viewModels.JournalViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.scribesoul.app.commands.AddImageCommand
import com.scribesoul.app.commands.ChangeFillStyleCommand
import com.scribesoul.app.commands.LayerDirection
import com.scribesoul.app.commands.LayeringCommand
import com.scribesoul.app.models.Colorable
import com.scribesoul.app.models.ImageLayer
import com.scribesoul.app.models.LinearGradient as LinearGradientFill
import com.scribesoul.app.models.*
import com.scribesoul.app.models.SolidColor as SolidColorFill
import com.scribesoul.app.models.ToolMode
import com.scribesoul.app.ui.components.journalPages.CalendarPage
import com.scribesoul.app.ui.components.journalPages.DottedPage
import com.scribesoul.app.ui.components.journalPages.HabitsPage
import com.scribesoul.app.ui.components.journalPages.LargeGridPage
import com.scribesoul.app.ui.components.journalPages.MoodPage
import com.scribesoul.app.ui.components.journalPages.NarrowLinedLargeMarginPage
import com.scribesoul.app.ui.components.journalPages.NarrowLinedPage
import com.scribesoul.app.ui.components.journalPages.NarrowLinedSmallMarginPage
import com.scribesoul.app.ui.components.journalPages.SmallGridPage
import com.scribesoul.app.ui.components.journalPages.TodoPage
import com.scribesoul.app.ui.components.journalPages.WideLinedLargeMarginsPage
import com.scribesoul.app.ui.components.journalPages.WideLinedPage
import com.scribesoul.app.ui.components.journalPages.WideLinedSmallMarginsPage
import com.scribesoul.app.utils.DeleteWarningDialog
import com.scribesoul.app.utils.JournalNameChangeDialog
import com.scribesoul.app.viewModels.DrawingViewModel
import com.scribesoul.app.viewModels.JournalListViewModel

//fix undo redo to implement for each pages, scribble still needs revamping

@Composable
fun JournalScreen(navController: NavController, journalViewModel: JournalViewModel,journalListViewModel: JournalListViewModel, drawingViewModel: DrawingViewModel){
    val section = journalViewModel.sections.getOrNull(journalViewModel.selectedSectionIndex)
    val page = section?.pages?.getOrNull(journalViewModel.selectedPageIndex)
    BackHandler {
        journalViewModel.syncBackTo(journalListViewModel)
        navController.navigate("journalList")
    }

    val changeTool: (ToolMode) -> Unit = { newMode ->
        drawingViewModel.toolMode = newMode
        drawingViewModel.selectedItems.clear() // Remove bounding box around shapes
        drawingViewModel.selectedPaths.clear() // Remove bounding box around strokes
        drawingViewModel.colorPickerTarget = null
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            drawingViewModel.executeCommand(AddImageCommand(ImageLayer(uri = it, offset = drawingViewModel.canvasCenter.value), page!!.imageLayers), page)
        }
        changeTool(ToolMode.Lasso)
    }

    if (drawingViewModel.colorPickerTarget != null) {
        val initialColor = when (drawingViewModel.colorPickerTarget) {
            ColorPickerTarget.DRAW_STROKE -> drawingViewModel.drawColor
            ColorPickerTarget.EDIT_SELECTION -> {
                val firstSelected = (drawingViewModel.selectedItems.firstOrNull() as? Colorable)
                    ?: drawingViewModel.selectedPaths.firstOrNull()
                (firstSelected?.fill as? SolidColorFill)?.color ?: Color.Black
            }
            ColorPickerTarget.ADD_SHAPE -> Color.Red
            else -> Color.Black
        }

        ColorPickerDialog(
            initialColor = initialColor,
            onDismissRequest = { drawingViewModel.colorPickerTarget = null },
            onColorSelected = { selectedColor ->
                when (drawingViewModel.colorPickerTarget) {
                    ColorPickerTarget.DRAW_STROKE -> drawingViewModel.drawColor = selectedColor
                    ColorPickerTarget.EDIT_SELECTION -> {
                        val allTargets = drawingViewModel.selectedItems.toList() + drawingViewModel.selectedPaths.toList()
                        drawingViewModel.executeCommand(ChangeFillStyleCommand(allTargets, SolidColorFill(selectedColor)), page)
                    }
                    ColorPickerTarget.ADD_SHAPE -> {
                        drawingViewModel.pendingShapeType?.let {
                            drawingViewModel.pendingShapeFill = SolidColorFill(selectedColor)
                            changeTool(ToolMode.SHAPE) // Use helper
                        }
                    }
                    null -> {}
                }
                drawingViewModel.colorPickerTarget = null
            }
        )
    }

    if (drawingViewModel.showGradientPicker) {
        GradientPickerDialog(
            onDismissRequest = { drawingViewModel.showGradientPicker = false },
            onGradientSelected = { colors ->
                val allTargets = drawingViewModel.selectedItems.toList() + drawingViewModel.selectedPaths.toList()
                drawingViewModel.executeCommand(ChangeFillStyleCommand(allTargets, LinearGradientFill(colors)),page)
                drawingViewModel.showGradientPicker = false
            }
        )
    }

    if (journalViewModel.showChangeName.value) {
        JournalNameChangeDialog(
            onDismissRequest = {
                journalViewModel.showChangeName.value = false
            },
            onNameChange = { name ->
                journalListViewModel.changeJournalName(journalListViewModel.findJournalIndex(journalViewModel.journalId), name)
                journalViewModel.showChangeName.value = false
            },
            initial = journalListViewModel.journals[journalListViewModel.findJournalIndex(journalViewModel.journalId)].name
        )
    }

    if (journalViewModel.showDeleteWarning.value) {
        DeleteWarningDialog(
            onDismissRequest = {
                journalViewModel.showDeleteWarning.value = false
            },
            onDelete = {
                journalViewModel.showDeleteWarning.value = false
                journalListViewModel.deleteJournal(journalListViewModel.findJournalIndex(journalViewModel.journalId), navController)
            }
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFF4FFFE)
                    )
                )
            )
        ,
        contentAlignment = Alignment.TopCenter
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp).offset(y = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_left),
                    contentDescription = "Undo",
                    modifier = Modifier.size(20.dp).clickable(enabled = page?.undoStack?.isNotEmpty() == true) {
                        page?.undoStack?.removeLastOrNull()?.let {
                            it.undo()
                            page.redoStack.add(it)
                        }
                    }
                )
                Image(
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = "Redo",
                    modifier = Modifier.size(20.dp).clickable(enabled = page?.redoStack?.isNotEmpty() == true) {
                        page?.redoStack?.removeLastOrNull()?.let {
                            it.execute()
                            page.undoStack.add(it)
                        }
                    }
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.text),
                    contentDescription = "Text",
                    modifier = Modifier.size(18.dp).clickable {
                        changeTool(ToolMode.TEXT)
                    }
                )
                Image(
                    painter = painterResource(id = R.drawable.lassotool),
                    contentDescription = "Lasso",
                    modifier = Modifier.size(22.dp).clickable {
                        // Note: We don't clear selection when clicking Lasso itself,
                        // in case user wants to adjust existing selection
                        drawingViewModel.toolMode = ToolMode.Lasso
                    }
                )
                Image(
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = "Image",
                    modifier = Modifier.size(22.dp).clickable {
                        imagePickerLauncher.launch("image/*")
                    }
                )
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.shapeasset),
                        contentDescription = "Shape",
                        modifier = Modifier.size(22.dp).clickable { journalViewModel.showShapeMenu.value = true }
                    )
                    DropdownMenu(
                        expanded = journalViewModel.showShapeMenu.value,
                        onDismissRequest = { journalViewModel.showShapeMenu.value = false }
                    ) {
                        val shapes = listOf("Circle", "Rectangle", "Star", "Triangle", "Hexagon")
                        shapes.forEach { shape ->
                            DropdownMenuItem(
                                text = { Text(shape) },
                                onClick = {
                                    changeTool(ToolMode.SHAPE)
                                    drawingViewModel.pendingShapeType = shape
                                    drawingViewModel.colorPickerTarget = ColorPickerTarget.ADD_SHAPE
                                    journalViewModel.showShapeMenu.value = false
                                }
                            )
                        }
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.pencil),
                    contentDescription = "Pencil",
                    modifier = Modifier.size(22.dp).clickable {
                        changeTool(ToolMode.DRAW)
                        drawingViewModel.colorPickerTarget = ColorPickerTarget.DRAW_STROKE
                    }
                )
                Image(
                    painter = painterResource(id = R.drawable.eraser),
                    contentDescription = "Eraser",
                    modifier = Modifier.size(22.dp).clickable {
                        changeTool(ToolMode.ERASE)
                    }
                )

                // Layer Menu Logic
                Box {
                    val isLayerMenuEnabled = drawingViewModel.selectedItems.size == 1
                    Image(
                        painter = painterResource(id = R.drawable.layer),
                        contentDescription = "Layer",
                        modifier = Modifier.size(22.dp).clickable(enabled = isLayerMenuEnabled) {
                            journalViewModel.showLayerMenu.value = true
                        },
                        alpha = if (isLayerMenuEnabled) 1f else 0.4f
                    )
                    DropdownMenu(
                        expanded = journalViewModel.showLayerMenu.value,
                        onDismissRequest = { journalViewModel.showLayerMenu.value = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Bring to Front") },
                            onClick = {
                                if (isLayerMenuEnabled) {
                                    drawingViewModel.executeCommand(
                                        LayeringCommand(
                                            drawingViewModel.selectedItems.first(),
                                            listOf(page!!.texts, page.shapes, page.imageLayers, drawingViewModel.groups),
                                            LayerDirection.TO_FRONT
                                        ), page
                                    )
                                }
                                journalViewModel.showLayerMenu.value = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Send to Back") },
                            onClick = {
                                if (isLayerMenuEnabled) {
                                    drawingViewModel.executeCommand(
                                        LayeringCommand(
                                            drawingViewModel.selectedItems.first(),
                                            listOf(page!!.texts, page.shapes, page.imageLayers, drawingViewModel.groups),
                                            LayerDirection.TO_BACK
                                        ), page
                                    )
                                }
                                journalViewModel.showLayerMenu.value = false
                            }
                        )
                    }
                }
                Box{
                    Image(painter = painterResource(id = R.drawable.dot3), contentDescription = "More", modifier = Modifier.clickable{
                        journalViewModel.showJournalMenu.value = true
                    }.size(22.dp))
                    DropdownMenu(
                        expanded = journalViewModel.showJournalMenu.value,
                        onDismissRequest = { journalViewModel.showJournalMenu.value = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit Journal Name") },
                            onClick = {
                                journalViewModel.showChangeName.value = true
                                journalViewModel.showJournalMenu.value = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Exit") },
                            onClick = {
                                navController.popBackStack()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                journalViewModel.showDeleteWarning.value = true
                                journalViewModel.showJournalMenu.value = false
                            }
                        )
                    }
                }

            }

        }



        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(fraction = 0.75f).offset(250.dp)
            ) {

                    LazyColumn {
                        itemsIndexed(journalViewModel.sections){ index, section ->
                                Row(
                                    modifier = Modifier

                                        .width(94.dp)
                                        .height(59.dp)
                                        .background(color = section.color, shape = RoundedCornerShape(size = 23.dp))
                                        .clickable(onClick = {
                                            journalViewModel.changeSelectedPageIndex(0)
                                            journalViewModel.changeSelectedSection(index)
                                        })
                                ) {

                                }
                        }


                }


            }

            if (section?.type == SectionType.Creation) {
                CreationPage(
                    onAddPlainPage = { journalViewModel.addSection(SectionType.Plain) },
                    onAddHabitsPage = { journalViewModel.addSection(SectionType.Calendar) },
                    onAddCalendarPage = { journalViewModel.addSection(SectionType.Calendar) },
                    onAddTodoPage = { journalViewModel.addSection(SectionType.Todo)},
                    onAddMoodPage = { journalViewModel.addSection(SectionType.Mood) },
                    onAddDottedPage ={ journalViewModel.addSection(SectionType.Dotted)},
                    onAddWideLinedPage = {journalViewModel.addSection(SectionType.WideLined)},
                    onAddLargeGridPage = {journalViewModel.addSection(SectionType.LargeGrid)},
                    onAddSmallGridPage = {journalViewModel.addSection(SectionType.SmallGrid)},
                    onAddNarrowLinedPage = {journalViewModel.addSection(SectionType.NarrowLined)},
                    onAddWideLinedLargeMarginPage = {journalViewModel.addSection(SectionType.WideLinedLargeMargin)},
                    onAddNarrowLinedLargeMarginPage = {journalViewModel.addSection(SectionType.NarrowLinedLargeMargin)},
                    onAddWideLinedSmallMarginPage = {journalViewModel.addSection(SectionType.WideLinedSmallMargin)},
                    onAddNarrowLinedSmallMarginPage = {journalViewModel.addSection(SectionType.NarrowLinedSmallMargin)}
                )
            } else if (page != null) {
                key("${section?.id}-${page?.id}") {
                    when (page) {
                        is JournalPage.PlainPage -> PlainPage(
                            journalViewModel,
                            drawingViewModel,
                            page,
                            section.color
                        )

                        is JournalPage.HabitsPage -> HabitsPage(
                            section.color,
                            page,
                            journalViewModel,
                            drawingViewModel
                        )

                        is JournalPage.CalendarPage -> CalendarPage(
                            page = page,
                            color = section.color
                        )

                        is JournalPage.MoodsPage -> MoodPage(page, section.color)
                        is JournalPage.TodoPage -> TodoPage(page)
                        is JournalPage.WideLinedPage -> WideLinedPage(
                            journalViewModel,
                            drawingViewModel,
                            page,
                            section.color
                        )

                        is JournalPage.WideLinedLargeMarginPage -> WideLinedLargeMarginsPage(
                            journalViewModel,
                            drawingViewModel,
                            page,
                            section.color
                        )

                        is JournalPage.WideLinedSmallMarginPage -> WideLinedSmallMarginsPage(
                            journalViewModel,
                            drawingViewModel,
                            page,
                            section.color
                        )

                        is JournalPage.NarrowLinedPage -> NarrowLinedPage(
                            page,
                            section.color,
                            drawingViewModel,
                            journalViewModel
                        )

                        is JournalPage.NarrowLinedSmallMarginPage -> NarrowLinedSmallMarginPage(
                            page,
                            section.color,
                            drawingViewModel,
                            journalViewModel
                        )

                        is JournalPage.NarrowLinedLargeMarginPage -> NarrowLinedLargeMarginPage(
                            page,
                            section.color,
                            drawingViewModel,
                            journalViewModel
                        )

                        is JournalPage.SmallGridPage -> SmallGridPage(
                            page,
                            section.color,
                            drawingViewModel,
                            journalViewModel
                        )

                        is JournalPage.LargeGridPage -> LargeGridPage(
                            page,
                            section.color,
                            drawingViewModel,
                            journalViewModel
                        )

                        is JournalPage.DottedPage -> DottedPage(
                            page,
                            section.color,
                            drawingViewModel,
                            journalViewModel
                        )

                        else -> TODO()
                    }
                }
            }





        }
        if (drawingViewModel.selectedItems.isNotEmpty() || drawingViewModel.selectedPaths.isNotEmpty()) {
            Box(modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)
                .wrapContentWidth()
            ) {
                PropertiesToolbar(
                    selectedItems = drawingViewModel.selectedItems,
                    selectedPaths = drawingViewModel.selectedPaths,
                    executeCommand = { drawingViewModel.executeCommand(it, page) },
                    onClearSelection = {
                        drawingViewModel.selectedItems.clear()
                        drawingViewModel.selectedPaths.clear()
                    },
                    allLists = listOf(page!!.texts, page.shapes, page.imageLayers, drawingViewModel.groups),
                    onShowColorPicker = { drawingViewModel.colorPickerTarget = ColorPickerTarget.EDIT_SELECTION },
                    onShowGradientPicker = { drawingViewModel.showGradientPicker = true }
                )
            }
        }

        AnimatedVisibility(
            visible = drawingViewModel.toolMode == ToolMode.DRAW || drawingViewModel.toolMode == ToolMode.ERASE,
            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 5.dp)
        ) {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f), RoundedCornerShape(8.dp)).padding(horizontal = 0.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (drawingViewModel.toolMode == ToolMode.DRAW) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painterResource(id = R.drawable.pencil), contentDescription = "Pencil", Modifier.size(24.dp))
                        Text("Draw", fontSize = 12.sp)
                        Slider(value = drawingViewModel.drawThickness, onValueChange = { drawingViewModel.drawThickness = it }, valueRange = 1f..50f, modifier = Modifier.width(60.dp).height(150.dp).rotate(270f))
                        Text("${drawingViewModel.drawThickness.toInt()}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
                if (drawingViewModel.toolMode == ToolMode.ERASE) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painterResource(id = R.drawable.eraser), contentDescription = "Eraser", Modifier.size(24.dp))
                        Text("Erase", fontSize = 12.sp)
                        Slider(value = drawingViewModel.eraseThickness, onValueChange = { drawingViewModel.eraseThickness = it }, valueRange = 10f..100f, modifier = Modifier.width(60.dp).height(150.dp).rotate(270f))
                        Text("${drawingViewModel.eraseThickness.toInt()}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

//        Column(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(bottom = 0.dp),
//            verticalArrangement = Arrangement.spacedBy(1.dp)
//        ) {
//
//            BottomBarJournal(navController)
//        }

    }


}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun JournalPreview() {
    JournalScreen(navController = NavController(LocalContext.current), journalViewModel = viewModel(factory = JournalViewModel.Factory), journalListViewModel = viewModel(factory = JournalListViewModel.Factory), drawingViewModel = viewModel(factory = DrawingViewModel.Factory))
}