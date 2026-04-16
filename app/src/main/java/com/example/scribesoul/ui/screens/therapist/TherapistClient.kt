package com.example.scribesoul.ui.screens.therapist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import androidx.navigation.compose.rememberNavController
import com.example.scribesoul.R
import com.example.scribesoul.ui.navigation.BottomNavItem
import com.example.scribesoul.viewModels.HomeViewModel

enum class ClientStage {
    MILD, MEDIUM, URGENT
}

@Composable
fun ClientTherapistScreen(navController: NavController, viewModel: HomeViewModel) {
    var currentStage by remember { mutableStateOf(ClientStage.MILD) }

    val stageColors = mapOf(
        ClientStage.MILD to Color(0xFFBCD5FF),
        ClientStage.MEDIUM to Color(0xFFFFD6B4),
        ClientStage.URGENT to Color(0xFFFFB4B4)
    )

    val indicatorColors = mapOf(
        ClientStage.MILD to Color(0xFF74A8FF),
        ClientStage.MEDIUM to Color(0xFFFFC107),
        ClientStage.URGENT to Color(0xFFD10000)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 56.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Foto profil di pojok kanan atas
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = painterResource(R.drawable.cat2),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clip(CircleShape)
                                .size(44.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Judul "Clients" di tengah
                    Text(
                        text = "Clients",
                        style = TextStyle(
                            fontSize = 32.sp,
                            fontFamily = FontFamily(Font(R.font.verdana_bold)),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2B395B)
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Tabs dengan warna khusus
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StageTab("MILD", ClientStage.MILD, currentStage, Color(0xFFBCD5FF)) { currentStage = it }
                        StageTab("MEDIUM", ClientStage.MEDIUM, currentStage, Color(0xFFFFD6B4)) { currentStage = it }
                        StageTab("URGENT", ClientStage.URGENT, currentStage, Color(0xFFFFB4B4)) { currentStage = it }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    repeat(3) {
                        TherapistClientCard(
                            name = "Jake Connor", // Gunakan string langsung karena tidak ada objek 'client'
                            category = "Anxiety",
                            lastSession = "3 days ago",
                            cardColor = stageColors[currentStage]!!,
                            indicatorColor = indicatorColors[currentStage]!!,
                            onClick = {
                                // Langsung arahkan ke detail dengan nama hardcoded untuk sementara
                                navController.navigate("client_detail/Jake Connor")
                            }
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomBarTherapistAnonymous(navController)
        }
    }
}

@Composable
fun StageTab(
    label: String,
    stage: ClientStage,
    currentStage: ClientStage,
    tabColor: Color, // Warna dasar tab (Biru/Oranye/Pink)
    onClick: (ClientStage) -> Unit
) {
    val isActive = stage == currentStage
    Box(
        modifier = Modifier
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(50.dp))
            .clip(RoundedCornerShape(50.dp))
            .background(tabColor) // Selalu berwarna sesuai tabColor

            .clickable { onClick(stage) }
            .padding(horizontal = 24.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.verdana_bold)),
            color = Color(0xFF2B395B)
        )
    }
}

@Composable
fun TherapistClientCard(
    name: String,
    category: String,
    lastSession: String,
    cardColor: Color,
    indicatorColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(cardColor)
            .clickable { onClick() }
            .padding(28.dp)
    ) {
        // --- GUNAKAN ROW UNTUK MENGETENGAHKAN ICON SECARA VERTIKAL ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // Mengetengahkan panah di tengah kartu
        ) {
            Column(
                modifier = Modifier.weight(1f) // Memberi ruang agar teks di kiri dan panah di kanan
            ) {
                // Nama Pasien (Tanpa Icon di dalam baris ini)
                Text(
                    text = name,
                    style = TextStyle(
                        fontSize = 36.sp,
                        fontFamily = FontFamily(Font(R.font.verdana_bold)),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B395B)
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Pill Kategori
                Box(
                    modifier = Modifier
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(50.dp))
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White.copy(alpha = 0.8f))
                ) {
                    Row(
                        modifier = Modifier.height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(6.dp)
                                .background(indicatorColor)
                        )
                        Text(
                            text = category,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.verdana_bold)),
                            color = Color(0xFF2B395B),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Sesi Terakhir
                Text(
                    text = "Last Session: $lastSession",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.verdana)),
                    color = Color(0xFF2B395B).copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Section Notes
                Text(
                    text = "Notes:",
                    style = TextStyle(
                        fontSize = 18.sp, // Ukuran font sesuai tampilan Figma
                        fontFamily = FontFamily(Font(R.font.verdana)),
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF2B395B) // Biru gelap, bukan putih
                    )
                )

                Spacer(modifier = Modifier.height(40.dp))
            }

            // --- ICON PANAH SEKARANG DI SINI (SEJAJAR DENGAN COLUMN) ---
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF2B395B),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun BottomBarTherapistAnonymous(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 40.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(30.dp), clip = false)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
            .height(72.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(R.drawable.home_icon, "Home", iconSize = 28.dp) {
                navController.navigate("home_therapist") { launchSingleTop = true }
            }
            BottomNavItem(R.drawable.therapist_icon_clicked, "Clients", iconSize = 40.dp) {
                navController.navigate("client_therapist") { launchSingleTop = true }
            }
            BottomNavItem(R.drawable.explore_icon, "Explore", iconSize = 28.dp) {
                navController.navigate("explore_therapist") { launchSingleTop = true }
            }
            BottomNavItem(R.drawable.schedule_icon, "Schedule", iconSize = 40.dp) {
                navController.navigate("schedule_therapist") { launchSingleTop = true }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_9_pro_xl")
@Composable
fun ClientTherapistPreview() {
    val navController = rememberNavController()
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        ClientTherapistScreen(navController = navController, viewModel = viewModel(factory = HomeViewModel.Factory))
    }
}