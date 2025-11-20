package com.example.scribesoul.ui.components.journalPages

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scribesoul.R
import com.example.scribesoul.models.ToolMode
import com.example.scribesoul.ui.components.CalendarGrid
import com.example.scribesoul.utils.DrawCanvas
import com.example.scribesoul.viewModels.DrawingViewModel
import com.example.scribesoul.viewModels.JournalViewModel
import java.time.LocalDate
import java.time.YearMonth

//(0XFFFFFEE4)

@Composable
fun CalendarPage(
    page: JournalPage.CalendarPage,
    color: Color
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var textInput by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .background(color, RoundedCornerShape(23.dp))
            .height(640.dp)
            .fillMaxWidth(0.8f)
            .clip(RoundedCornerShape(23.dp))
            .clipToBounds()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp)
        ) {

            // --- MONTH NAVIGATION ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "◀",
                    fontSize = 32.sp,
                    color = Color(0xFF2B395B),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            page.currentMonth = page.currentMonth.minusMonths(1)
                        }
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        page.currentMonth.month.toString(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B395B)
                    )
                    Text(
                        page.currentMonth.year.toString(),
                        fontSize = 22.sp,
                        color = Color.DarkGray
                    )
                }

                Text(
                    "▶",
                    fontSize = 32.sp,
                    color = Color(0xFF2B395B),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            page.currentMonth = page.currentMonth.plusMonths(1)
                        }
                )
            }

            Spacer(Modifier.height(8.dp))

            // --- IMPROVED GRID WITH SPACING ---
            CalendarGrid(
                month = page.currentMonth,
                reminders = page.reminders,
//                onDateClick = { date ->
//                    selectedDate = date
//                    textInput = ""
//                    showDialog = true
//                }
            )
        }
    }

    // --- ADD EVENT POP-UP ---
    if (showDialog && selectedDate != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add Event for $selectedDate") },
            text = {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Type reminder here") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (textInput.isNotBlank()) {
                            val list = page.reminders.getOrPut(selectedDate!!) { SnapshotStateList() }
                            list.add(textInput)


                        }
                        showDialog = false
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}


// Fungsi menggambar path dari list Offset, tergantung mode aktif
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalendarPageView(){
    CalendarPage(page = JournalPage.CalendarPage(id = 1), color = Color(0XFFFFCCE3))
}
