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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.scribesoul.R
import com.google.accompanist.flowlayout.FlowRow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TherapistProfileScreen(navController: NavController) {

    val gradientBrushs = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF74A8FF), // Warna awal
            Color(0xFF82D9D2)  // Warna akhir
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0x40000000), // Hitam dengan transparansi ~50%
                        Color(0xFFFFFDE6), // Krem Sangat Muda
                        Color(0xFFFFFFFF), // Putih
                        Color(0xFFFFFABE), // Krem Sangat Muda
                        Color(0x40000000), // Hitam dengan transparansi ~50%
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), // Menambahkan sedikit padding vertikal untuk header
                contentAlignment = Alignment.Center // Menyelaraskan item di tengah secara default
            ) {
                // Tombol Kembali (diselaraskan ke kiri)
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart) // << PENTING: Menyelaraskan item ini ke kiri tengah
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

                // Teks Judul (otomatis di tengah karena contentAlignment Box)
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight(650),
                        fontSize = 25.sp
                    ),
                    color = Color(0xFF2B395B),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .width(400.dp)
                    .height(660.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally, // <-- UBAH MENJADI INI
                    modifier = Modifier.fillMaxSize()
                ) {

                    Box(
                        modifier = Modifier
                            // Menggunakan .size() agar lebar & tinggi sama untuk lingkaran sempurna
                            .size(220.dp)
                            .border(
                                // --- MODIFIKASI DI SINI ---
                                width = 2.dp, // Lebar border ditipiskan dari 3.dp menjadi 2.dp
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF74A8FF), Color(0xFF82D9D2))
                                ),
                                shape = CircleShape
                            )
                            .clip(CircleShape) // Memotong konten di dalam (background & image) menjadi lingkaran
                            .background(Color.White), // Background di dalam box menjadi putih polos
                        contentAlignment = Alignment.Center // Atribut ini sudah membuat gambar di tengah lingkaran
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.cat2),
                            contentDescription = "Avatar Dokter",
                            modifier = Modifier.size(150.dp) // ukuran gambar di dalam avatar
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Eloise Hamilton",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val gradientBrush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF74A8FF), // Warna awal
                                Color(0xFF82D9D2)  // Warna akhir
                            )
                        )

                        // Experience
                        Box(
                            modifier = Modifier
                                // 1. Tambahkan shadow SEBELUM background
                                // 'elevation' mengontrol ukuran dan kelembutan bayangan
                                .shadow(elevation = 8.dp, shape = RoundedCornerShape(50))
                                // 2. Modifier selanjutnya tetap sama
                                .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                                .padding(1.dp) // Border thickness
                        ) {
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Color.White)
                                    // Padding vertikal sedikit ditambah agar ada ruang untuk 2 baris
                                    .padding(horizontal = 18.dp, vertical = 6.dp),
                                // Menengahkan kedua teks secara horizontal
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Teks baru yang lebih besar
                                Text(
                                    text = "4",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFF2B395B)
                                )
                                // Teks original
                                Text(
                                    text = "Therapy Session",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                    color = Color(0xFF2B395B)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Compatibility
                        Box(
                            modifier = Modifier
                                .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                                .shadow(elevation = 8.dp, shape = RoundedCornerShape(50))
                                .padding(1.dp) // Border thickness
                        ) {
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Color.White)
                                    // Padding vertikal sedikit ditambah agar ada ruang untuk 2 baris
                                    .padding(horizontal = 18.dp, vertical = 6.dp),
                                // Menengahkan kedua teks secara horizontal
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Teks baru yang lebih besar
                                Text(
                                    text = "20",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFF2B395B)
                                )
                                // Teks original
                                Text(
                                    text = "Completed Task",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                    color = Color(0xFF2B395B)
                                )
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(20.dp))

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
                            "Account Info", "Therapy History", "Achievement Unlock", "Customer Service"
                        )

                        chipTexts.forEach { text ->
                            Box(
                                modifier = Modifier
                                    .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                                    .padding(1.dp)


                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(50))
                                        .background(Color.White)
                                        .padding(horizontal = 24.dp, vertical = 20.dp),
                                    // Menyusun item secara horizontal dengan jarak di antaranya
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    // Menyelaraskan item di tengah secara vertikal
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = text,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = Color(0xFF2B395B),
                                        maxLines = 1
                                    )

                                    // Ikon tidak perlu modifier align lagi karena sudah diatur oleh Row
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                        contentDescription = "Arrow Icon",
                                        tint = Color.Unspecified, // Tint di-override oleh brush
                                        modifier = Modifier
                                            .size(18.dp)
                                            .graphicsLayer(alpha = 0.99f) // Diperlukan agar brush berfungsi
                                            .drawWithCache {
                                                onDrawWithContent {
                                                    drawContent()
                                                    drawRect(gradientBrushs, blendMode = BlendMode.SrcAtop)
                                                }
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
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
                    .padding(horizontal = 50.dp, vertical = 13.dp)
            ) {
                Text(
                    text = "LOG OUT",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TherapistProfilePreview() {
    val context = LocalContext.current
    val navController = remember { NavController(context) }

    Surface(modifier = Modifier.fillMaxSize()) {
        TherapistProfileScreen(navController = navController)
    }
}