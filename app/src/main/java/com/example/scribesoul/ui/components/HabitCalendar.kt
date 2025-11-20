import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.scribesoul.utils.DrawCanvas
import com.example.scribesoul.viewModels.DrawingViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HabitCalendar(
    currentMonth: YearMonth,
    habits: List<String> = emptyList(),
    onAddHabit: () -> Unit = {},
    drawingViewModel: DrawingViewModel,
    page: JournalPage
) {


    val days = remember(currentMonth) {
        generateCalendarDays(currentMonth)
    }

    Box {

            LazyColumn(
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                habits.forEach { habit->
                    item{
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = MaterialTheme.shapes.medium,
                            shadowElevation = 4.dp,
                            color = Color.White
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(habit,
                                    style = androidx.compose.ui.text.TextStyle(

                                        fontSize = 19.sp,
                                        fontFamily = FontFamily(Font(R.font.verdana_bold)),
                                        fontWeight = FontWeight(800),
                                        color = Color(0xFFFFF47A),

                                        textAlign = TextAlign.Center,
                                        letterSpacing = 13.3.sp,

                                        ),
                                    modifier = Modifier
                                        .graphicsLayer(alpha = 0.99f)
                                        .drawWithCache {
                                            val brush = Brush.horizontalGradient(
                                                listOf(
                                                    Color(0XFFFFF47A),
                                                    Color(0XFFFFA8CF),
                                                    Color(0XFFA774FF)
                                                )
                                            )
                                            onDrawWithContent {
                                                drawContent()
                                                drawRect(brush, blendMode = BlendMode.SrcAtop)
                                            }
                                        }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                DayOfWeekHeader()
                                Spacer(modifier = Modifier.height(8.dp))
                                CalendarGrid(
                                    days = days,
                                    currentMonth = currentMonth,

                                    )
                            }
                        }
                    }
                }

                item{
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Column(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White)
                                .size(30.dp)
                                .clickable {
                                    onAddHabit()
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center


                        ) {
                            Text("+",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.verdana_bold)),
                                    fontWeight = FontWeight(600),
                                    color = Color.Black,
                                    letterSpacing = 1.sp,
                                )
                            )
                        }

                    }
                }



            }
//            DrawCanvas(drawingViewModel =  drawingViewModel, page)

    }


}




@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DayOfWeekHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val daysOfWeek = DayOfWeek.entries.toTypedArray().let {
            val sunday = it.first { day -> day == DayOfWeek.SUNDAY }
            it.dropWhile { day -> day != sunday } + it.takeWhile { day -> day != sunday }
        }
        daysOfWeek.forEach { day ->
            Text(
                text = day.getDisplayName(java.time.format.TextStyle.NARROW, Locale.getDefault()),
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = FontFamily(Font(R.font.verdana_bold)),
                    fontWeight = FontWeight(700),
                    color = Color(0XFF2B395B),
                    letterSpacing = 1.sp,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Composable that displays the grid of calendar days.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CalendarGrid(
    days: List<LocalDate>,
    currentMonth: YearMonth,
) {
    Column {
        val weeks = days.chunked(7)
        weeks.forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                week.forEach { date ->
                    // Use a Box with weight to ensure each day cell takes up equal space
                    Box(modifier = Modifier.weight(1f)) {
                        CalendarDay(
                            date = date,
                            isCurrentMonth = YearMonth.from(date) == currentMonth,
                        )
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CalendarDay(
    date: LocalDate,
    isCurrentMonth: Boolean,
) {
    val textColor = when {
        !isCurrentMonth -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0f)
        else -> Color(0XFF2B395B)
    }


    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            ,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = androidx.compose.ui.text.TextStyle(
                fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                fontWeight = FontWeight(400),
                letterSpacing = 1.sp,
            ),
            color = textColor,
            fontSize = 14.sp,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun generateCalendarDays(yearMonth: YearMonth): List<LocalDate> {
    val firstDayOfMonth = yearMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek
    val startOffset = (firstDayOfWeek.value % 7)

    val startDate = firstDayOfMonth.minusDays(startOffset.toLong())
    
    return (0 until 42).map { dayIndex ->
        startDate.plusDays(dayIndex.toLong())
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HabitCalendarPreview() {
    HabitCalendar(currentMonth = YearMonth.now(), drawingViewModel = viewModel(factory = DrawingViewModel.Factory), page = JournalPage.HabitsPage(id=0))
}
