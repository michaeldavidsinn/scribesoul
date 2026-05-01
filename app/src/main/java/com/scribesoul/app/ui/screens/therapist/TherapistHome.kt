package com.scribesoul.app.ui.screens.therapist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.scribesoul.app.ui.components.SchedulePill
import com.scribesoul.app.ui.navigation.BottomNavItem
import com.scribesoul.app.viewModels.HomeViewModel
import com.scribesoul.app.R

@Composable
fun TherapistHomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val bgGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF82D9D2), Color(0xFF74A8FF))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(0xFFF4FFFE))
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp) // Beri ruang untuk Navbar
        ) {
            // --- HEADER (Persis seperti HomeScreen) ---
            item {
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 56.dp, bottom = 20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Hi, Dr. Lisa", // Bisa diganti viewModel.user.name
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.verdana)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFF2B395B)
                            )
                        )
                        Text(
                            "Good Morning", // Bisa diganti viewModel.getGreetinng()
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
                            Text("SOS", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(brush = bgGradient, shape = CircleShape)
                                .padding(3.dp)
                                .clickable { navController.navigate("profile") }
                        ) {
                            Box(modifier = Modifier.clip(CircleShape).background(Color.White).padding(10.dp)) {
                                Image(
                                    painter = painterResource(R.drawable.cat2),
                                    contentDescription = null,
                                    modifier = Modifier.clip(CircleShape).size(30.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }

            // --- SECTION JADWAL (Dikecilkan) ---
            item {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFE0ECFF))
                        .padding(14.dp) // Pengecilan padding internal dari 20.dp ke 14.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp) // Jarak antar kolom dipersempit
                    ) {
                        // KOLOM KIRI
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Monday",
                                style = TextStyle(fontSize = 14.sp, color = Color(0xFF2B395B)) // Font lebih kecil
                            )
                            Text(
                                text = "23",
                                style = TextStyle(
                                    fontSize = 36.sp, // Pengecilan dari 48.sp ke 36.sp
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2B395B)
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            SchedulePill("Jake", "Anxiety", "09.00 - 10.00", Color(0xFF74A8FF))
                            SchedulePill("Jane Hoppers", "Depression", "10.00 - 11.00", Color(0xFFFFC107))
                            SchedulePill("Howard", "PTSD", "14.00 - 16.00", Color(0xFFD10000))
                        }

                        // KOLOM KANAN
                        Column(modifier = Modifier.weight(1.2f)) {
                            Text(
                                text = "Tomorrow",
                                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2B395B))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            SchedulePill("Jake", "Anxiety", "09.00 - 10.00", Color(0xFF74A8FF))
                            SchedulePill("Jane Hoppers", "Depression", "09.00 - 10.00", Color(0xFFFFC107))
                            Text("see other schedules", fontSize = 9.sp, color = Color.Gray, modifier = Modifier.padding(start = 4.dp))

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Wednesday, 25 Mar",
                                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2B395B))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            SchedulePill("Jake", "Anxiety", "09.00 - 10.00", Color(0xFF74A8FF))
                        }
                    }
                }
            }

// --- SECTION ACTIVE CLIENTS (Dikecilkan) ---
            item {
                Column(modifier = Modifier.padding(bottom = 20.dp)) {
                    Text(
                        text = "Active Clients",
                        style = TextStyle(
                            fontSize = 24.sp, // Pengecilan dari 32.sp ke 24.sp
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2B395B)
                        ),
                        modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 12.dp)
                    )

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFFFEBF2))
                            .padding(14.dp) // Pengecilan padding internal
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            ActiveClientItem("Sarah Gibson", "Anxiety", "Last Session: 3 days ago", Color(0xFF74A8FF))
                            ActiveClientItem("Jane Hoppers", "Depression", "Last Session: Yesterday", Color(0xFFFFC107))
                            ActiveClientItem("Eloise Bridgerton", "PTSD", "Last Session: 21 March 2026", Color(0xFFD10000))
                        }
                    }
                }
            }
        }

        // --- BOTTOM NAVBAR ---
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomBarTherapist(navController)
        }
    }
}


@Composable
fun ActiveClientItem(name: String, category: String, sessionDetail: String, statusColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(50.dp))
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.fillMaxHeight().width(5.dp).background(statusColor))
            Column(modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp)) {
                Text(text = name, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2B395B)))
                Text(text = category, fontSize = 10.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = sessionDetail,
                style = TextStyle(fontSize = 10.sp, color = Color.Gray, textAlign = TextAlign.End),
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
}

@Composable
fun BottomBarTherapist(navController: NavController, modifier: Modifier = Modifier) {
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
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                R.drawable.home_icon_clicked, "Home", iconSize = 46.dp, onClick = {
                    navController.navigate("home_therapist") {
                        launchSingleTop = true
                    }
                })

            BottomNavItem(R.drawable.therapist_icon, "Clients", iconSize = 25.dp, onClick = {
                navController.navigate("client_therapist") {
                    launchSingleTop = true
                }
            })

            BottomNavItem(R.drawable.explore_icon, "Explore", iconSize = 25.dp, onClick = {
                navController.navigate("explore_therapist") {
                    launchSingleTop = true
                }
            })

            BottomNavItem(R.drawable.schedule_icon, "Schedule", iconSize = 40.dp, onClick = {
                navController.navigate("schedule_therapist") {
                    launchSingleTop = true
                }
            })
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_9_pro_xl")
@Composable
fun TherapistHomePreview() {
    val context = LocalContext.current
    // Menggunakan NavController dummy untuk kebutuhan preview
    val navController = NavController(context)

    // Menggunakan viewModel factory yang sudah kamu miliki di kode sebelumnya
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        TherapistHomeScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
}