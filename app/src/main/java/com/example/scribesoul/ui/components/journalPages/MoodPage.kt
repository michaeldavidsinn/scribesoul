package com.example.scribesoul.ui.components.journalPages

import JournalPage
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
import java.nio.file.WatchEvent
import java.time.LocalDate
import java.time.YearMonth


@Composable
fun MoodPage(page: JournalPage.MoodsPage, color: Color) {

    var currentMonth by remember { mutableStateOf(page.currentMonth) }

    val today = LocalDate.now()
    val isCurrentMonth = today.year == currentMonth.year && today.month == currentMonth.month
    val daysInMonth = currentMonth.lengthOfMonth()

    val monthlyMoods =
        page.moods.getOrPut(currentMonth) { SnapshotStateMap<Int, Float>() }

    val moodList = (1..daysInMonth).map { day ->
        monthlyMoods[day]  // null means missing → draw gap
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
        Column(verticalArrangement = Arrangement.spacedBy(-60.dp)) {
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
            }
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

        Row(verticalAlignment = Alignment.Top) {

            // --- Day Numbers ---
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (day in 1..daysInMonth) {
                    Text(text = day.toString(), fontSize = 12.sp)
                }
            }

            // --- Graph Area ---
            Canvas(
                modifier = Modifier
                    .padding(start = 14.dp, end = 36.dp)
                    .padding(vertical = 18.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                val spacingY = size.height / (daysInMonth - 1)
                val graphWidth = size.width

                val points = moodList.mapIndexedNotNull { index, value ->
                    value?.let {
                        val percent = (it - 1f) / (4f - 1f)
                        val x = graphWidth * percent
                        val y = spacingY * index
                        Offset(x, y)
                    }
                }

                // Draw horizontal grid lines
                for (i in 0 until daysInMonth) {
                    val y = spacingY * i
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f, y),
                        end = Offset(graphWidth, y),
                        strokeWidth = 1f
                    )
                }

                // Draw lines between mood points (skip gaps)
                for (i in 0 until points.size - 1) {
                    drawLine(
                        color = Color.Black,
                        start = points[i],
                        end = points[i + 1],
                        strokeWidth = 4f
                    )
                }
            }
        }

        // --- Mood Slider (only for today) ---
        if (isCurrentMonth) {
            val todayMood = monthlyMoods[today.dayOfMonth]
            if (todayMood == null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("How do you feel today?", fontSize = 18.sp)

                    var sliderValue by remember { mutableStateOf(2f) }

                    Slider(
                        value = sliderValue,
                        onValueChange = { sliderValue = it },
                        valueRange = 1f..4f,
                        steps = 2
                    )

                    Button(
                        onClick = {
                            monthlyMoods[today.dayOfMonth] = sliderValue
                        }
                    ) {
                        Text("Save Mood")
                    }
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
    MoodPage(JournalPage.MoodsPage(5, currentMonth =  YearMonth.of(2025,8)), color = Color(0xFFFFFDB4))
}