package com.example.scribesoul.ui.components.journalPages

import HabitCalendar
import JournalPage
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
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
import com.example.scribesoul.models.DrawablePath
import com.example.scribesoul.utils.DrawCanvas
import com.example.scribesoul.utils.drawPathFromFill
import com.example.scribesoul.viewModels.DrawingViewModel
import com.example.scribesoul.viewModels.JournalViewModel
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import com.example.scribesoul.models.SolidColor as SolidColorFill
import java.util.Locale

@Composable
fun HabitsPage(color: Color, page: JournalPage.HabitsPage, journalViewModel: JournalViewModel, drawingViewModel: DrawingViewModel){
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var showAddHabitDialog by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val currentPath = remember { mutableStateListOf<Offset>() }

    Column(
        modifier = Modifier
            .background(color, shape = RoundedCornerShape(size = 23.dp))
            .height(640.dp)
            .fillMaxWidth(fraction = 0.8f)
            .clip(RoundedCornerShape(23.dp))
            .clipToBounds()
            .padding(top = 20.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(-50.dp)
        ) {
            Text("HABIT", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 50.sp,
                    fontFamily = FontFamily(Font(R.font.verdana_bold)),
                    fontWeight = FontWeight(600),
                    color = Color(0XFF2B395B),
                    letterSpacing = 1.sp,
                )
            )
            Text("Tracker", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 96.sp,
                    fontFamily = FontFamily(Font(R.font.palace_script)),
                    fontWeight = FontWeight(500),
                    color = Color.Black,
                    letterSpacing = 1.sp,
                )
            )

        }

        CalendarHeader(
            yearMonth = currentMonth,
            onPreviousMonth = { currentMonth = currentMonth.minusMonths(1) },
            onNextMonth = { currentMonth = currentMonth.plusMonths(1) }
        )

        HabitCalendar(currentMonth = currentMonth, habits = page.habits, onAddHabit = {
            showAddHabitDialog = true
        },
            drawingViewModel,
            page)

        if(showAddHabitDialog){
            NewHabitDialog(onDismissRequest = {showAddHabitDialog =false}, onHabitAdd = {
                page.habits.add(it)
                showAddHabitDialog = false
            })
        }




    }
}

@Composable
fun NewHabitDialog(onDismissRequest: () -> Unit, onHabitAdd: (String) -> Unit) {
    var habit by remember { mutableStateOf(String()) }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Input New Habit") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(onValueChange = { habit = it }, label = { Text("Habit Name") }, value = habit)
            }
        },
        confirmButton = { Button(onClick = { onHabitAdd(habit) }) { Text("OK") } },
        dismissButton = { TextButton(onClick = onDismissRequest) { Text("Cancel") } }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
public fun CalendarHeader(
    yearMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous Month")
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(-20.dp)
        ) {
            Text(
                text =yearMonth.month.toString(),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.verdana_bold)),
                    fontWeight = FontWeight(600),
                    color = Color(0XFF2B395B),
                    letterSpacing = 1.sp,
                ),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = yearMonth.year.toString(),
                style = TextStyle(
                    fontSize = 56.sp,
                    fontFamily = FontFamily(Font(R.font.palace_script)),
                    fontWeight = FontWeight(600),
                    color = Color(0XFF2B395B),
                    letterSpacing = 1.sp,
                ),
                fontWeight = FontWeight.Bold
            )
        }

        IconButton(onClick = onNextMonth) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Month")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HabitsPagePreview() {
    HabitsPage(Color.Red, JournalPage.HabitsPage(id=0), journalViewModel = viewModel(factory = JournalViewModel.Factory), drawingViewModel = viewModel(factory = DrawingViewModel.Factory))
}
