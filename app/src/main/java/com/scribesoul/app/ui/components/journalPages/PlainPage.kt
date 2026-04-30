package com.scribesoul.app.ui.components.journalPages

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scribesoul.app.R
import com.scribesoul.app.models.ToolMode
import kotlin.math.abs
import com.scribesoul.app.models.*
import com.scribesoul.app.utils.ChangeInputDialog
import com.scribesoul.app.utils.DrawCanvas
import com.scribesoul.app.utils.NameInputDialog
import com.scribesoul.app.viewModels.DrawingViewModel
import com.scribesoul.app.viewModels.JournalViewModel

//(0XFFFFFEE4)

@Composable
fun PlainPage(
    journalViewModel: JournalViewModel,
    drawingViewModel: DrawingViewModel,
    page: JournalPage.PlainPage,
    color: Color
) {
    val density = LocalDensity.current
    var showTextInput by remember { mutableStateOf(false) }
    var showChangeInput by remember { mutableStateOf(false) }

    if(showChangeInput){
        ChangeInputDialog(onNameChange = { name ->
            showChangeInput = false
            page.name = name

        }, onDismissRequest = { showChangeInput = false },
            initial = page.name
        )
    }

    if (showTextInput) {
        NameInputDialog(
            onNameCreate = { name ->
                showTextInput = false
                journalViewModel.addPageToSection(journalViewModel.selectedSectionIndex)
                val newPage =
                    journalViewModel.sections[journalViewModel.selectedSectionIndex].pages.last() as JournalPage.PlainPage
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
        ) {
            DrawCanvas(drawingViewModel = drawingViewModel, page = page)

            // Page navigation and add new page
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (page.name.isEmpty()) {
                    Text(
                        "${journalViewModel.selectedPageIndex + 1} Page",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.verdana_bold)),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B395B),
                        modifier = Modifier.clickable { showChangeInput = true }
                    )
                } else {
                    Text(
                        page.name,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.verdana_bold)),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B395B),
                        modifier = Modifier.clickable { showChangeInput = true }
                    )
                }

                Text(
                    "+",
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.verdana_bold)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B395B),
                    modifier = Modifier.clickable {
                        showTextInput = true
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
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
                        journalViewModel.changeSelectedPageIndex(
                            maxOf(
                                0,
                                journalViewModel.selectedPageIndex - 1
                            )
                        )
                    }
                )
                Text(
                    ">",
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.verdana_bold)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B395B),
                    modifier = Modifier.clickable {
                        val lastIndex =
                            journalViewModel.sections[journalViewModel.selectedSectionIndex].pages.lastIndex
                        journalViewModel.changeSelectedPageIndex(
                            minOf(
                                lastIndex,
                                journalViewModel.selectedPageIndex + 1
                            )
                        )
                    }
                )
            }
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
            val from = offsets[i - 1]
            val to = offsets[i]
            val dx = abs(from.x - to.x)
            val dy = abs(from.y - to.y)
            if (dx >= smoothness || dy >= smoothness) {
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
        style = Stroke(
            width = if (mode == ToolMode.DRAW) 8f else 36f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        ),
        blendMode = if (mode == ToolMode.ERASE) BlendMode.Clear else BlendMode.SrcOver
    )
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PlainPageView(){
//    PlainPage(journalViewModel = viewModel(factory = JournalViewModel.Factory),page = JournalPage.PlainPage(id = 0, paths = mutableListOf(), undoStack = mutableListOf(), redoStack = mutableListOf(), name = "hi"), color = Color.Cyan)
//}
