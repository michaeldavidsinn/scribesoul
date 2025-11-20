package com.example.scribesoul.ui.components.journalPages

import JournalPage
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scribesoul.R
import com.example.scribesoul.utils.DrawCanvas
import com.example.scribesoul.utils.NameInputDialog
import com.example.scribesoul.viewModels.DrawingViewModel
import com.example.scribesoul.viewModels.JournalViewModel

@Composable
fun NarrowLinedSmallMarginPage(
    page: JournalPage.NarrowLinedSmallMarginPage,
    color: Color,
    drawingViewModel: DrawingViewModel,
    journalViewModel: JournalViewModel
){
    val density = LocalDensity.current
    var showTextInput by remember { mutableStateOf(false) }
    val lineColor = Color.Gray.copy(alpha = 0.3f)
    val lineSpacing = 20.dp
    val topMargin = 80.dp

    if (showTextInput) {
        NameInputDialog(
            onNameCreate = { name ->
                showTextInput = false
                journalViewModel.addPageToSection(journalViewModel.selectedSectionIndex)
                val newPage = journalViewModel.sections[journalViewModel.selectedSectionIndex].pages.last() as JournalPage.PlainPage
                newPage.name = name
                journalViewModel.changeSelectedPageIndex(
                    journalViewModel.sections[journalViewModel.selectedSectionIndex].pages.lastIndex
                )
            },
            onDismissRequest = { showTextInput = false }
        )
    }


    key(page.id) {
        Box(
            modifier = Modifier
                .background(color, RoundedCornerShape(23.dp))
                .height(640.dp)
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(23.dp))
                .clipToBounds()
                .drawBehind{
                    val spacingPx = lineSpacing.toPx()
                    val topMarginPx = topMargin.toPx()
                    val strokeWidth = 1.dp.toPx()

                    // 2. Loop from top margin down to the bottom of the page
                    var currentY = topMarginPx
                    while (currentY < size.height) {
                        drawLine(
                            color = lineColor,
                            start = Offset(x = 0f, y = currentY),
                            end = Offset(x = size.width, y = currentY),
                            strokeWidth = strokeWidth
                        )
                        currentY += spacingPx
                    }
                }
                .clipToBounds()
        ) {

            DrawCanvas(drawingViewModel = drawingViewModel, page = page)

            // Page navigation and add new page
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (page.name.isEmpty()) {
                    Text(
                        "${journalViewModel.selectedPageIndex + 1} Page",
                        fontSize = 30.sp,
                        fontFamily = FontFamily(Font(R.font.verdana_bold)),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B395B),
                        modifier = Modifier.clickable { showTextInput = true }
                    )
                } else {
                    Text(
                        page.name,
                        fontSize = 30.sp,
                        fontFamily = FontFamily(Font(R.font.verdana_bold)),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B395B)
                    )
                }

                Text(
                    "+",
                    fontSize = 50.sp,
                    fontFamily = FontFamily(Font(R.font.verdana_bold)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B395B),
                    modifier = Modifier.clickable {
                        showTextInput = true
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "<",
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.verdana_bold)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B395B),
                    modifier = Modifier.clickable {
                        journalViewModel.changeSelectedPageIndex(maxOf(0, journalViewModel.selectedPageIndex - 1))
                    }
                )
                Text(
                    ">",
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.verdana_bold)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B395B),
                    modifier = Modifier.clickable {
                        val lastIndex = journalViewModel.sections[journalViewModel.selectedSectionIndex].pages.lastIndex
                        journalViewModel.changeSelectedPageIndex(minOf(lastIndex, journalViewModel.selectedPageIndex + 1))
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NarrowLinedSmallMarginPageView(){
    NarrowLinedSmallMarginPage(journalViewModel = viewModel(factory = JournalViewModel.Factory),page = JournalPage.NarrowLinedSmallMarginPage(id = 0,  name = "hi"), color = Color.Cyan, drawingViewModel = viewModel(factory = DrawingViewModel.Factory))
}