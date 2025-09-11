package com.example.scribesoul.ui.screens

import com.example.scribesoul.ui.components.InputBar
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
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Search
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
import com.example.scribesoul.ui.components.ChatBubble


@Composable
fun CommunityGroupScreen(navController: NavController) {
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable { /* TODO: Back action */ }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Title
            Text(
                text = "Community Group",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight(650),
                    fontSize = 20.sp
                ),
                color = Color(0xFF2B395B),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 8.dp)
            )

            // Spacer biar icon kanan nempel ke ujung
            Spacer(modifier = Modifier.weight(1f))

            // Right action icons
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* TODO: Search action */ },
                    modifier = Modifier.size(36.dp) // lebih kecil dari default
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp) // biar proporsional
                    )
                }
                IconButton(
                    onClick = { /* TODO: Notes action */ },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notes,
                        contentDescription = "Notes",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = { /* TODO: More options action */ },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {

            ChatBubble(
                message = "Hi, I'm Clara. I've been working",
                sender = "anonymous 1",
                isMine = false,
                modifier = Modifier.padding(start = 12.dp, end = 48.dp) // margin kiri kecil, kanan lebih besar
            )

            ChatBubble(
                message = "Hi Clara! Same here 👋",
                sender = "Me",
                isMine = true,
                modifier = Modifier.padding(start = 48.dp, end = 12.dp) // margin kanan kecil, kiri lebih besar
            )


            Spacer(modifier = Modifier.height(15.dp))

            InputBar()
            BottomBarAnonymous(navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommunityGroupPreview() {
    // Gunakan dummy NavController untuk preview
    CommunityGroupScreen(navController = NavController(LocalContext.current))
}
