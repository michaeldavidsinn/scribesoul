package com.example.scribesoul.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    // --- KUNCI #1: Gunakan Box sebagai container utama ---
    // Box memungkinkan elemen di dalamnya untuk ditumpuk (stack)
    // atau diposisikan relatif terhadap Box itu sendiri.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF6F6F6),
                        Color(0xFFFFFFFF),
                        Color(0xFFF6F6F6),
                    )
                )
            )
    ) {
        // --- KUNCI #2: Column ini berisi SEMUA konten yang ingin bisa di-scroll ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Modifier .verticalScroll HANYA berlaku untuk Column ini dan isinya.
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header (Back button & Title)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clip(RoundedCornerShape(50))
                        .clickable { navController.popBackStack() } // Navigasi kembali
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    color = Color(0xFF2B395B)
                )
            }

            // Profile Picture
            Box(
                modifier = Modifier
                    .size(200.dp) // Ukuran avatar diperkecil agar lebih proporsional
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF74A8FF), Color(0xFF82D9D2))
                        ),
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cat2),
                    contentDescription = "Avatar",
                    modifier = Modifier.size(120.dp) // Ukuran gambar disesuaikan
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name
            Text(
                text = "Eloise Hamilton",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2B395B),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stats (Therapy Session & Completed Task)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val gradientBrush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF74A8FF), Color(0xFF82D9D2))
                )

                // Stat Item 1
                Box(
                    modifier = Modifier
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(50))
                        .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                        .padding(1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.White)
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "4",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF2B395B)
                        )
                        Text(
                            text = "Therapy Session",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF2B395B)
                        )
                    }
                }
                // Stat Item 2
                Box(
                    modifier = Modifier
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(50))
                        .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                        .padding(1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.White)
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "20",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF2B395B)
                        )
                        Text(
                            text = "Completed Task",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF2B395B)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Menu List
            val chipTexts = listOf(
                "Account Info", "Therapy History", "Achievement Unlock", "Customer Service", "Subscriptions"
            )
            val gradientBrush = Brush.horizontalGradient(
                colors = listOf(Color(0xFFFFF47A), Color(0xFFFFA8CF), Color(0xFFA774FF))
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp) // Jarak antar item menu
            ) {
                chipTexts.forEach { text ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                            .padding(1.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.White)
                            .clickable { /* TODO: Add navigation logic */ }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = Color(0xFF2B395B)
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = "Arrow Icon",
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(16.dp)
                                    .graphicsLayer(alpha = 0.99f)
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

            // --- SPACER TAMBAHAN UNTUK MEMASTIKAN KONTEN BISA DI-SCROLL ---
            // Spacer ini berada di dalam Column yang bisa scroll.
            // Anda bisa menghapus ini jika konten Anda sudah pasti lebih panjang dari layar.
            Spacer(modifier = Modifier.height(120.dp))
        }

        // --- KUNCI #3: Tombol LOG OUT berada di dalam Box utama, BUKAN di dalam Column scroll ---
        // Karena menjadi "sibling" dari Column scroll, posisinya tidak terpengaruh oleh scroll.
        // Modifier .align akan memposisikannya relatif terhadap parent-nya (Box).
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Menempel di bawah
                .padding(bottom = 32.dp) // Memberi jarak dari tepi bawah
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
                .clickable { /* TODO: Log out logic */ }
        ) {
            Text(
                text = "LOG OUT",
                modifier = Modifier.padding(horizontal = 50.dp, vertical = 16.dp),
                color = Color.White,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
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