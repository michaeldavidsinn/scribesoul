package com.example.scribesoul.ui.screens



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
import com.example.scribesoul.R
import com.example.scribesoul.ui.components.journalPages.CreationPage
import com.example.scribesoul.ui.components.journalPages.PlainPage
import com.example.scribesoul.viewModels.JournalViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scribesoul.commands.AddImageCommand
import com.example.scribesoul.commands.AddShapeCommand
import com.example.scribesoul.commands.ChangeFillStyleCommand
import com.example.scribesoul.commands.LayerDirection
import com.example.scribesoul.commands.LayeringCommand
import com.example.scribesoul.models.Colorable
import com.example.scribesoul.models.ImageLayer
import com.example.scribesoul.models.LinearGradient as LinearGradientFill
import com.example.scribesoul.models.ShapeItem
import com.example.scribesoul.models.*
import com.example.scribesoul.models.SolidColor as SolidColorFill
import com.example.scribesoul.models.ToolMode
import com.example.scribesoul.ui.components.journalPages.CalendarPage
import com.example.scribesoul.ui.components.journalPages.HabitsPage
import com.example.scribesoul.ui.components.journalPages.LargeGridPage
import com.example.scribesoul.ui.components.journalPages.MoodPage
import com.example.scribesoul.ui.components.journalPages.NarrowLinedLargeMarginPage
import com.example.scribesoul.ui.components.journalPages.NarrowLinedPage
import com.example.scribesoul.ui.components.journalPages.NarrowLinedSmallMarginPage
import com.example.scribesoul.ui.components.journalPages.SmallGridPage
import com.example.scribesoul.ui.components.journalPages.TodoPage
import com.example.scribesoul.ui.components.journalPages.WideLinedLargeMarginsPage
import com.example.scribesoul.ui.components.journalPages.WideLinedPage
import com.example.scribesoul.ui.components.journalPages.WideLinedSmallMarginsPage
import com.example.scribesoul.viewModels.DrawingViewModel
import com.example.scribesoul.viewModels.JournalListViewModel

//fix undo redo to implement for each pages, scribble still needs revamping

@Composable
fun JournalScreen(navController: NavController, journalViewModel: JournalViewModel,journalListViewModel: JournalListViewModel, drawingViewModel: DrawingViewModel){
    val section = journalViewModel.sections.getOrNull(journalViewModel.selectedSectionIndex)
    val page = section?.pages?.getOrNull(journalViewModel.selectedPageIndex)
    BackHandler {
        journalViewModel.syncBackTo(journalListViewModel)
        navController.navigate("journalList")
    }


    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            drawingViewModel.executeCommand(AddImageCommand(ImageLayer(uri = it, offset = drawingViewModel.canvasCenter.value), drawingViewModel.imageLayers), page)
        }
    }

    if (journalViewModel.colorPickerTarget != null) {
        val initialColor = when (journalViewModel.colorPickerTarget) {
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
            onDismissRequest = { journalViewModel.colorPickerTarget = null },
            onColorSelected = { selectedColor ->
                when (journalViewModel.colorPickerTarget) {
                    ColorPickerTarget.DRAW_STROKE -> drawingViewModel.drawColor = selectedColor
                    ColorPickerTarget.EDIT_SELECTION -> {
                        val allTargets = drawingViewModel.selectedItems.toList() + drawingViewModel.selectedPaths.toList()
                        drawingViewModel.executeCommand(ChangeFillStyleCommand(allTargets, SolidColorFill(selectedColor)), page)
                    }
                    ColorPickerTarget.ADD_SHAPE -> {
                       drawingViewModel.pendingShapeType?.let { shapeType ->
                           drawingViewModel.pendingShapeFill = SolidColorFill(selectedColor)
                            // set the tool mode so next drag will create shape at click
                           drawingViewModel.toolMode = ToolMode.SHAPE
                            // Do NOT add to shapes here — we only set pending fill and shape type.
                        }
                    }
                    null -> {}
                }
                journalViewModel.colorPickerTarget = null
//                drawingViewModel.pendingShapeType = null
            }
        )
    }

    if (journalViewModel.showGradientPicker) {
        GradientPickerDialog(
            onDismissRequest = { journalViewModel.showGradientPicker = false },
            onGradientSelected = { colors ->
                val allTargets = drawingViewModel.selectedItems.toList() + drawingViewModel.selectedPaths.toList()
                drawingViewModel.executeCommand(ChangeFillStyleCommand(allTargets, LinearGradientFill(colors)),page)
                journalViewModel.showGradientPicker = false
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
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp).offset(y = 40.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // -- FIX: Logika Undo/Redo yang benar --
                Image(
                    painter = painterResource(id = R.drawable.arrow_left),
                    contentDescription = "Undo",
                    modifier = Modifier.size(20.dp).clickable(enabled = page?.undoStack?.isNotEmpty()==true) {
                        val commandToUndo = page?.undoStack?.removeLastOrNull()
                        commandToUndo?.let {
                            it.undo()
                            page.redoStack.add(it)
                        }
                    }
                )
                Image(painter = painterResource(id = R.drawable.arrow_right), contentDescription = "Redo",
                    modifier = Modifier.size(20.dp).clickable(enabled = page?.redoStack?.isNotEmpty() == true) {
                        val commandToRedo = page?.redoStack?.removeLastOrNull()
                        commandToRedo?.let {
                            it.execute()
                            page.undoStack.add(it)
                        }
                    })
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(painter = painterResource(id = R.drawable.text), contentDescription = "Text", modifier = Modifier.size(18.dp).clickable { journalViewModel.isAddingText = true })
                Image(painter = painterResource(id = R.drawable.lassotool), contentDescription = "Lasso", modifier = Modifier.size(22.dp).clickable { drawingViewModel.toolMode = ToolMode.Lasso })
                Image(painter = painterResource(id = R.drawable.image), contentDescription = "Image", modifier = Modifier.size(22.dp).clickable { imagePickerLauncher.launch("image/*") })
                Box {
                    Image(painter = painterResource(id = R.drawable.shapeasset), contentDescription = "Shape", modifier = Modifier.size(22.dp).clickable { journalViewModel.showShapeMenu.value = true })
                    DropdownMenu(expanded = journalViewModel.showShapeMenu.value, onDismissRequest = { journalViewModel.showShapeMenu.value = false }) {
                        DropdownMenuItem(onClick = { drawingViewModel.pendingShapeType = "Circle"; journalViewModel.colorPickerTarget = ColorPickerTarget.ADD_SHAPE; journalViewModel.showShapeMenu.value = false; drawingViewModel.toolMode = ToolMode.SHAPE }, text = { Text("Circle") })
                        DropdownMenuItem(onClick = { drawingViewModel.pendingShapeType = "Rectangle"; journalViewModel.colorPickerTarget = ColorPickerTarget.ADD_SHAPE; journalViewModel.showShapeMenu.value = false; drawingViewModel.toolMode = ToolMode.SHAPE }, text = { Text("Rectangle") })
                        DropdownMenuItem(onClick = { drawingViewModel.pendingShapeType = "Star"; journalViewModel.colorPickerTarget = ColorPickerTarget.ADD_SHAPE; journalViewModel.showShapeMenu.value = false; drawingViewModel.toolMode = ToolMode.SHAPE }, text = { Text("Star") })
                        DropdownMenuItem(onClick = { drawingViewModel.pendingShapeType = "Triangle"; journalViewModel.colorPickerTarget = ColorPickerTarget.ADD_SHAPE; journalViewModel.showShapeMenu.value = false; drawingViewModel.toolMode = ToolMode.SHAPE }, text = { Text("Triangle") })
                        DropdownMenuItem(onClick = { drawingViewModel.pendingShapeType = "Hexagon"; journalViewModel.colorPickerTarget = ColorPickerTarget.ADD_SHAPE; journalViewModel.showShapeMenu.value = false; drawingViewModel.toolMode = ToolMode.SHAPE }, text = { Text("Hexagon") })
                    }
                }
                Image(painter = painterResource(id = R.drawable.pencil), contentDescription = "Pencil", modifier = Modifier.size(22.dp).clickable {
                    drawingViewModel.toolMode = ToolMode.DRAW; journalViewModel.colorPickerTarget = ColorPickerTarget.DRAW_STROKE
                })
                Image(painter = painterResource(id = R.drawable.eraser), contentDescription = "Eraser", modifier = Modifier.size(22.dp).clickable { drawingViewModel.toolMode = ToolMode.ERASE })
                Box {
                    val isLayerMenuEnabled = drawingViewModel.selectedItems.size == 1
                    Image(
                        painter = painterResource(id = R.drawable.layer), contentDescription = "Layer",
                        modifier = Modifier.size(22.dp).clickable(enabled = isLayerMenuEnabled) { journalViewModel.showLayerMenu.value = true },
                        alpha = if (isLayerMenuEnabled) 1f else 0.4f
                    )
                    DropdownMenu(expanded = journalViewModel.showLayerMenu.value, onDismissRequest = { journalViewModel.showLayerMenu.value = false }) {
                        DropdownMenuItem(text = { Text("Bring to Front") }, onClick = {
                            if (isLayerMenuEnabled) {
                                drawingViewModel.executeCommand(LayeringCommand(drawingViewModel.selectedItems.first(), listOf(drawingViewModel.texts, drawingViewModel.shapes, drawingViewModel.imageLayers, drawingViewModel.groups), LayerDirection.TO_FRONT), page)
                            }; journalViewModel.showLayerMenu.value = false
                        })
                        DropdownMenuItem(text = { Text("Send to Back") }, onClick = {
                            if (isLayerMenuEnabled) {
                                drawingViewModel.executeCommand(LayeringCommand(drawingViewModel.selectedItems.first(), listOf(drawingViewModel.texts, drawingViewModel.shapes, drawingViewModel.imageLayers, drawingViewModel.groups), LayerDirection.TO_BACK), page)
                            }; journalViewModel.showLayerMenu.value = false
                        })
                    }
                }
                Image(painter = painterResource(id = R.drawable.dot3), contentDescription = "More", modifier = Modifier.size(22.dp))
        }

        }



        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = 5.dp, y= (-20).dp)
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

                        else -> TODO()
                    }
                }
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

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 0.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {

            BottomBarJournal(navController)
        }

    }


}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun JournalPreview() {
    JournalScreen(navController = NavController(LocalContext.current), journalViewModel = viewModel(factory = JournalViewModel.Factory), journalListViewModel = viewModel(factory = JournalListViewModel.Factory), drawingViewModel = viewModel(factory = DrawingViewModel.Factory))
}