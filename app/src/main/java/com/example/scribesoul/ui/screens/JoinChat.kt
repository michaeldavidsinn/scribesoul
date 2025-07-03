package com.example.scribesoul.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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


@Composable
fun JoinChatScreen(navController: NavController) {
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

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Wanna join our",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight(650),
                            fontSize = 32.sp
                        ),
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(2.dp)) // Jarak antar baris, sesuaikan sesuai keinginan

                    Text(
                        text = "community?",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight(650),
                            fontSize = 32.sp
                        ),
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f) // Panjang kotak 50% dari layar
                            .height(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1EB980)) // Warna biru tua
                            .align(Alignment.CenterHorizontally), // Posisi tengah horizontal
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Yes",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f) // Panjang kotak 50% dari layar
                            .height(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF44336)) // Warna biru tua
                            .align(Alignment.CenterHorizontally), // Posisi tengah horizontal
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
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
            BottomBarAnonymous()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JoinChatPreview() {
    // Gunakan dummy NavController untuk preview
    JoinChatScreen(navController = NavController(LocalContext.current))
}
