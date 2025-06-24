package com.example.scribesoul.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scribesoul.R

@Composable
fun TherapistRecommendationScreen() {
    val therapistList = listOf(
        Triple("Dr. Andini Pramudita", "Psikolog Klinis", "Depresi & Kecemasan"),
        Triple("Dr. Raka Mahendra", "Psikolog Anak", "Gangguan Perilaku & ADHD"),
        Triple("Dr. Eliza Syahputri", "Psikolog Pernikahan", "Masalah Relasi & Komunikasi"),
        Triple("Dr. Fajar Santoso", "Psikolog Remaja", "Masalah Identitas Diri"),
        Triple("Dr. Mita Ardhana", "Psikolog Klinis", "Burnout & Stres Kerja"),
        Triple("Dr. Samuel Hartono", "Psikolog Sosial", "Kecemasan Sosial"),
        Triple("Dr. Intan Maheswari", "Psikolog Anak", "Autisme & Perkembangan"),
        Triple("Dr. Kevin Salim", "Psikolog Umum", "Overthinking & Insomnia"),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFFFFDE6)
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Recommendation",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight(650),
                            fontSize = 32.sp
                        ),
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            items(therapistList) { (name, specialization, issue) ->
                TherapistCard(
                    name = name,
                    specialization = specialization,
                    issue = issue,
                    experienceYears = (5..15).random(),
                    compatibility = (90..100).random(),
                    price = "Rp 250.000,00",
                    onChatClick = { /* Action placeholder */ }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp)) // Bottom padding
            }
        }

        // Bottom bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        ) {
            BottomBarTherapist()
        }
    }
}

@Composable
fun TherapistCard(
    name: String,
    specialization: String,
    issue: String,
    experienceYears: Int,
    compatibility: Int,
    price: String,
    onChatClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar box
            Box(
                modifier = Modifier
                    .height(100.dp) // Sedikit lebih pendek dari sebelumnya
                    .width(80.dp) // Tetap lebar seperti sebelumnya
                    .shadow(6.dp, shape = RoundedCornerShape(12.dp), clip = false)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cat2),
                    contentDescription = "Therapist Avatar",
                    modifier = Modifier
                        .fillMaxSize(0.85f) // Ukuran gambar diperkecil jadi 85% dari Box
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp),
                        color = Color(0xFF2B395B)
                    )
                    Text(
                        text = specialization,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = Color(0xFF2B395B)
                    )
                    Text(
                        text = "$issue",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = Color(0xFF2B395B)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

// Baris Experience + Compatibility
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val gradientBrush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFFF47A),
                            Color(0xFFFFA8CF),
                            Color(0xFFA774FF)
                        )
                    )

                    // Experience
                    Box(
                        modifier = Modifier
                            .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                            .padding(1.dp) // Border thickness
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color.White)
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "$experienceYears Years Experience",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                color = Color(0xFF2B395B)
                            )
                        }
                    }

                    // Compatibility
                    Box(
                        modifier = Modifier
                            .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                            .padding(1.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color.White)
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "$compatibility% Compatibility",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                                color = Color(0xFF2B395B),
                                maxLines = 1
                            )
                        }
                    }
                }


                Spacer(modifier = Modifier.height(8.dp))

// Baris Price + Chat
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = price,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = Color(0xFF2B395B)
                    )

                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF82D9D2),
                                        Color(0xFF7CC3E6),
                                        Color(0xFF74A8FF)
                                    )
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .clickable { onChatClick() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Chat",
                            modifier = Modifier.padding(horizontal = 20.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBarTherapist(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(start = 24.dp, end = 24.dp, top = 6.dp, bottom = 20.dp)
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
            BottomNavItem(R.drawable.home_icon, "Home", 28.dp)
            BottomNavItem(R.drawable.therapist_icon_clicked, "Therapist", 40.dp)
            BottomNavItem(R.drawable.explore_icon, "Explore", 25.dp)
            BottomNavItem(R.drawable.scribble_icon, "Scribble", 28.dp)
            BottomNavItem(R.drawable.journal_icon, "Journal", 25.dp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TherapistRecommendationPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        TherapistRecommendationScreen()
    }
}
