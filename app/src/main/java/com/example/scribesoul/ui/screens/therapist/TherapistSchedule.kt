package com.example.scribesoul.ui.screens.therapist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scribesoul.R
import com.example.scribesoul.ui.navigation.BottomNavItem
import com.example.scribesoul.viewModels.HomeViewModel

@Composable
fun TherapistScheduleScreen(navController: NavController, viewModel: HomeViewModel) {
    val bgGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF82D9D2), Color(0xFF74A8FF))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // --- HEADER (Persis seperti TherapistHomeScreen) ---
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 56.dp, bottom = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Hi, Dr. Lisa",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.verdana)),
                            color = Color(0xFF2B395B)
                        )
                    )
                    Text(
                        "Good Morning",
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

            // --- DATE SELECTOR (Mewakili hari dalam seminggu) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val days = listOf("Sat" to "21", "Sun" to "22", "Mon" to "23", "Tue" to "24", "Wed" to "25", "Thu" to "26", "Fri" to "27")
                days.forEach { (day, date) ->
                    val isSelected = date == "23" // Contoh hari aktif sesuai Figma
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(day, fontSize = 12.sp, color = if (isSelected) Color(0xFF2B395B) else Color.Gray)
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) Color(0xFF74A8FF) else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                date,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color.Black
                            )
                        }
                        if (isSelected) {
                            Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Color(0xFF74A8FF)))
                        }
                    }
                }
            }

            // --- SCHEDULE SESSION CONTAINER ---
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFBCD5FF)) // Background biru muda kontainer
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        "Schedule Session",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.verdana_bold)),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2B395B)
                        )
                    )

                    // Tabel Waktu Scrollable
                    Box(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                        Column {
                            val hours = (0..23).map { String.format("%02d:00", it) }
                            hours.forEach { hour ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(30.dp), // Sesuaikan tinggi row agar tidak terlalu rapat
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = hour,
                                        modifier = Modifier.width(55.dp).padding(start = 8.dp), // Lebar disesuaikan sedikit
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily(Font(R.font.verdana_bold)),
                                            color = Color.White
                                        )
                                    )

                                    // --- TAMBAHKAN SPACER DI SINI ---
                                    Spacer(modifier = Modifier.width(12.dp))

                                    HorizontalDivider(
                                        modifier = Modifier
                                            .padding(top = 8.dp) // Sesuaikan posisi vertikal garis agar sejajar tengah teks
                                            .weight(1f), // Agar garis memenuhi sisa ruang ke kanan
                                        thickness = 1.dp, // Ketebalan 1.dp biasanya lebih rapi untuk grid
                                        color = Color.White.copy(alpha = 0.3f) // Transparansi dikurangi sedikit agar lebih halus
                                    )
                                }
                            }
                        }

                        // Menaruh "Pills" Jadwal pada posisi tertentu
                        Column(modifier = Modifier.padding(start = 55.dp)) {
                            ScheduleGridItem("Jake", "Anxiety", "09:00 - 10:30", Color(0xFF74A8FF), durationHours = 1.5f)

// Sesi 1 jam
                            ScheduleGridItem("Jane Hoppers", "Depression", "10:30 - 11:30", Color(0xFFFFC107), durationHours = 1f)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp)) // Ruang untuk BottomBar
        }

        // --- BOTTOM NAVBAR (Tetap menggunakan BottomBarTherapist) ---
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomBarTherapistSchedule(navController)
        }
    }
}
@Composable
fun ScheduleGridItem(
    name: String,
    category: String,
    time: String,
    indicatorColor: Color,
    durationHours: Float = 1f // Tambahkan parameter durasi (misal 1.5f untuk 1,5 jam)
) {
    // Menghitung tinggi berdasarkan durasi. Jika 1 jam = 60dp, maka durasi * 60.
    val cardHeight = (durationHours * 60).dp

    Box(
        modifier = Modifier
            .padding(top = 2.dp, bottom = 2.dp, end = 16.dp)
            .width(200.dp) // Sedikit dikecilkan lebar totalnya
            .height(cardHeight) // Tinggi dinamis berdasarkan durasi sesi
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp, topEnd = 24.dp, bottomEnd = 24.dp)
            )
            .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp, topEnd = 24.dp, bottomEnd = 24.dp))
            .background(Color.White.copy(alpha = 0.85f))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Garis indikator tetap mengikuti tinggi kartu
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(5.dp) // Sedikit lebih tipis
                    .background(indicatorColor)
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center // Teks tetap di tengah secara vertikal
            ) {
                Text(
                    text = name,
                    style = TextStyle(
                        fontSize = 13.sp, // Dikecilkan dari 16.sp
                        fontFamily = FontFamily(Font(R.font.verdana_bold)),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B395B)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = category,
                    style = TextStyle(
                        fontSize = 10.sp, // Dikecilkan dari 11.sp
                        fontFamily = FontFamily(Font(R.font.verdana)),
                        color = Color(0xFF2B395B).copy(alpha = 0.7f)
                    ),
                    maxLines = 1
                )
                Text(
                    text = time,
                    style = TextStyle(
                        fontSize = 10.sp, // Dikecilkan dari 11.sp
                        fontFamily = FontFamily(Font(R.font.verdana)),
                        color = Color(0xFF2B395B).copy(alpha = 0.7f)
                    )
                )
            }
        }
    }
}

@Composable
fun BottomBarTherapistSchedule(navController: NavController, modifier: Modifier = Modifier) {
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
                R.drawable.home_icon, "Home", iconSize = 46.dp, onClick = {
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

            BottomNavItem(R.drawable.schedule_icon_clicked, "Schedule", iconSize = 50.dp, onClick = {
                navController.navigate("schedule_therapist") {
                    launchSingleTop = true
                }
            })
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_9_pro_xl")
@Composable
fun TherapistSchedulePreview() {
    val context = LocalContext.current
    val navController = NavController(context)

    // Menggunakan factory yang sama dengan HomeScreen kamu
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White // Latar belakang putih bersih sesuai figma
    ) {
        TherapistScheduleScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
}