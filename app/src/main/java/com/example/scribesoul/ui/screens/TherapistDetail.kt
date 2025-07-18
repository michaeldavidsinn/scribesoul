package com.example.scribesoul.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.scribesoul.R
import com.google.accompanist.flowlayout.FlowRow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TherapistDetailScreen(navController: NavController) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))

                        .clickable { /* TODO: action back */ }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(660.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(24.dp), clip = false) // 🌟 Shadow ditambahkan di sini
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White,
                                Color(0xFFE0FFFC)
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxSize()
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .border(0.2.dp, Color.Black, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color.White, Color(0xFFE0FFFC))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.cat2),
                            contentDescription = "Avatar Dokter",
                            modifier = Modifier.size(150.dp) // ukuran gambar di dalam avatar
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Dr. Maria Hartono",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Psikolog Anak & Remaja",
                        fontSize = 11.sp,
                        color = Color(0xFF2B395B),
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Gangguan Kecemasan dan PTSD",
                        fontSize = 11.sp,
                        color = Color(0xFF2B395B),
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
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
                                    .padding(horizontal = 12.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "7 Years Experience",
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
                                    .padding(horizontal = 12.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "100% Compatibility",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                                    color = Color(0xFF2B395B),
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Rp. 60.000,00",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Keahlian",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        mainAxisSpacing = 2.dp, // jarak horizontal antar chip
                        crossAxisSpacing = 6.dp // jarak vertikal antar baris
                    ) {
                        val gradientBrush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFF47A),
                                Color(0xFFFFA8CF),
                                Color(0xFFA774FF)
                            )
                        )

                        val chipTexts = listOf(
                            "Depresi", "Anxiety", "Trauma", "Adiksi", "Bipolar", "Pengembangan Diri"
                        )

                        chipTexts.forEach { text ->
                            Box(
                                modifier = Modifier
                                    .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                                    .padding(1.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(Color.White)
                                        .padding(horizontal = 12.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = text,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                                        color = Color(0xFF2B395B),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Alumnus",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        mainAxisSpacing = 2.dp, // jarak horizontal antar chip
                        crossAxisSpacing = 6.dp // jarak vertikal antar baris
                    ) {
                        val gradientBrush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFF47A),
                                Color(0xFFFFA8CF),
                                Color(0xFFA774FF)
                            )
                        )

                        val chipTexts = listOf(
                            "Universitas Ciputra Surabaya", "Universitas Gajah Mada",
                        )

                        chipTexts.forEach { text ->
                            Box(
                                modifier = Modifier
                                    .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                                    .padding(1.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(Color.White)
                                        .padding(horizontal = 12.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = text,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                                        color = Color(0xFF2B395B),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Praktik di",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        mainAxisSpacing = 2.dp, // jarak horizontal antar chip
                        crossAxisSpacing = 6.dp // jarak vertikal antar baris
                    ) {
                        val gradientBrush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFF47A),
                                Color(0xFFFFA8CF),
                                Color(0xFFA774FF)
                            )
                        )

                        val chipTexts = listOf(
                            "Rumah Sakit Jiwa",
                        )

                        chipTexts.forEach { text ->
                            Box(
                                modifier = Modifier
                                    .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                                    .padding(1.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(Color.White)
                                        .padding(horizontal = 12.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = text,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                                        color = Color(0xFF2B395B),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Nomor STR",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        mainAxisSpacing = 2.dp, // jarak horizontal antar chip
                        crossAxisSpacing = 6.dp // jarak vertikal antar baris
                    ) {
                        val gradientBrush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFF47A),
                                Color(0xFFFFA8CF),
                                Color(0xFFA774FF)
                            )
                        )

                        val chipTexts = listOf(
                            "12345678",
                        )

                        chipTexts.forEach { text ->
                            Box(
                                modifier = Modifier
                                    .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                                    .padding(1.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(Color.White)
                                        .padding(horizontal = 12.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = text,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                                        color = Color(0xFF2B395B),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
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

        // Bottom Bar tetap
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        ) {
            BottomBarTherapist(navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TherapistDetailPreview() {
    val context = LocalContext.current
    val navController = remember { NavController(context) }

    Surface(modifier = Modifier.fillMaxSize()) {
        TherapistDetailScreen(navController = navController)
    }
}