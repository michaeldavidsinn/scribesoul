package com.example.scribesoul.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scribesoul.R
import com.example.scribesoul.models.Habit
import com.example.scribesoul.ui.components.ProblemBubble
import com.example.scribesoul.ui.navigation.BottomNavItem
import com.example.scribesoul.utils.NameInputDialog
import com.example.scribesoul.utils.softShadow
import com.example.scribesoul.viewModels.HomeViewModel
import com.example.scribesoul.viewModels.JournalViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val bgGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF82d9d2),
            Color(0xFFF74a8ff),
        )
    )
    var showTextInput by remember { mutableStateOf(false) }
    var showAddHabit by remember { mutableStateOf(false) }
    var selectedHabit by remember { mutableStateOf<Habit?>(null) }


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
            Row (modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 56.dp, bottom = 10.dp).fillMaxWidth()){
                Column {
                    Text("Hi, Jake",
                        style =
                            TextStyle(
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.verdana)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFF2B395B)
                            ))
                    Text("Good Morning",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.verdana_bold)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFF2B395B),

                            ))
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background( brush = bgGradient, shape = CircleShape)
                        .padding(3.dp)
                ) {
                    Box(
                        modifier = Modifier.clip(CircleShape).background(Color.White).padding(10.dp).align(Alignment.Center)
                    ){
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
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .padding(top = 5.dp)
                    .align(Alignment.CenterHorizontally),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
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
                            Box {
                                Column(
                                    modifier = Modifier
                                        .softShadow(
                                            radius = 20f,
                                            offsetY = 12f,
                                            alpha = 0.18f
                                        )
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
                                            color = Color.White
                                        )
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
                                modifier = Modifier.padding(5.dp),
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

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding( top = 5.dp).align(alignment = Alignment.Start), contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
            ) {
                item{

                    ProblemBubble("Stress", onClick = {})
                }
                item{
                    ProblemBubble("Anxiety", onClick = {})
                }
                item{
                    ProblemBubble("Insomnia", onClick = {})
                }
                item{
                    ProblemBubble("Overthinking", onClick = {})
                }
                item{
                    ProblemBubble("Fatigue", onClick = {})
                }
                item{
                    ProblemBubble("Mood Swings", onClick = {})
                }

                item{
                    ProblemBubble("Panic Attack", onClick = {})
                }


            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Your Habits",
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
                                modifier = Modifier.softShadow(
                                    radius = 20f,
                                    offsetY = 12f,
                                    alpha = 0.18f
                                ).clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
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
                                text = "All Habits",
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
                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(3.dp)).padding(top = 2.dp),
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
                                    showTextInput = true
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
                                    modifier = Modifier.softShadow(
                                        radius = 20f,
                                        offsetY = 12f,
                                        alpha = 0.18f
                                    ).clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
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
                                    modifier = Modifier.fillMaxWidth().height(8.dp).padding(top = 2.dp),
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
                                    showTextInput = true
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
                                    modifier = Modifier.softShadow(
                                        radius = 20f,
                                        offsetY = 12f,
                                        alpha = 0.18f
                                    ).clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
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
                                    modifier = Modifier.fillMaxWidth().height(8.dp).padding(top = 2.dp),
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
                                    showTextInput = true
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
                                    modifier = Modifier.softShadow(
                                        radius = 20f,
                                        offsetY = 12f,
                                        alpha = 0.18f
                                    ).clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
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
                                    modifier = Modifier.fillMaxWidth().height(8.dp).padding(top = 2.dp),
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
                                    showTextInput = true
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
                                    modifier = Modifier.softShadow(
                                        radius = 20f,
                                        offsetY = 12f,
                                        alpha = 0.18f
                                    ).clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
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
                                    modifier = Modifier.fillMaxWidth().height(8.dp).padding(top = 2.dp),
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
                                    showTextInput = true
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
                                    modifier = Modifier.softShadow(
                                        radius = 20f,
                                        offsetY = 12f,
                                        alpha = 0.18f
                                    ).clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
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
                                    modifier = Modifier.fillMaxWidth().height(8.dp).padding(top = 2.dp),
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
                                    showTextInput = true
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
                                    modifier = Modifier.softShadow(
                                        radius = 20f,
                                        offsetY = 12f,
                                        alpha = 0.18f
                                    ).clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
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
                                    modifier = Modifier.fillMaxWidth().height(8.dp).padding(top = 2.dp),
                                    color = Color(0XFFFD7A16),
                                    trackColor = Color(0XFFF9F9F9),
                                    strokeCap = StrokeCap.Round,
                                )
                            }
                        }
                    }

                }

//                item {
//                    Column(
//                        modifier = Modifier
//                            .width(120.dp)
//                            .height(160.dp)
//                            .clickable{
//                                name = "Exercise Hours"
//                                showTextInput = true
//                            }
//
//                            .clip(RoundedCornerShape(16.dp))
//                            .background(
//                                color = Color(0XFFE0ECFF)
//                            )
//                            .padding(20.dp)
//                            ,
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Column{
//                            Box(
//                                modifier = Modifier.clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
//                            ){
//                                Image(
//                                    painter = painterResource(R.drawable.exercise_icon),
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .width(37.dp)
//                                        .height(37.dp),
//                                )
//                            }
//                            Text(
//                                text = "Exercise",
//                                style = TextStyle(
//                                    fontSize = 12.sp,
//                                    fontFamily = FontFamily(Font(R.font.verdana)),
//                                    fontWeight = FontWeight(400),
//                                    color = Color(0xFF313632),
//                                    letterSpacing = 1.sp,
//                                ),
//                                modifier = Modifier.padding(top = 5.dp)
//                            )
//                            Text(
//                                text = "${viewModel.exercise} hours",
//                                style = TextStyle(
//                                    fontSize = 16.sp,
//                                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
//                                    fontWeight = FontWeight(400),
//                                    color = Color(0xFF313632),
//
//                                    letterSpacing = 1.sp,
//                                ),
//                                modifier = Modifier.padding(top = 2.dp)
//                            )
//                            LinearProgressIndicator(
//                                progress = { 1F },
//                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(3.dp)).padding(top = 2.dp),
//                                color = Color(0XFF5373FF),
//                                trackColor = Color(0XFFF9F9F9)
//                            )
//                        }
//                    }
//
//
//                }
//                item {
//                    Column(
//                        modifier = Modifier
//                            .width(120.dp)
//                            .height(160.dp)
//                            .clip(RoundedCornerShape(16.dp))
//                            .background(
//                                color = Color(0XFFEEE3FF)
//                            )
//                            .clickable{
//                                name = "Drink Amount"
//                                showTextInput = true
//                            }
//                            .padding(20.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Column{
//                            Box(
//                                modifier = Modifier.clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
//                            ){
//                                Image(
//                                    painter = painterResource(R.drawable.water_icon),
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .width(18.dp)
//                                        .height(37.dp),
//                                )
//                            }
//                            Text(
//                                text = "Drink Water",
//                                style = TextStyle(
//                                    fontSize = 12.sp,
//                                    fontFamily = FontFamily(Font(R.font.verdana)),
//                                    fontWeight = FontWeight(400),
//                                    color = Color(0xFF313632),
//                                    letterSpacing = 1.sp,
//                                ),
//                                modifier = Modifier.padding(top = 5.dp)
//                            )
//                            Text(
//                                text = "${viewModel.drink} lt",
//                                style = TextStyle(
//                                    fontSize = 16.sp,
//                                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
//                                    fontWeight = FontWeight(400),
//                                    color = Color(0xFF313632),
//
//                                    letterSpacing = 1.sp,
//                                ),
//                                modifier = Modifier.padding(top = 5.dp)
//                            )
//                            LinearProgressIndicator(
//                                progress = { 1F },
//                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(3.dp)).padding(top = 2.dp),
//                                color = Color(0XFF9747FF),
//                                trackColor = Color(0XFFF9F9F9)
//                            )
//                        }
//
//                    }
//
//
//                }
//                item {
//                    Column(
//                        modifier = Modifier
//                            .width(120.dp)
//                            .height(160.dp)
//                            .clip(RoundedCornerShape(16.dp))
//                            .background(
//                                color = Color(0XFFFFE6F7)
//                            )
//                            .clickable{
//                                name = "Meditation Hours"
//                                showTextInput = true
//                            }
//                            .padding(20.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Column{
//                            Box(
//                                modifier = Modifier.clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
//                            ){
//                                Image(
//                                    painter = painterResource(R.drawable.meditation_icon),
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .width(37.dp)
//                                        .height(37.dp),
//                                )
//                            }
//                            Text(
//                                text = "Meditation",
//                                style = TextStyle(
//                                    fontSize = 12.sp,
//                                    fontFamily = FontFamily(Font(R.font.verdana)),
//                                    fontWeight = FontWeight(400),
//                                    color = Color(0xFF313632),
//                                    letterSpacing = 1.sp,
//                                ),
//                                modifier = Modifier.padding(top = 5.dp)
//                            )
//                            Text(
//                                text = "${viewModel.meditation} hour",
//                                style = TextStyle(
//                                    fontSize = 16.sp,
//                                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
//                                    fontWeight = FontWeight(400),
//                                    color = Color(0xFF313632),
//
//                                    letterSpacing = 1.sp,
//                                ),
//                                modifier = Modifier.padding(top = 5.dp)
//                            )
//                            LinearProgressIndicator(
//                                progress = { 1F },
//                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(3.dp)).padding(top = 2.dp),
//                                color = Color(0XFFDC30AD),
//                                trackColor = Color(0XFFF9F9F9)
//                            )
//                        }
//                    }
//
//
//                }
//                item {
//                    Column(
//                        modifier = Modifier
//                            .width(120.dp)
//                            .height(160.dp)
//                            .clip(RoundedCornerShape(16.dp))
//                            .background(
//                                color = Color(0XFFFFB4B4)
//                            )
//                            .clickable{
//                                name = "Steps Amount"
//                                showTextInput = true
//                            }
//                            .padding(20.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Column{
//                            Box(
//                                modifier = Modifier.clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
//                            ){
//                                Image(
//                                    painter = painterResource(R.drawable.running_icon),
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .width(37.dp)
//                                        .height(37.dp),
//                                )
//                            }
//                            Text(
//                                text = "Running",
//                                style = TextStyle(
//                                    fontSize = 12.sp,
//                                    fontFamily = FontFamily(Font(R.font.verdana)),
//                                    fontWeight = FontWeight(400),
//                                    color = Color(0xFF313632),
//                                    letterSpacing = 1.sp,
//                                ),
//                                modifier = Modifier.padding(top = 5.dp)
//                            )
//                            Text(
//                                text = "${viewModel.running} steps",
//                                style = TextStyle(
//                                    fontSize = 16.sp,
//                                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
//                                    fontWeight = FontWeight(400),
//                                    color = Color(0xFF313632),
//
//                                    letterSpacing = 1.sp,
//                                ),
//                                modifier = Modifier.padding(top = 5.dp)
//                            )
//                            LinearProgressIndicator(
//                                progress = { 1F },
//                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(3.dp)).padding(top = 2.dp),
//                                color = Color(0XFFDC30AD),
//                                trackColor = Color(0XFFF9F9F9)
//                            )
//                        }
//                    }
//
//
//                }
//                item {
//                    Column(
//                        modifier = Modifier
//                            .width(120.dp)
//                            .height(160.dp)
//                            .clip(RoundedCornerShape(16.dp))
//                            .background(
//                                color = Color(0XFFFFD6B4)
//                            )
//                            .clickable{
//                                name = "Pages Read"
//                                showTextInput = true
//                            }
//                         .padding(20.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Column{
//                            Box(
//                                modifier = Modifier.clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
//                            ){
//                                Image(
//                                    painter = painterResource(R.drawable.read_icon),
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .width(37.dp)
//                                        .height(37.dp),
//                                )
//                            }
//                            Text(
//                                text = "Read Book",
//                                style = TextStyle(
//                                    fontSize = 12.sp,
//                                    fontFamily = FontFamily(Font(R.font.verdana)),
//                                    fontWeight = FontWeight(400),
//                                    color = Color(0xFF313632),
//                                    letterSpacing = 1.sp,
//                                ),
//                                modifier = Modifier.padding(top = 5.dp)
//                            )
//                            Text(
//                                text = "${viewModel.read} pages",
//                                style = TextStyle(
//                                    fontSize = 16.sp,
//                                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
//                                    fontWeight = FontWeight(400),
//                                    color = Color(0xFF313632),
//
//                                    letterSpacing = 1.sp,
//                                ),
//                                modifier = Modifier.padding(top = 5.dp)
//                            )
//                            LinearProgressIndicator(
//                                progress = { 1F },
//                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(3.dp)).padding(top = 2.dp),
//                                color = Color(0XFFFD7A16),
//                                trackColor = Color(0XFF8433D1)
//                            )
//                        }
//                    }
//
//
//                }

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
            .padding(start = 24.dp, end = 24.dp, top = 6.dp, bottom = 40.dp)
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
                navController.navigate("scribble") {
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
    var metric by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf("") }

    val iconOptions = listOf(
        R.drawable.habits_icon,
        R.drawable.meditation_icon,
        R.drawable.exercise_icon,
        R.drawable.running_icon,
        R.drawable.water_icon,
        R.drawable.read_icon
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedIcon by remember { mutableStateOf(iconOptions.first()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Habit") },
        text = {
            Column {
                Text("Habit Name")
                TextField(value = name, onValueChange = { name = it })

                Spacer(Modifier.height(10.dp))

                Text("Metric (e.g. hours, steps, ml)")
                TextField(value = metric, onValueChange = { metric = it })

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
                            .clickable { expanded = true }
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
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
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
                                    expanded = false
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



@Preview(showBackground = true)
@Composable
fun Homeview() {
    // Gunakan dummy NavController untuk preview
    HomeScreen(navController = NavController(LocalContext.current), viewModel = viewModel(factory = HomeViewModel.Factory))
}