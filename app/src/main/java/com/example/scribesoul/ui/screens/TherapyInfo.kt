package com.example.scribesoul.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.BlendMode
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TherapyInfoScreen(navController: NavController) {

    val gradientBrushs = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF74A8FF),
            Color(0xFF82D9D2)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFDE6),
                        Color(0xFFFFFDE6),
                        Color(0xFFFFFABE),
                        Color(0xFFFFFDE6),
                    )
                )
            )
    ) {
        // Konten scrollable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 56.dp), // padding atas biar ga ketutup tombol back
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(400.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        mainAxisSpacing = 2.dp,
                        crossAxisSpacing = 6.dp
                    ) {
                        val gradientBrush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFF47A),
                                Color(0xFFFFA8CF),
                                Color(0xFFA774FF)
                            )
                        )

                        Column {
                            InfoCard(
                                title = "Terapist Info",
                                items = listOf(
                                    "Name" to "Dr. Lisa Hermawan S.Psi, M.Psi, Psikolog",
                                    "Expertise" to "Depression, Anxiety...",
                                    "STR Number" to "123456789876534"
                                ),
                                gradient = gradientBrush
                            )

                            InfoCard(
                                title = "Patient Info",
                                items = listOf(
                                    "Name" to "Eloise Hamilton",
                                    "Expertise" to "Depression, Anxiety..."
                                ),
                                gradient = gradientBrush
                            )

                            InfoCard(
                                title = "Session Info",
                                items = listOf(
                                    "Date" to "Wednesday, 17 September 2025",
                                    "Time" to "19:00",
                                    "Duration" to "2 hours",
                                    "Location" to "Online Zoom"
                                ),
                                gradient = gradientBrush
                            )

                            InfoCard(
                                title = "Billing Info",
                                items = listOf(
                                    "Payment Method" to "GoPay",
                                    "Account" to "0833923849",
                                    "First Name" to "Eloise",
                                    "Last Name" to "Hamilton",
                                    "Price" to "Rp 180.000,00",
                                    "Payment Status" to "Done"
                                ),
                                gradient = gradientBrush
                            )
                        }
                    }
                }
            }
        }

        // Tombol back (ikon panah) fixed di atas kiri
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .clip(RoundedCornerShape(50))
                .clickable { navController.popBackStack() }
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }

        // Tombol BACK (teks) fixed di bawah tengah
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = 64.dp)
                .clip(RoundedCornerShape(50))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF82D9D2),
                            Color(0xFF7CC3E6),
                            Color(0xFF74A8FF)
                        )
                    )
                )
                .clickable { navController.popBackStack() }
                .padding(horizontal = 50.dp, vertical = 13.dp)
        ) {
            Text(
                text = "BOOK SESSION",
                color = Color.White,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    items: List<Pair<String, String>>, // Label - Value
    gradient: Brush
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // Judul kecil di atas tabel
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            ),
            color = Color(0xFF2B395B),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // Box tabel dengan border gradient
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(brush = gradient)
                .padding(1.dp) // border tipis
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            ) {
                items.forEachIndexed { index, (label, value) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Kolom label
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF2B395B),
                            modifier = Modifier.weight(1f) // label ambil 1 bagian
                        )

                        // Kolom value
                        Text(
                            text = value,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 13.sp
                            ),
                            color = Color.Black,
                            modifier = Modifier.weight(2f) // value ambil 2 bagian, biar rata kiri
                        )
                    }

                    // Garis pemisah antar row (kecuali terakhir)
                    if (index != items.lastIndex) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFFE0E0E0))
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TherapyInfoPreview() {
    val context = LocalContext.current
    val navController = remember { NavController(context) }

    Surface(modifier = Modifier.fillMaxSize()) {
        TherapyInfoScreen(navController = navController)
    }
}
