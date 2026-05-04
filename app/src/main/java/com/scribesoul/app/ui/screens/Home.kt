package com.scribesoul.app.ui.screens

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.scribesoul.R
import com.scribesoul.app.models.Habit
import com.scribesoul.app.ui.navigation.BottomNavItem
import com.scribesoul.app.utils.softShadow
import com.scribesoul.app.viewModels.HomeViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val bgGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF82d9d2),
            Color(0xFFF74a8ff),
        )
    )
    StepCounterEffect(viewModel = viewModel)


    var showTextInput by remember { mutableStateOf(false) }
    var showAddHabit by remember { mutableStateOf(false) }
    var selectedHabit by remember { mutableStateOf<Habit?>(null) }
    var showUnavailable by remember { mutableStateOf(false) }
    var showTimerDialog by remember { mutableStateOf(false) }


    var name by remember {mutableStateOf("")}

    if (showAddHabit) {
        AddHabitDialog(
            onDismiss = { showAddHabit = false },
            onSubmit = { name, metric, icon, goal ->
                viewModel.addHabit(name, metric, icon, goal)
                showAddHabit = false
            }
        )
    }
    if (showUnavailable) {
        Dialog(onDismissRequest = { showUnavailable = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.wrapContentSize()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tip is Unavailable",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                }
            }
        }
    }

    if (showTextInput && selectedHabit != null) {
        InputDialog(
            name = selectedHabit!!.habitName,
            onInput = { newValue, metric ->
                viewModel.updateHabitForDay(
                    selectedHabit!!,
                    viewModel.currentDay,
                    newValue
                )
                showTextInput = false
                selectedHabit = null
            },
            onDismissRequest = {
                showTextInput = false
                selectedHabit = null
            }
        )
    }

    if (showTimerDialog && selectedHabit != null) {
        val currentValue = viewModel.getValueForDay(selectedHabit!!, viewModel.currentDay)

        TimerDialog(
            habitName = selectedHabit!!.habitName,
            metric = selectedHabit!!.metric,
            initialValue = currentValue,
            onDismissRequest = {
                showTimerDialog = false
                selectedHabit = null
            },
            onSave = { newValue ->
                // This utilizes your existing pairing logic for today's date!
                viewModel.updateHabitForDay(
                    selectedHabit!!,
                    viewModel.currentDay,
                    newValue
                )
                showTimerDialog = false
                selectedHabit = null
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
    ) {
        Column(

        ) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 56.dp, bottom = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Hi, ${viewModel.user.name}",
                        style =
                            TextStyle(
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.verdana)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFF2B395B)
                            ))
                    Text(viewModel.getGreetinng(),
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.verdana_bold)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFF2B395B),
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2B395B))
                            .clickable { navController.navigate("sos_screen") },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SOS",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.verdana_bold))
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(brush = bgGradient, shape = CircleShape)
                            .padding(3.dp)
                            .clickable { navController.navigate("profile") }
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(10.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.cat2),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(30.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .padding(top = 15.dp)
                    .align(Alignment.CenterHorizontally),

            ) {
                items(viewModel.dates) { date ->
                    Column(
                        modifier = Modifier
                            .width(48.dp)
                            .clickable { viewModel.switchDay(date) }
                            .align(Alignment.CenterHorizontally),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = date.dayOfWeek.toString().substring(0, 3),
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.verdana)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFF5F5F5F)
                            )
                        )
                        if (date == viewModel.currentDay) {
                            Box(
                                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(top = 5.dp)
                                        .softShadow(
                                            radius = 20f,
                                            offsetY = 12f,
                                            alpha = 0.18f
                                        )
                                        .align(Alignment.Center)
                                        .width(30.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xff74A8FF))
                                        .padding(5.dp)

                                ) {
                                    Text(
                                        text = date.dayOfMonth.toString(),
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily(Font(R.font.verdana)),
                                            fontWeight = FontWeight(600),
                                            color = Color.White,
                                            textAlign = TextAlign.Center
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .offset(y = 4.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xff74A8FF))
                                        .border(2.dp, Color.White, CircleShape)
                                        .padding(5.dp)
                                        .align(Alignment.BottomCenter)
                                ) {}
                            }
                        } else {
                            Text(
                                text = date.dayOfMonth.toString(),
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .padding(5.dp),
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.verdana)),
                                    fontWeight = FontWeight(600),
                                    color = Color(0XFF121212)
                                )
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Your Goals",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.verdana)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF313632),
                        letterSpacing = 1.sp,
                    )
                )

                Text(
                    "+ Add",
                    modifier = Modifier
                        .softShadow(
                            radius = 20f,
                            offsetY = 12f,
                            alpha = 0.18f
                        )
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF74A8FF))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clickable { showAddHabit = true },
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.verdana_bold))
                )
            }

            LazyVerticalGrid (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
//                columns = GridCells.Adaptive(minSize = 175.dp),
                columns = GridCells.Fixed(2)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .width(120.dp)
                            .height(160.dp)
                            .softShadow(
                                radius = 20f,
                                offsetY = 12f,
                                alpha = 0.18f
                            )
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                color = Color(0XFFE1F9DF)
                            )
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column{
                            Box(
                                modifier = Modifier
                                    .softShadow(
                                        radius = 20f,
                                        offsetY = 12f,
                                        alpha = 0.18f
                                    )
                                    .clip(RoundedCornerShape(16.dp))
                                    .size(56.dp)
                                    .background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
                            ){
                                Image(
                                    painter = painterResource(R.drawable.habits_icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(37.dp)
                                        .height(37.dp),
                                )
                            }
                            Text(
                                text = "All Goals",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.verdana)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF313632),
                                    letterSpacing = 1.sp,
                                ),
                                modifier = Modifier.padding(top = 5.dp)
                            )
                            Text(
                                text = "${viewModel.habits.size} Habits",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF313632),

                                    letterSpacing = 1.sp,
                                ),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            LinearProgressIndicator(
                                progress = { viewModel.overallProgress(viewModel.currentDay) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .padding(top = 2.dp),
                                color = Color(0XFF40B490),
                                trackColor = Color(0XFFF9F9F9)
                            )
                        }
                    }


                }
                items(viewModel.habits) {habit->
                    if(habit.iconChoice == 1){
                        Column(
                            modifier = Modifier
                                .width(120.dp)
                                .height(160.dp)
                                .softShadow(
                                    radius = 20f,
                                    offsetY = 12f,
                                    alpha = 0.18f
                                )
                                .clickable {
                                    selectedHabit = habit
                                    // Check if the metric is time-based
                                    if (habit.metric.equals("Minutes", ignoreCase = true) || habit.metric.equals("Hours", ignoreCase = true)) {
                                        showTimerDialog = true
                                    } else {
                                        showTextInput = true
                                    }
                                }

                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    color = Color(0XFFE1F9DF)
                                )
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Column{
                                Box(
                                    modifier = Modifier
                                        .softShadow(
                                            radius = 20f,
                                            offsetY = 12f,
                                            alpha = 0.18f
                                        )
                                        .clip(RoundedCornerShape(16.dp))
                                        .size(56.dp)
                                        .background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
                                ){
                                    Image(
                                        painter = painterResource(R.drawable.habits_icon),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(37.dp)
                                            .height(37.dp),
                                    )
                                }
                                Text(
                                    text = "${habit.habitName}",
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily(Font(R.font.verdana)),
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFF313632),
                                        letterSpacing = 1.sp,
                                    ),
                                    modifier = Modifier.padding(top = 5.dp)
                                )
                                Text(
                                    text = "${viewModel.getValueForDay(habit, viewModel.currentDay)} ${habit.metric}",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFF313632),

                                        letterSpacing = 1.sp,
                                    ),
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                LinearProgressIndicator(
                                    progress = { viewModel.habitProgress(habit, viewModel.currentDay) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .padding(top = 2.dp),
                                    color = Color(0XFF40B490),
                                    trackColor = Color(0XFFF9F9F9),
                                    strokeCap = StrokeCap.Round,
                                )
                            }
                        }
                    }else if(habit.iconChoice == 2){
                        Column(
                            modifier = Modifier
                                .width(120.dp)
                                .height(160.dp)
                                .softShadow(
                                    radius = 20f,
                                    offsetY = 12f,
                                    alpha = 0.18f
                                )
                                .clickable {
                                    selectedHabit = habit
                                    // Check if the metric is time-based
                                    if (habit.metric.equals("Minutes", ignoreCase = true) || habit.metric.equals("Hours", ignoreCase = true)) {
                                        showTimerDialog = true
                                    } else {
                                        showTextInput = true
                                    }
                                }

                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    color = Color(0XFFFFE6F7)
                                )
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Column{
                                Box(
                                    modifier = Modifier
                                        .softShadow(
                                            radius = 20f,
                                            offsetY = 12f,
                                            alpha = 0.18f
                                        )
                                        .clip(RoundedCornerShape(16.dp))
                                        .size(56.dp)
                                        .background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
                                ){
                                    Image(
                                        painter = painterResource(R.drawable.meditation_icon),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(37.dp)
                                            .height(37.dp),
                                    )
                                }
                                Text(
                                    text = "${habit.habitName}",
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily(Font(R.font.verdana)),
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFF313632),
                                        letterSpacing = 1.sp,
                                    ),
                                    modifier = Modifier.padding(top = 5.dp)
                                )
                                Text(
                                    text = "${viewModel.getValueForDay(habit, viewModel.currentDay)} ${habit.metric}",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFF313632),

                                        letterSpacing = 1.sp,
                                    ),
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                LinearProgressIndicator(
                                    progress = { viewModel.habitProgress(habit, viewModel.currentDay) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .padding(top = 2.dp),
                                    color = Color(0XFFDC30AD),
                                    trackColor = Color(0XFFF9F9F9),
                                    strokeCap = StrokeCap.Round,
                                )
                            }
                        }
                    }else if(habit.iconChoice == 3){
                        Column(
                            modifier = Modifier
                                .width(120.dp)
                                .height(160.dp)
                                .softShadow(
                                    radius = 20f,
                                    offsetY = 12f,
                                    alpha = 0.18f
                                )
                                .clickable {
                                    selectedHabit = habit
                                    // Check if the metric is time-based
                                    if (habit.metric.equals("Minutes", ignoreCase = true) || habit.metric.equals("Hours", ignoreCase = true)) {
                                        showTimerDialog = true
                                    } else {
                                        showTextInput = true
                                    }
                                }

                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    color = Color(0XFFE0ECFF)
                                )
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Column{
                                Box(
                                    modifier = Modifier
                                        .softShadow(
                                            radius = 20f,
                                            offsetY = 12f,
                                            alpha = 0.18f
                                        )
                                        .clip(RoundedCornerShape(16.dp))
                                        .size(56.dp)
                                        .background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
                                ){
                                    Image(
                                        painter = painterResource(R.drawable.exercise_icon),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(37.dp)
                                            .height(37.dp),
                                    )
                                }
                                Text(
                                    text = "${habit.habitName}",
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily(Font(R.font.verdana)),
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFF313632),
                                        letterSpacing = 1.sp,
                                    ),
                                    modifier = Modifier.padding(top = 5.dp)
                                )
                                Text(
                                    text = "${viewModel.getValueForDay(habit, viewModel.currentDay)} ${habit.metric}",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFF313632),

                                        letterSpacing = 1.sp,
                                    ),
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                LinearProgressIndicator(
                                    progress = { viewModel.habitProgress(habit, viewModel.currentDay) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .padding(top = 2.dp),
                                    color = Color(0XFF5373FF),
                                    trackColor = Color(0XFFF9F9F9),
                                    strokeCap = StrokeCap.Round,
                                )
                            }
                        }
                    }else if(habit.iconChoice == 4){
                        Column(
                            modifier = Modifier
                                .width(120.dp)
                                .height(160.dp)
                                .softShadow(
                                    radius = 20f,
                                    offsetY = 12f,
                                    alpha = 0.18f
                                )
                                .clickable {
                                    selectedHabit = habit
                                    // Check if the metric is time-based
                                    if (habit.metric.equals("Minutes", ignoreCase = true) || habit.metric.equals("Hours", ignoreCase = true)) {
                                        showTimerDialog = true
                                    } else {
                                        showTextInput = true
                                    }
                                }

                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    color = Color(0XFFFFB4B4)
                                )
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Column{
                                Box(
                                    modifier = Modifier
                                        .softShadow(
                                            radius = 20f,
                                            offsetY = 12f,
                                            alpha = 0.18f
                                        )
                                        .clip(RoundedCornerShape(16.dp))
                                        .size(56.dp)
                                        .background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
                                ){
                                    Image(
                                        painter = painterResource(R.drawable.running_icon),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(37.dp)
                                            .height(37.dp),
                                    )
                                }
                                Text(
                                    text = "${habit.habitName}",
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily(Font(R.font.verdana)),
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFF313632),
                                        letterSpacing = 1.sp,
                                    ),
                                    modifier = Modifier.padding(top = 5.dp)
                                )
                                Text(
                                    text = "${viewModel.getValueForDay(habit, viewModel.currentDay)} ${habit.metric}",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFF313632),

                                        letterSpacing = 1.sp,
                                    ),
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                LinearProgressIndicator(
                                    progress = { viewModel.habitProgress(habit, viewModel.currentDay) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .padding(top = 2.dp),
                                    color = Color(0XFFDC30AD),
                                    trackColor = Color(0XFFF9F9F9),
                                    strokeCap = StrokeCap.Round,
                                )
                            }
                        }
                    }else if(habit.iconChoice == 5){
                        Column(
                            modifier = Modifier
                                .width(120.dp)
                                .height(160.dp)
                                .softShadow(
                                    radius = 20f,
                                    offsetY = 12f,
                                    alpha = 0.18f
                                )
                                .clickable {
                                    selectedHabit = habit
                                    // Check if the metric is time-based
                                    if (habit.metric.equals("Minutes", ignoreCase = true) || habit.metric.equals("Hours", ignoreCase = true)) {
                                        showTimerDialog = true
                                    } else {
                                        showTextInput = true
                                    }
                                }

                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    color = Color(0XFFEEE3FF)
                                )
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Column{
                                Box(
                                    modifier = Modifier
                                        .softShadow(
                                            radius = 20f,
                                            offsetY = 12f,
                                            alpha = 0.18f
                                        )
                                        .clip(RoundedCornerShape(16.dp))
                                        .size(56.dp)
                                        .background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
                                ){
                                    Image(
                                        painter = painterResource(R.drawable.water_icon),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(37.dp)
                                            .height(37.dp),
                                    )
                                }
                                Text(
                                    text = "${habit.habitName}",
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily(Font(R.font.verdana)),
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFF313632),
                                        letterSpacing = 1.sp,
                                    ),
                                    modifier = Modifier.padding(top = 5.dp)
                                )
                                Text(
                                    text = "${viewModel.getValueForDay(habit, viewModel.currentDay)} ${habit.metric}",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFF313632),

                                        letterSpacing = 1.sp,
                                    ),
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                LinearProgressIndicator(
                                    progress = { viewModel.habitProgress(habit, viewModel.currentDay) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .padding(top = 2.dp),
                                    color = Color(0XFF9747FF),
                                    trackColor = Color(0XFFF9F9F9),
                                    strokeCap = StrokeCap.Round,
                                )
                            }
                        }
                    }else if(habit.iconChoice == 6){
                        Column(
                            modifier = Modifier
                                .width(120.dp)
                                .height(160.dp)
                                .softShadow(
                                    radius = 20f,
                                    offsetY = 12f,
                                    alpha = 0.18f
                                )
                                .clickable {
                                    selectedHabit = habit
                                    // Check if the metric is time-based
                                    if (habit.metric.equals("Minutes", ignoreCase = true) || habit.metric.equals("Hours", ignoreCase = true)) {
                                        showTimerDialog = true
                                    } else {
                                        showTextInput = true
                                    }
                                }

                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    color = Color(0XFFFFD6B4)
                                )
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Column{
                                Box(
                                    modifier = Modifier
                                        .softShadow(
                                            radius = 20f,
                                            offsetY = 12f,
                                            alpha = 0.18f
                                        )
                                        .clip(RoundedCornerShape(16.dp))
                                        .size(56.dp)
                                        .background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
                                ){
                                    Image(
                                        painter = painterResource(R.drawable.read_icon),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(37.dp)
                                            .height(37.dp),
                                    )
                                }
                                Text(
                                    text = "${habit.habitName}",
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily(Font(R.font.verdana)),
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFF313632),
                                        letterSpacing = 1.sp,
                                    ),
                                    modifier = Modifier.padding(top = 5.dp)
                                )
                                Text(
                                    text = "${viewModel.getValueForDay(habit, viewModel.currentDay)} ${habit.metric}",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFF313632),

                                        letterSpacing = 1.sp,
                                    ),
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                LinearProgressIndicator(
                                    progress = { viewModel.habitProgress(habit, viewModel.currentDay) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .padding(top = 2.dp),
                                    color = Color(0XFFFD7A16),
                                    trackColor = Color(0XFFF9F9F9),
                                    strokeCap = StrokeCap.Round,
                                )
                            }
                        }
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

            BottomBarHome(navController)
        }
    }
}

@Composable
fun InputDialog(
    name:String, onDismissRequest: () -> Unit, onInput: (Int, String) -> Unit
){
    var inputText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,

        title = { Text("Input $name") },
        text = {
            TextField(value = inputText,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                onValueChange = { newValue ->
                    // Only allow digits
                    if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                        inputText = newValue
                    }
                }
            )
        },
        confirmButton = { Button(onClick = {  val number = inputText.toIntOrNull() ?: 0
            onInput(number, name)
            onDismissRequest()
        }) { Text("OK") } },
        dismissButton = { TextButton(onClick = onDismissRequest) { Text("Cancel") } }
    )
}


@Composable
fun BottomBarHome(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(start = 24.dp, end = 24.dp, top = 6.dp, bottom = 50.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(30.dp),
                clip = false
            )
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
            .height(70.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                R.drawable.home_icon_clicked, "Home", iconSize = 46.dp, onClick = {
                navController.navigate("home") {
                    launchSingleTop = true
                }

            })
            BottomNavItem(R.drawable.therapist_icon, "Therapist", iconSize = 25.dp, onClick = {
                navController.navigate("therapist") {
                    launchSingleTop = true
                }
            })
            BottomNavItem(R.drawable.explore_icon, "Explore", iconSize = 25.dp, onClick = {
                navController.navigate("explore") {
                    launchSingleTop = true
                }
            })
            BottomNavItem(R.drawable.scribble_icon, "Scribble", iconSize = 25.dp, onClick = {
                navController.navigate("addScribble") {
                    launchSingleTop = true
                }
            })
            BottomNavItem(R.drawable.journal_icon, "Journal", iconSize = 25.dp, onClick = {
                navController.navigate("journalList") {
                    launchSingleTop = true
                }
            })
        }
    }
}

@Composable
fun AddHabitDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, Int, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }

    var goal by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf("1") }

    val iconOptions = listOf(
        R.drawable.habits_icon,
        R.drawable.meditation_icon,
        R.drawable.exercise_icon,
        R.drawable.running_icon,
        R.drawable.water_icon,
        R.drawable.read_icon
    )
    val metricList = arrayOf("Minutes", "Hours", "Times", "Litres", "Steps", "Kilometers", "Calories", "Percentage")

    var expandedMetric by remember { mutableStateOf(false) }
    var metric by remember { mutableStateOf(metricList.first()) }
    var expandedIcon by remember { mutableStateOf(false) }
    var selectedMetric by remember { mutableStateOf(metricList.first()) }
    var selectedIcon by remember { mutableStateOf(iconOptions.first()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Habit") },
        text = {
            Column {
                Text("Habit Name")
                TextField(value = name, onValueChange = { name = it })

                Spacer(Modifier.height(10.dp))

                Text("Metric")
                Box{
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { expandedMetric = true }
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(Modifier.width(12.dp))
                            Text("$selectedMetric")
                        }

                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }

                    DropdownMenu(
                        expanded = expandedMetric,
                        onDismissRequest = { expandedMetric = false }
                    ) {
                        metricList.forEachIndexed { index, metricsSelect ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(metricList.get(index))
                                    }
                                },
                                onClick = {
                                    selectedMetric = metricList.get(index)
                                    metric = metricList.get(index)
                                    expandedMetric = false
                                }
                            )
                        }
                    }
                }


                Spacer(Modifier.height(10.dp))

                Text("Goal Per Day")
                TextField(value = goal,keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number), onValueChange = { goal = it })

                // Icon Picker Label
                Text("Choose Icon")
                Spacer(Modifier.height(6.dp))

                // Icon Dropdown
                Box {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { expandedIcon = true }
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = selectedIcon),
                                contentDescription = null,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text("Icon #${iconOptions.indexOf(selectedIcon) + 1}")
                        }

                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }

                    DropdownMenu(
                        expanded = expandedIcon,
                        onDismissRequest = { expandedIcon = false }
                    ) {
                        iconOptions.forEachIndexed { index, iconRes ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = iconRes),
                                            contentDescription = null,
                                            modifier = Modifier.size(28.dp)
                                        )
                                        Spacer(Modifier.width(10.dp))
                                        Text("Icon ${index + 1}")
                                    }
                                },
                                onClick = {
                                    selectedIcon = iconRes
                                    icon = (index + 1).toString()
                                    expandedIcon = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && metric.isNotBlank() && goal.isNotBlank()) {
                        val number = goal.toIntOrNull() ?: 0
                        val numIcon = icon.toIntOrNull() ?: 0
                        onSubmit(name, metric, numIcon, number)
                    }
                    onDismiss()
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}


@Composable
fun StepCounterEffect(viewModel: HomeViewModel) {
    val context = LocalContext.current

    // Check if we already have permission
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Launcher to ask the user for permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasPermission = isGranted }
    )

    // Request permission automatically on launch for Android 10+
    LaunchedEffect(Unit) {
        if (!hasPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }
    }

    // If permission is granted, start the sensor
    if (hasPermission) {
        DisposableEffect(Unit) {
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

            val sharedPrefs = context.getSharedPreferences("ScribeSoulPrefs", Context.MODE_PRIVATE)
            val todayStr = LocalDate.now().toString()

            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let {
                        val totalStepsSinceReboot = it.values[0].toInt()

                        // Get today's baseline, or set it if it's the first step of the day
                        var baseline = sharedPrefs.getInt("baseline_$todayStr", -1)

                        if (baseline == -1 || totalStepsSinceReboot < baseline) {
                            baseline = totalStepsSinceReboot
                            sharedPrefs.edit().putInt("baseline_$todayStr", baseline).apply()
                        }

                        // Calculate steps for today and push to ViewModel
                        val stepsToday = totalStepsSinceReboot - baseline
                        viewModel.updateStepHabitIfExists(stepsToday)
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            // Start listening
            stepSensor?.let {
                sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI)
            }

            // Stop listening when the screen is closed
            onDispose {
                sensorManager.unregisterListener(listener)
            }
        }
    }
}

@Composable
fun TimerDialog(
    habitName: String,
    metric: String,
    initialValue: Int, // The amount already tracked today
    onDismissRequest: () -> Unit,
    onSave: (Int) -> Unit
) {
    // Determine the conversion factor.
    // If the metric is Hours, 1 unit = 3600 seconds. If Minutes, 1 unit = 60 seconds.
    val isHours = metric.equals("Hours", ignoreCase = true)
    val multiplier = if (isHours) 3600 else 60

    // Start the timer at the already accumulated time
    var elapsedSeconds by remember { mutableStateOf(initialValue * multiplier) }
    var isRunning by remember { mutableStateOf(false) }

    // Coroutine that runs every second when isRunning is true
    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000)
            elapsedSeconds++
        }
    }

    // Format the time for display (HH:MM:SS)
    val hours = elapsedSeconds / 3600
    val minutes = (elapsedSeconds % 3600) / 60
    val seconds = elapsedSeconds % 60
    val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Tracking: $habitName",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.verdana_bold))










                    
                )
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = timeString,
                    style = TextStyle(
                        fontSize = 48.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF74A8FF)
                    ),
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Button(
                    onClick = { isRunning = !isRunning },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRunning) Color(0xFFFD7A16) else Color(0xFF40B490)
                    ),
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text(if (isRunning) "Pause" else if (elapsedSeconds == 0) "Start" else "Resume")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Convert the total seconds back to the correct integer metric
                    val valueToSave = elapsedSeconds / multiplier
                    onSave(valueToSave)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    isRunning = false
                    onDismissRequest()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}



@Preview(showBackground = true)
@Composable
fun Homeview() {
    // Gunakan dummy NavController untuk preview
    HomeScreen(navController = NavController(LocalContext.current), viewModel = viewModel(factory = HomeViewModel.Factory))
}