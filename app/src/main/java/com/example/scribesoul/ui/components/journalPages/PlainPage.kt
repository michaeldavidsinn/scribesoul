package com.example.scribesoul.ui.components.journalPages

import JournalPage
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scribesoul.R
import com.example.scribesoul.commands.AddDrawableCommand
import com.example.scribesoul.commands.AddTextCommand
import com.example.scribesoul.commands.EraseCommand
import com.example.scribesoul.models.ToolMode
import com.example.scribesoul.utils.distance
import com.example.scribesoul.utils.isMovableInPolygon
import com.example.scribesoul.utils.isPointInPolygon
import kotlin.math.abs
import com.example.scribesoul.models.*
import com.example.scribesoul.ui.screens.drawPathFromFill
import com.example.scribesoul.utils.drawSelectionBorder
import com.example.scribesoul.viewModels.JournalViewModel
import com.example.scribesoul.models.SolidColor as SolidColorFill

//(0XFFFFFEE4)

@Composable
fun PlainPage(journalViewModel: JournalViewModel, page: JournalPage.PlainPage, color: Color){
    val currentPath = remember { mutableStateListOf<Offset>() }
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .background(color, shape = RoundedCornerShape(size = 23.dp))
            .height(640.dp)
            .fillMaxWidth(fraction=0.8f)
            .clip(RoundedCornerShape(23.dp))
            .clipToBounds()
            .pointerInput(journalViewModel.toolMode, journalViewModel.drawThickness, journalViewModel.eraseThickness, journalViewModel.drawColor) {
                detectDragGestures(
                    onDragStart = { currentPath.clear(); currentPath.add(it) },
                    onDrag = { change, _ -> currentPath.add(change.position) },
                    onDragEnd = {
                        when (journalViewModel.toolMode) {
                            ToolMode.ERASE -> {
                                val originalPaths = page.paths.toList()
                                val pathsAfterErase = mutableListOf<DrawablePath>()
                                originalPaths.forEach { drawablePath ->
                                    if (drawablePath.toolMode == ToolMode.Highlighter) {
                                        pathsAfterErase.add(drawablePath)
                                        return@forEach
                                    }
                                    val pointErasureStatus = drawablePath.offsets.map { point ->
                                        currentPath.any { eraserPoint -> distance(point, eraserPoint) < journalViewModel.eraseThickness }
                                    }
                                    var currentSegment = mutableListOf<Offset>()
                                    pointErasureStatus.forEachIndexed { index, isErased ->
                                        if (!isErased) {
                                            currentSegment.add(drawablePath.offsets[index])
                                        } else {
                                            if (currentSegment.size > 1) {
                                                pathsAfterErase.add(drawablePath.copy(offsets = currentSegment.toList()))
                                            }
                                            currentSegment = mutableListOf()
                                        }
                                    }
                                    if (currentSegment.size > 1) {
                                        pathsAfterErase.add(drawablePath.copy(offsets = currentSegment.toList()))
                                    }
                                }
                                if (originalPaths != pathsAfterErase) {
                                   journalViewModel.executeCommand(EraseCommand(originalPaths, pathsAfterErase, page.paths), page)
                                }
                            }
                            ToolMode.Lasso -> {
                                if (currentPath.size > 2) {
                                    val polygon = currentPath.toList()
                                    journalViewModel.selectedItems.clear()
                                    journalViewModel.selectedPaths.clear()
                                    val allMovables = journalViewModel.texts + journalViewModel.shapes + journalViewModel.imageLayers + journalViewModel.groups
                                    allMovables.forEach { item ->
                                        if (isMovableInPolygon(item, polygon, density)) {
                                          journalViewModel.selectedItems.add(item)
                                        }
                                    }
                                    page.paths.forEach { path ->
                                        if (path.offsets.any { point -> isPointInPolygon(point, polygon) }) {
                                           journalViewModel.selectedPaths.add(path)
                                        }
                                    }
                                }
                            }
                            else -> {
                                if (currentPath.isNotEmpty()) {
                                    val newPath = DrawablePath(
                                        offsets = currentPath.toList(),
                                        toolMode = journalViewModel.toolMode,
                                        thickness = journalViewModel.drawThickness,
                                        fill =  SolidColorFill(if (journalViewModel.toolMode == ToolMode.Highlighter) Color.Yellow.copy(alpha = 0.5f) else journalViewModel.drawColor)
                                    )
                                   journalViewModel.executeCommand(AddDrawableCommand(newPath, page.paths), page)
                                }
                            }
                        }
                        currentPath.clear()
                    }
                )
            }

    ){

        Canvas(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .pointerInput(journalViewModel.toolMode, journalViewModel.drawThickness, journalViewModel.eraseThickness, journalViewModel.drawColor) {
                detectDragGestures(
                    onDragStart = { currentPath.clear(); currentPath.add(it) },
                    onDrag = { change, _ -> currentPath.add(change.position) },
                    onDragEnd = {
                        when (journalViewModel.toolMode) {
                            ToolMode.ERASE -> {
                                val originalPaths = page.paths.toList()
                                val pathsAfterErase = mutableListOf<DrawablePath>()
                                originalPaths.forEach { drawablePath ->
                                    if (drawablePath.toolMode == ToolMode.Highlighter) {
                                        pathsAfterErase.add(drawablePath)
                                        return@forEach
                                    }
                                    val pointErasureStatus = drawablePath.offsets.map { point ->
                                        currentPath.any { eraserPoint -> distance(point, eraserPoint) < journalViewModel.eraseThickness }
                                    }
                                    var currentSegment = mutableListOf<Offset>()
                                    pointErasureStatus.forEachIndexed { index, isErased ->
                                        if (!isErased) {
                                            currentSegment.add(drawablePath.offsets[index])
                                        } else {
                                            if (currentSegment.size > 1) {
                                                pathsAfterErase.add(drawablePath.copy(offsets = currentSegment.toList()))
                                            }
                                            currentSegment = mutableListOf()
                                        }
                                    }
                                    if (currentSegment.size > 1) {
                                        pathsAfterErase.add(drawablePath.copy(offsets = currentSegment.toList()))
                                    }
                                }
                                if (originalPaths != pathsAfterErase) {
                                   journalViewModel.executeCommand(EraseCommand(originalPaths, pathsAfterErase, page.paths), page)
                                }
                            }
                            ToolMode.Lasso -> {
                                if (currentPath.size > 2) {
                                    val polygon = currentPath.toList()
                                   journalViewModel.selectedItems.clear()
                                   journalViewModel.selectedPaths.clear()
                                    val allMovables = journalViewModel.texts + journalViewModel.shapes + journalViewModel.imageLayers + journalViewModel.groups
                                    allMovables.forEach { item ->
                                        if (isMovableInPolygon(item, polygon, density)) {
                                           journalViewModel.selectedItems.add(item)
                                        }
                                    }
                                   page.paths.forEach { path ->
                                        if (path.offsets.any { point -> isPointInPolygon(point, polygon) }) {
                                            journalViewModel.selectedPaths.add(path)
                                        }
                                    }
                                }
                            }
                            else -> {
                                if (currentPath.isNotEmpty()) {
                                    val newPath = DrawablePath(
                                        offsets = currentPath.toList(),
                                        toolMode = journalViewModel.toolMode,
                                        thickness = journalViewModel.drawThickness,
                                        fill = SolidColorFill(if (journalViewModel.toolMode == ToolMode.Highlighter) Color.Yellow.copy(alpha = 0.5f) else journalViewModel.drawColor)
                                    )
                                   journalViewModel.executeCommand(AddDrawableCommand(newPath,page.paths), page)
                                }
                            }
                        }
                        currentPath.clear()
                    }
                )
            }
            .pointerInput(journalViewModel.isAddingText) {
//                detectTapGestures { offset ->
//                    if (journalViewModel.isAddingText) {
//                        journalViewModel.executeCommand(journalViewModel.AddTextCommand(EditableText(text = "New Text", offset = offset), texts))
//                        isAddingText = false
//                    }
//                    texts.forEach { it.isEditing = false }
//                }
            }
        )
        {



           page.paths.forEach { path ->
                drawPathFromFill(path.offsets, path.fill, path.toolMode, path.thickness)
            }
            drawPathFromFill(currentPath, SolidColorFill(journalViewModel.drawColor),journalViewModel.toolMode,journalViewModel.drawThickness)
           journalViewModel.selectedPaths.forEach { drawSelectionBorder(it.offsets) }
            if (journalViewModel.toolMode == ToolMode.ERASE && currentPath.isNotEmpty()) {
                val lastPoint = currentPath.last()
                drawCircle(color = Color.LightGray.copy(alpha = 0.5f), center = lastPoint, radius = journalViewModel.eraseThickness, style = Stroke(width = 2.dp.toPx()))
            }
           journalViewModel.guideLines.forEach { line ->
                drawLine(color = Color.Cyan, start = line.start, end = line.end, strokeWidth = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("S Project",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontWeight = FontWeight(600),
                    color = Color(0XFF2B395B),
                    letterSpacing = 1.sp,
                )
                )

            Text("+",
                modifier = Modifier.clickable{
                    journalViewModel.addPageToSection(journalViewModel.selectedSectionIndex)
                },
                style = TextStyle(
                    fontSize = 50.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontWeight = FontWeight(600),
                    color = Color(0XFF2B395B),
                    letterSpacing = 1.sp,
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("<",
                modifier = Modifier.clickable{
                    if(journalViewModel.selectedPageIndex==0){
                        journalViewModel.changeSelectedPageIndex(0)
                    }else{
                        journalViewModel.changeSelectedPageIndex(journalViewModel.selectedPageIndex-1)
                    }
                },
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontWeight = FontWeight(600),
                    color = Color(0XFF2B395B),
                    letterSpacing = 1.sp,
                ))
            Text(">",
                modifier = Modifier.clickable{
                    if(journalViewModel.selectedPageIndex==(journalViewModel.sections[journalViewModel.selectedSectionIndex].pages.size-1)){
                        journalViewModel.changeSelectedPageIndex(journalViewModel.selectedPageIndex)
                    }else{
                        journalViewModel.changeSelectedPageIndex(journalViewModel.selectedPageIndex+1)
                    }
                },
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontWeight = FontWeight(600),
                    color = Color(0XFF2B395B),
                    letterSpacing = 1.sp,
                ))
        }
            

    }

}

// Fungsi menggambar path dari list Offset, tergantung mode aktif
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPathFromOffsets(
    offsets: List<Offset>,
    mode: ToolMode
) {
    if (offsets.size < 2) return

    val smoothedPath = Path().apply {
        moveTo(offsets.first().x, offsets.first().y)
        val smoothness = 5
        for (i in 1 until offsets.size) {
            val from = offsets[i -1]
            val to = offsets[i]
            val dx = abs(from.x - to.x)
            val dy = abs(from.y - to.y)
            if(dx >= smoothness || dy >= smoothness){
                quadraticTo(
                    x1 = (from.x + to.x) / 2f,
                    y1 = (from.y + to.y) / 2f,
                    x2 = to.x,
                    y2 = to.y
                )
            }
        }
    }

    drawPath(
        path = smoothedPath,
        color = if (mode == ToolMode.DRAW) Color.Black else Color.Transparent,
        style = Stroke(width = if (mode == ToolMode.DRAW) 8f else 36f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        ,
        blendMode = if (mode == ToolMode.ERASE) BlendMode.Clear else BlendMode.SrcOver
    )
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PlainPageView(){
//    PlainPage(journalViewModel = viewModel(factory = JournalViewModel.Factory),page = JournalPage.PlainPage(id = 0, paths = mutableListOf(), undoStack = mutableListOf(), redoStack = mutableListOf(), name = "hi"), color = Color.Cyan)
//}
