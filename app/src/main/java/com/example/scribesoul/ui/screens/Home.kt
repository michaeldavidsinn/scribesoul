package com.example.scribesoul.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scribesoul.R
import com.example.scribesoul.ui.navigation.BottomNavItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController) {
    val bgGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF82d9d2),
            Color(0xFFF74a8ff),
        )
    )

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
            Row (modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 30.dp, bottom = 10.dp).fillMaxWidth()){
                Column {
                    Text("Hi, Jake",
                        style =
                            TextStyle(
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFF2B395B)
                            ))
                    Text("Good Morning",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
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
                horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.padding( top = 5.dp).align(alignment = Alignment.CenterHorizontally), contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
            ){
                item {
                    Column(
                        modifier = Modifier.width(48.dp).align(Alignment.CenterHorizontally), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Sat",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(400),
                                fontSize = 12.sp,
                                color = Color(0xFF5F5F5F)
                            ))
                        Text("21"
                            ,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(600),
                                fontSize = 14.sp,
                                color = Color(0xFF121212)
                            ))
                    }
                }
                item {
                    Column(
                        modifier = Modifier.width(48.dp).align(Alignment.CenterHorizontally), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Sun",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(400),
                                fontSize = 12.sp,
                                color = Color(0xFF5F5F5F)
                            ))
                        Text("22"
                            ,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(600),
                                fontSize = 14.sp,
                                color = Color(0xFF121212)
                            ))
                    }
                }
                item {
                    Column(
                        modifier = Modifier.width(48.dp).align(Alignment.CenterHorizontally), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Mon",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(400),
                                fontSize = 12.sp,
                                color = Color(0xFF5F5F5F)
                            ))
                        Text("23"
                            ,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(600),
                                fontSize = 14.sp,
                                color = Color(0xFF121212)
                            ))
                    }
                }
                item {
                    Column(
                        modifier = Modifier.width(48.dp).align(Alignment.CenterHorizontally), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Mon",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(400),
                                fontSize = 12.sp,
                                color = Color(0xFF5F5F5F)
                            ))
                        Text("23"
                            ,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(600),
                                fontSize = 14.sp,
                                color = Color(0xFF121212)
                            ))
                    }
                }
                item {
                    Column(
                        modifier = Modifier.width(48.dp).align(Alignment.CenterHorizontally), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Mon",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(400),
                                fontSize = 12.sp,
                                color = Color(0xFF5F5F5F)
                            ))
                        Text("23"
                            ,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(600),
                                fontSize = 14.sp,
                                color = Color(0xFF121212)
                            ))
                    }
                }
                item {
                    Column(
                        modifier = Modifier.width(48.dp).align(Alignment.CenterHorizontally), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Mon",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(400),
                                fontSize = 12.sp,
                                color = Color(0xFF5F5F5F)
                            ))
                        Text("23"
                            ,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(600),
                                fontSize = 14.sp,
                                color = Color(0xFF121212)
                            ))
                    }
                }
                item {
                    Column(
                        modifier = Modifier.width(48.dp).align(Alignment.CenterHorizontally), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Mon",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(400),
                                fontSize = 12.sp,
                                color = Color(0xFF5F5F5F)
                            ))
                        Text("23"
                            ,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(600),
                                fontSize = 14.sp,
                                color = Color(0xFF121212)
                            ))
                    }
                }
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding( top = 5.dp).align(alignment = Alignment.Start), contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
            ) {
                item{

                        Column (
                            modifier = Modifier.clickable(onClick = {
                                navController.navigate("mental")
                            }),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background( brush = bgGradient, shape = CircleShape)
                                    .padding(3.dp)
                                    .align(Alignment.CenterHorizontally),
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
                            Text(text = "Stress",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight(400),
                                    fontSize = 12.sp,
                                    color = Color(0xFF5F5F5F)
                                ))

                        }
                }
                item{

                    Column (
                        modifier = Modifier.clickable(onClick = {

                        }),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background( brush = bgGradient, shape = CircleShape)
                                .padding(3.dp)
                                .align(Alignment.CenterHorizontally),
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
                        Text(text = "Anxiety",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(400),
                                fontSize = 12.sp,
                                color = Color(0xFF5F5F5F)
                            ))

                    }
                }
                item{

                    Column (
                        modifier = Modifier.clickable(onClick = {

                        }),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background( brush = bgGradient, shape = CircleShape)
                                .padding(3.dp)
                                .align(Alignment.CenterHorizontally),
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
                        Text(text = "Insomnia",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(400),
                                fontSize = 12.sp,
                                color = Color(0xFF5F5F5F)
                            ))

                    }
                }
                item{

                    Column (
                        modifier = Modifier.clickable(onClick = {

                        }),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background( brush = bgGradient, shape = CircleShape)
                                .padding(3.dp)
                                .align(Alignment.CenterHorizontally),
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
                        Text(text = "Overthinking",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(400),
                                fontSize = 12.sp,
                                color = Color(0xFF5F5F5F)
                            ))

                    }
                }
                item{

                    Column (
                        modifier = Modifier.clickable(onClick = {

                        }),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background( brush = bgGradient, shape = CircleShape)
                                .padding(3.dp)
                                .align(Alignment.CenterHorizontally),
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
                        Text(text = "Fatigue",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(400),
                                fontSize = 12.sp,
                                color = Color(0xFF5F5F5F)
                            ))

                    }
                }
                item{

                    Column (
                        modifier = Modifier.clickable(onClick = {

                        }),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background( brush = bgGradient, shape = CircleShape)
                                .padding(3.dp)
                                .align(Alignment.CenterHorizontally),
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
                        Text(text = "Mood Swings",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(400),
                                fontSize = 12.sp,
                                color = Color(0xFF5F5F5F)
                            ))

                    }
                }

                item{

                    Column (
                        modifier = Modifier.clickable(onClick = {

                        }),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background( brush = bgGradient, shape = CircleShape)
                                .padding(3.dp)
                                .align(Alignment.CenterHorizontally),
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
                        Text(text = "Panic Attack",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight(400),
                                fontSize = 12.sp,
                                color = Color(0xFF5F5F5F)
                            ))

                    }
                }



            }

            LazyVerticalGrid (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                columns = GridCells.Adaptive(minSize = 175.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .width(120.dp)
                            .height(160.dp)

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
                                modifier = Modifier.clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
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
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF313632),
                                    letterSpacing = 1.sp,
                                ),
                                modifier = Modifier.padding(top = 5.dp)
                            )
                            Text(
                                text = "7 Habits",
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
                                progress = { 1F },
                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(3.dp)).padding(top = 2.dp),
                                color = Color(0XFF40B490),
                                trackColor = Color(0XFFF9F9F9)
                            )
                        }
                    }


                }
                item {
                    Column(
                        modifier = Modifier
                            .width(120.dp)
                            .height(160.dp)


                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                color = Color(0XFFE0ECFF)
                            )
                            .padding(20.dp)
                            ,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column{
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
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
                                text = "Exercise",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF313632),
                                    letterSpacing = 1.sp,
                                ),
                                modifier = Modifier.padding(top = 5.dp)
                            )
                            Text(
                                text = "2 hours",
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
                                progress = { 1F },
                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(3.dp)).padding(top = 2.dp),
                                color = Color(0XFF5373FF),
                                trackColor = Color(0XFFF9F9F9)
                            )
                        }
                    }


                }
                item {
                    Column(
                        modifier = Modifier
                            .width(120.dp)
                            .height(160.dp)
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
                                modifier = Modifier.clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
                            ){
                                Image(
                                    painter = painterResource(R.drawable.water_icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(18.dp)
                                        .height(37.dp),
                                )
                            }
                            Text(
                                text = "Drink Water",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF313632),
                                    letterSpacing = 1.sp,
                                ),
                                modifier = Modifier.padding(top = 5.dp)
                            )
                            Text(
                                text = "2 lt",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF313632),

                                    letterSpacing = 1.sp,
                                ),
                                modifier = Modifier.padding(top = 5.dp)
                            )
                            LinearProgressIndicator(
                                progress = { 1F },
                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(3.dp)).padding(top = 2.dp),
                                color = Color(0XFF9747FF),
                                trackColor = Color(0XFFF9F9F9)
                            )
                        }

                    }


                }
                item {
                    Column(
                        modifier = Modifier
                            .width(120.dp)
                            .height(160.dp)
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
                                modifier = Modifier.clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
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
                                text = "Meditation",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF313632),
                                    letterSpacing = 1.sp,
                                ),
                                modifier = Modifier.padding(top = 5.dp)
                            )
                            Text(
                                text = "1 hour",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF313632),

                                    letterSpacing = 1.sp,
                                ),
                                modifier = Modifier.padding(top = 5.dp)
                            )
                            LinearProgressIndicator(
                                progress = { 1F },
                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(3.dp)).padding(top = 2.dp),
                                color = Color(0XFFDC30AD),
                                trackColor = Color(0XFFF9F9F9)
                            )
                        }
                    }


                }
                item {
                    Column(
                        modifier = Modifier
                            .width(120.dp)
                            .height(160.dp)
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
                                modifier = Modifier.clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
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
                                text = "Running",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF313632),
                                    letterSpacing = 1.sp,
                                ),
                                modifier = Modifier.padding(top = 5.dp)
                            )
                            Text(
                                text = "4000 steps",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF313632),

                                    letterSpacing = 1.sp,
                                ),
                                modifier = Modifier.padding(top = 5.dp)
                            )
                            LinearProgressIndicator(
                                progress = { 1F },
                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(3.dp)).padding(top = 2.dp),
                                color = Color(0XFFDC30AD),
                                trackColor = Color(0XFFF9F9F9)
                            )
                        }
                    }


                }
                item {
                    Column(
                        modifier = Modifier
                            .width(120.dp)
                            .height(160.dp)
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
                                modifier = Modifier.clip(RoundedCornerShape(16.dp)).size(56.dp).background(Color(0XFFF9F9F9)), contentAlignment = Alignment.Center
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
                                text = "Read Book",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF313632),
                                    letterSpacing = 1.sp,
                                ),
                                modifier = Modifier.padding(top = 5.dp)
                            )
                            Text(
                                text = "150 pages",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF313632),

                                    letterSpacing = 1.sp,
                                ),
                                modifier = Modifier.padding(top = 5.dp)
                            )
                            LinearProgressIndicator(
                                progress = { 1F },
                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(3.dp)).padding(top = 2.dp),
                                color = Color(0XFFFD7A16),
                                trackColor = Color(0XFF8433D1)
                            )
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


@Preview(showBackground = true)
@Composable
fun Homeview() {
    // Gunakan dummy NavController untuk preview
    HomeScreen(navController = NavController(LocalContext.current))
}