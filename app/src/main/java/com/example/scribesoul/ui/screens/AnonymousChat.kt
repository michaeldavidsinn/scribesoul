package com.example.scribesoul.ui.screens

import com.example.scribesoul.ui.theme.Typography
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
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.example.scribesoul.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

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
                    Text(
                        text = "SoulFess",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight(650)),
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Share your story anonymously",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 20.dp)
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
                                .height(12.dp) // Sesuaikan tinggi gambar
                        )
                    }
                }
            }
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
                    .size(48.dp)
                    .background(color = Color(0xFFBFD7FF), shape = CircleShape),
            )

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

@Preview(showBackground = true)
@Composable
fun AnonymousChatPreview() {
    // Gunakan dummy NavController untuk preview
    AnonymousChatScreen(navController = NavController(LocalContext.current))
}
