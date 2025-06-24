package com.example.scribesoul.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scribesoul.R

@Composable
fun ScribbleScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFFFE6ED)
                    )
                )
            )
    ) {

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
                        text = "Scribble",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight(650),
                            fontSize = 32.sp // Sesuaikan ukuran yang lebih besar dari default
                        ),
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Express yourself here",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 10.sp
                        ),
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .offset(y = (-7).dp) // naikkan sedikit
                            .padding(bottom = 16.dp) // opsional, untuk jarak bawah
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .width(275.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(Color(0xFFE0ECFF), Color.White),
                                    center = Offset.Unspecified, // Defaultnya di tengah
                                    radius = 500f // Sesuaikan agar menyebar dengan baik di ukuran box
                                )
                            )
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "BLANK",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight(650),
                                fontSize = 32.sp // Sesuaikan ukuran yang lebih besar dari default
                            ),
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .width(275.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(Color(0xFFEBDEFF), Color.White),
                                    center = Offset.Unspecified, // Defaultnya di tengah
                                    radius = 500f // Sesuaikan agar menyebar dengan baik di ukuran box
                                )
                            )
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Color by",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight(650),
                                    fontSize = 32.sp // Sesuaikan ukuran yang lebih besar dari default
                                ),
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(4.dp)) // Jarak antar teks

                            Text(
                                text = "Number",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight(650),
                                    fontSize = 32.sp // Sesuaikan ukuran yang lebih besar dari default
                                ),
                                color = Color.Black
                            )
                        }
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

            BottomBarScribble()
        }
    }
}


@Composable
fun BottomBarScribble(modifier: Modifier = Modifier) {
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
            BottomNavItem(R.drawable.home_icon, "Home", iconSize = 28.dp)
            BottomNavItem(R.drawable.therapist_icon, "Therapist", iconSize = 25.dp)
            BottomNavItem(R.drawable.explore_icon, "Explore", iconSize = 25.dp)
            BottomNavItem(R.drawable.scribble_icon, "Scribble", iconSize = 43.dp)
            BottomNavItem(R.drawable.journal_icon, "Journal", iconSize = 25.dp)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Scribblereview() {
    // Gunakan dummy NavController untuk preview
    ScribbleScreen(navController = NavController(LocalContext.current))
}