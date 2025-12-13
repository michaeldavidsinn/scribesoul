package com.example.scribesoul.ui.components.journalPages

import JournalPage
import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scribesoul.R
import com.example.scribesoul.utils.AddMoodDialog
import com.example.scribesoul.utils.NameInputDialog
import java.nio.file.WatchEvent
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodPage(page: JournalPage.MoodsPage, color: Color) {

    var currentMonth by remember { mutableStateOf(page.currentMonth) }
    var showAddMood by remember { mutableStateOf(false) }

    val today = LocalDate.now()
    val isCurrentMonth = today.year == currentMonth.year && today.month == currentMonth.month
    val daysInMonth = currentMonth.lengthOfMonth()

    val monthlyMoods =
        page.moods.getOrPut(currentMonth) { SnapshotStateMap<Int, Float>() }

    val moodList = (1..daysInMonth).map { day ->
        monthlyMoods[day]  // null means missing → draw gap
    }

    if (showAddMood) {
        AddMoodDialog(
            onDismiss = { showAddMood = false },
            onSave = { date, moodValue ->

                val ym = YearMonth.from(date)
                val monthMap = page.moods.getOrPut(ym) { SnapshotStateMap() }

                monthMap[date.dayOfMonth] = moodValue

                showAddMood = false
            }
        )
    }


    Column(
        modifier = Modifier
            .background(color, RoundedCornerShape(23.dp))
            .height(640.dp)
            .fillMaxWidth(0.8f)
            .clip(RoundedCornerShape(23.dp))
            .clipToBounds()
            .padding(top = 10.dp)
    ) {

        // --- Title ---
        Column(verticalArrangement = Arrangement.spacedBy(-60.dp), modifier = Modifier.height(75.dp)) {
            Text(
                "MOOD",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 50.sp,
                    fontFamily = FontFamily(Font(R.font.verdana_bold)),
                    fontWeight = FontWeight(600),
                    color = Color(0XFF2B395B),
                    letterSpacing = 1.sp,
                )
            )
            Text(
                "Tracker",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 96.sp,
                    fontFamily = FontFamily(Font(R.font.palace_script)),
                    fontWeight = FontWeight(500),
                    color = Color.Black,
                    letterSpacing = 1.sp,
                )
            )
        }

        // --- Month Navigation ---
        CalendarHeader(
            yearMonth = currentMonth,
            onPreviousMonth = {
                currentMonth = currentMonth.minusMonths(1)
                page.currentMonth = currentMonth
            },
            onNextMonth = {
                currentMonth = currentMonth.plusMonths(1)
                page.currentMonth = currentMonth
            },

        )

        // --- Mood Icons (legend) ---
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 32.dp)
                .offset(x = 24.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Image(painterResource(R.drawable.sad), null, Modifier.size(15.dp).weight(0.25f))
            Image(painterResource(R.drawable.mid), null, Modifier.size(15.dp).weight(0.25f))
            Image(painterResource(R.drawable.good), null, Modifier.size(15.dp).weight(0.25f))
            Image(painterResource(R.drawable.great), null, Modifier.size(15.dp).weight(0.25f))
        }

        Row(modifier = Modifier.fillMaxWidth().clickable{
            showAddMood = true
        }.height(460.dp).padding(top=10.dp),verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Center) {
            val dayHeight = 460.dp / (daysInMonth + 1)
            // --- Day Numbers ---

            // --- Graph Area ---
            Canvas(
                modifier = Modifier
                    .padding(start = 64.dp, end = 36.dp)
                    .fillMaxWidth()

                    .height(460.dp)

            ) {
                val daySpacing = size.height / daysInMonth
                val graphWidth = size.width



                val points = (1..daysInMonth).map { day ->
                    val value = monthlyMoods[day]  // keep nulls
                    value?.let {
                        val percent = (it - 1f) / 3f        // (4f - 1f)
                        val x = graphWidth * percent
                        val y = daySpacing * (day - 0.5f)
                        Offset(x, y)
                    }
                }

                // Draw horizontal grid lines
                for (i in 1 .. daysInMonth) {
                    val y = daySpacing * (i + 0.5f)
                    drawLine(
                        color = Color.Black,
                        start = Offset(0f, y),
                        end = Offset(graphWidth, y),
                        strokeWidth = 1f
                    )

                    drawContext.canvas.nativeCanvas.drawText(
                        i.toString(),
                        -40f,           // X offset (to the left of the Canvas start)
                        y + 5f,         // Y offset
                        android.graphics.Paint().apply {
                            this.color = android.graphics.Color.BLACK
                            this.textSize = 28f
                            this.isAntiAlias = true
                        }
                    )
                }

                // Draw lines between mood points (skip gaps)
                var lastPoint: Offset? = null
                for (p in points) {
                    if(p != null){
                        drawCircle(
                            color = Color.Black,
                            radius = 6f,
                            center = Offset(x = p.x, y = p.y)
                        )
                    }

                    if (p != null && lastPoint != null) {

                        drawLine(
                            color = Color.Black,
                            start = lastPoint!!,
                            end = p,
                            strokeWidth = 4f
                        )
                    }
                    lastPoint = p
                }

            }
        }


    }

}




private fun getValuePercentageForRange(value: Float, max: Float, min: Float) =
    (value - min) / (max - min)

@RequiresApi(Build.VERSION_CODES.O)
@Preview
    (showBackground = true, showSystemUi = true)
@Composable
fun MoodPagePreview(){
    MoodPage(page = JournalPage.MoodsPage(id = 1), color = Color(0XFFFFCCE3))
}