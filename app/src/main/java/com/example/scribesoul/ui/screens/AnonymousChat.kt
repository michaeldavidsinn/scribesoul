package com.example.scribesoul.ui.screens

import com.example.scribesoul.ui.components.InputBar
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.example.scribesoul.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.sp

import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.navigation.compose.rememberNavController
import com.example.scribesoul.ui.navigation.BottomNavItem


@Composable
fun AnonymousChatScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Lingkaran gradient
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleY = 1.15f // cukup sedikit, jangan terlalu besar
                    alpha = 0.9f // transparan agar teks tidak ketutupan
                }
        ) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFE0ECFF),
                        Color.Transparent
                    ),
                    center = Offset(size.width / 2, size.height / 2),
                    radius = size.minDimension * 0.5f
                )
            )
        }


        // Gunakan LazyColumn langsung sebagai konten scrollable
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = PaddingValues(0.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "SoulFess",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight(650),
                            fontSize = 32.sp // Sesuaikan ukuran yang lebih besar dari default
                        ),
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Share your story anonymously",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 10.sp
                        ),
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .offset(y = (-7).dp) // naikkan sedikit
                            .padding(bottom = 16.dp) // opsional, untuk jarak bawah
                    )
                }
            }

            itemsIndexed(List(10) { it }) { index, item ->
                Column {
                    ChatCard(
                        title = "Anonymous",
                        description = "This is message number $item."
                    )

                    if (index < 9) { // hanya tampilkan garis jika bukan item terakhir
                        Image(
                            painter = painterResource(id = R.drawable.gariswarna),
                            contentDescription = "Divider",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            InputBar()
            BottomBarAnonymous(navController = navController)
        }
    }
}

@Composable
fun ChatCard(
    title: String = "Anonymous",
    description: String = "This is an anonymous message.",
    likeCount: Int = 24,
    commentCount: Int = 12
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F8FF).copy(alpha = 0.4f) // 50% transparan
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()

        ) {
            // Avatar lingkaran
            Box(
                modifier = Modifier
                    .size(52.dp) // Ukuran lebih besar sedikit dari avatar
            ) {
                // Gradient border (tanpa fill background)
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawCircle(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF82D9D2), Color(0xFF74A8FF)),
                            start = Offset(0f, 0f),
                            end = Offset(size.width, size.height)
                        ),
                        style = Stroke(width = 4f) // Hanya border
                    )
                }

                // Avatar image di dalam border
                Image(
                    painter = painterResource(id = R.drawable.cat2),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(36.dp) // Supaya ada ruang untuk border 4dp
                        .align(Alignment.Center)
                        .clip(CircleShape)
                )
            }


            Spacer(modifier = Modifier.width(12.dp))

            // Kolom isi teks: title + deskripsi + ikon
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF2B395B)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = "16-06-2025",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF2B395B)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = { /* TODO: aksi titik tiga */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options",
                            tint = Color.Gray
                        )
                    }
                }

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF2B395B)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.like),
                            contentDescription = "Like",
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "24", // Ganti sesuai kebutuhan
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF2B395B)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.comment),
                            contentDescription = "Comment",
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "12", // Ganti sesuai kebutuhan
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF2B395B)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBarAnonymous(navController: NavController, modifier: Modifier = Modifier) {
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
            BottomNavItem(R.drawable.home_icon, "Home", iconSize = 28.dp) {
                navController.navigate("home") {
                    launchSingleTop = true
                }
            }
            BottomNavItem(R.drawable.therapist_icon, "Therapist", iconSize = 25.dp) {
                navController.navigate("therapist") {
                    launchSingleTop = true
                }
            }
            BottomNavItem(R.drawable.explore_icon_clicked, "Explore", iconSize = 40.dp) {
                navController.navigate("explore") {
                    launchSingleTop = true
                }
            }
            BottomNavItem(R.drawable.scribble_icon, "Scribble", iconSize = 28.dp) {
                navController.navigate("scribble") {
                    launchSingleTop = true
                }
            }
            BottomNavItem(R.drawable.journal_icon, "Journal", iconSize = 25.dp) {
                navController.navigate("journalList") {
                    launchSingleTop = true
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnonymousChatPreview() {
    val dummyController = rememberNavController()
    AnonymousChatScreen(navController = dummyController)
}
