package com.example.scribesoul.ui.screens



import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
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

//fix undo redo to implement for each pages, scribble still needs revamping

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JournalScreen(navController: NavController, journalViewModel: JournalViewModel){
    val section = journalViewModel.sections.getOrNull(journalViewModel.selectedSectionIndex)
    val page = section?.pages?.getOrNull(journalViewModel.selectedPageIndex)


    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            journalViewModel.executeCommand(AddImageCommand(ImageLayer(uri = it, offset = journalViewModel.canvasCenter.value), journalViewModel.imageLayers), page)
        }
    }

    if (journalViewModel.colorPickerTarget != null) {
        val initialColor = when (journalViewModel.colorPickerTarget) {
            ColorPickerTarget.DRAW_STROKE -> journalViewModel.drawColor
            ColorPickerTarget.EDIT_SELECTION -> {
                val firstSelected = (journalViewModel.selectedItems.firstOrNull() as? Colorable)
                    ?: journalViewModel.selectedPaths.firstOrNull()
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
                    ColorPickerTarget.DRAW_STROKE -> journalViewModel.drawColor = selectedColor
                    ColorPickerTarget.EDIT_SELECTION -> {
                        val allTargets = journalViewModel.selectedItems.toList() + journalViewModel.selectedPaths.toList()
                        journalViewModel.executeCommand(ChangeFillStyleCommand(allTargets, SolidColorFill(selectedColor)), page)
                    }
                    ColorPickerTarget.ADD_SHAPE -> {
                        journalViewModel.pendingShapeType?.let { shapeType ->
                            journalViewModel.executeCommand(AddShapeCommand(ShapeItem(shapeType, journalViewModel.canvasCenter.value, fill = SolidColorFill(selectedColor)), journalViewModel.shapes), page)
                        }
                    }
                    null -> {}
                }
                journalViewModel.colorPickerTarget = null
                journalViewModel.pendingShapeType = null
            }
        )
    }

    if (journalViewModel.showGradientPicker) {
        GradientPickerDialog(
            onDismissRequest = { journalViewModel.showGradientPicker = false },
            onGradientSelected = { colors ->
                val allTargets = journalViewModel.selectedItems.toList() + journalViewModel.selectedPaths.toList()
                journalViewModel.executeCommand(ChangeFillStyleCommand(allTargets, LinearGradientFill(colors)),page)
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
                Image(painter = painterResource(id = R.drawable.lassotool), contentDescription = "Lasso", modifier = Modifier.size(22.dp).clickable { journalViewModel.toolMode = ToolMode.Lasso })
                Image(painter = painterResource(id = R.drawable.image), contentDescription = "Image", modifier = Modifier.size(22.dp).clickable { imagePickerLauncher.launch("image/*") })
                Box {
                    Image(painter = painterResource(id = R.drawable.shapeasset), contentDescription = "Shape", modifier = Modifier.size(22.dp).clickable { journalViewModel.showShapeMenu.value = true })
                    DropdownMenu(expanded = journalViewModel.showShapeMenu.value, onDismissRequest = { journalViewModel.showShapeMenu.value = false }) {
                        DropdownMenuItem(onClick = { journalViewModel.pendingShapeType = "Circle"; journalViewModel.colorPickerTarget = ColorPickerTarget.ADD_SHAPE; journalViewModel.showShapeMenu.value = false }, text = { Text("Circle") })
                        DropdownMenuItem(onClick = { journalViewModel.pendingShapeType = "Rectangle"; journalViewModel.colorPickerTarget = ColorPickerTarget.ADD_SHAPE; journalViewModel.showShapeMenu.value = false }, text = { Text("Rectangle") })
                        DropdownMenuItem(onClick = { journalViewModel.pendingShapeType = "Star"; journalViewModel.colorPickerTarget = ColorPickerTarget.ADD_SHAPE; journalViewModel.showShapeMenu.value = false }, text = { Text("Star") })
                        DropdownMenuItem(onClick = { journalViewModel.pendingShapeType = "Triangle"; journalViewModel.colorPickerTarget = ColorPickerTarget.ADD_SHAPE; journalViewModel.showShapeMenu.value = false }, text = { Text("Triangle") })
                        DropdownMenuItem(onClick = { journalViewModel.pendingShapeType = "Hexagon"; journalViewModel.colorPickerTarget = ColorPickerTarget.ADD_SHAPE; journalViewModel.showShapeMenu.value = false }, text = { Text("Hexagon") })
                    }
                }
                Image(painter = painterResource(id = R.drawable.pencil), contentDescription = "Pencil", modifier = Modifier.size(22.dp).clickable {
                    journalViewModel.toolMode = ToolMode.DRAW; journalViewModel.colorPickerTarget = ColorPickerTarget.DRAW_STROKE
                })
                Image(painter = painterResource(id = R.drawable.eraser), contentDescription = "Eraser", modifier = Modifier.size(22.dp).clickable { journalViewModel.toolMode = ToolMode.ERASE })
                Box {
                    val isLayerMenuEnabled = journalViewModel.selectedItems.size == 1
                    Image(
                        painter = painterResource(id = R.drawable.layer), contentDescription = "Layer",
                        modifier = Modifier.size(22.dp).clickable(enabled = isLayerMenuEnabled) { journalViewModel.showLayerMenu.value = true },
                        alpha = if (isLayerMenuEnabled) 1f else 0.4f
                    )
                    DropdownMenu(expanded = journalViewModel.showLayerMenu.value, onDismissRequest = { journalViewModel.showLayerMenu.value = false }) {
                        DropdownMenuItem(text = { Text("Bring to Front") }, onClick = {
                            if (isLayerMenuEnabled) {
                                journalViewModel.executeCommand(LayeringCommand(journalViewModel.selectedItems.first(), listOf(journalViewModel.texts, journalViewModel.shapes, journalViewModel.imageLayers, journalViewModel.groups), LayerDirection.TO_FRONT), page)
                            }; journalViewModel.showLayerMenu.value = false
                        })
                        DropdownMenuItem(text = { Text("Send to Back") }, onClick = {
                            if (isLayerMenuEnabled) {
                                journalViewModel.executeCommand(LayeringCommand(journalViewModel.selectedItems.first(), listOf(journalViewModel.texts, journalViewModel.shapes, journalViewModel.imageLayers, journalViewModel.groups), LayerDirection.TO_BACK), page)
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
                .offset(x = 5.dp, y= -20.dp)
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
                    onAddHabitsPage = { journalViewModel.addSection(SectionType.Habits) },
                    onAddCalendarPage = { journalViewModel.addSection(SectionType.Calendar) }
                )
            } else if (page != null) {
                when (page) {
                    is JournalPage.PlainPage -> PlainPage(journalViewModel, page, section.color)
                    is JournalPage.HabitsPage -> HabitsPage(section.color)
                    is JournalPage.CalendarPage -> CalendarPage(ToolMode.DRAW)
                }
            }





        }

        AnimatedVisibility(
            visible = journalViewModel.toolMode == ToolMode.DRAW || journalViewModel.toolMode == ToolMode.ERASE,
            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 5.dp)
        ) {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f), RoundedCornerShape(8.dp)).padding(horizontal = 0.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (journalViewModel.toolMode == ToolMode.DRAW) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painterResource(id = R.drawable.pencil), contentDescription = "Pencil", Modifier.size(24.dp))
                        Text("Draw", fontSize = 12.sp)
                        Slider(value = journalViewModel.drawThickness, onValueChange = { journalViewModel.drawThickness = it }, valueRange = 1f..50f, modifier = Modifier.width(60.dp).height(150.dp).rotate(270f))
                        Text("${journalViewModel.drawThickness.toInt()}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
                if (journalViewModel.toolMode == ToolMode.ERASE) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painterResource(id = R.drawable.eraser), contentDescription = "Eraser", Modifier.size(24.dp))
                        Text("Erase", fontSize = 12.sp)
                        Slider(value = journalViewModel.eraseThickness, onValueChange = { journalViewModel.eraseThickness = it }, valueRange = 10f..100f, modifier = Modifier.width(60.dp).height(150.dp).rotate(270f))
                        Text("${journalViewModel.eraseThickness.toInt()}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
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

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPathFromOffsets(
    offsets: List<Offset>,
    mode: ToolMode
) {
    if (offsets.size < 2) return

    val path = Path().apply {
        moveTo(offsets.first().x, offsets.first().y)
        for (i in 1 until offsets.size) {
            lineTo(offsets[i].x, offsets[i].y)
        }
    }

    drawPath(
        path = path,
        color = if (mode == ToolMode.DRAW) Color.Black else Color.Transparent,
        style = Stroke(width = if (mode == ToolMode.DRAW) 4f else 36f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        ,
        blendMode = if (mode == ToolMode.ERASE) BlendMode.Clear else BlendMode.SrcOver
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Journalview() {
    JournalScreen(navController = NavController(LocalContext.current), journalViewModel = viewModel(factory = JournalViewModel.Factory))
}