package com.scribesoul.app.ui.screens.therapist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.scribesoul.app.viewModels.TherapistHomeViewModel

@Composable
fun TherapistHistoryScreen(
    navController: NavController,
    viewModel: TherapistHomeViewModel
) {

    // Load data saat layar dibuka
    LaunchedEffect(Unit) {
        viewModel.loadTherapistDashboard()
    }

    val gradientBrushs = Brush.horizontalGradient(
        colors = listOf(Color(0xFF74A8FF), Color(0xFF82D9D2))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF6F6F6), Color(0xFFFFFFFF), Color(0xFFF6F6F6))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- HEADER ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clip(RoundedCornerShape(50))
                        .clickable { navController.popBackStack() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "Therapy History",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight(650),
                        fontSize = 25.sp
                    ),
                    color = Color(0xFF2B395B)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- LIST KONTEN ---
            // LazyColumn langsung menjadi kontainer utama sisa ruang
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp) // Ruang lega di bawah
            ) {
                val sessions = viewModel.therapistSessions

                if (sessions.isEmpty()) {
                    item {
                        Text(
                            text = "No history found",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 50.dp),
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                } else {
                    items(sessions.size) { index ->
                        val session = sessions[index]

                        // Cari nama client (jika ada data client-nya)
                        val clientName = viewModel.therapistClients
                            .find { it.clientId == session.clientId }?.name
                            ?: "Client ${session.clientId.takeLast(4)}"

                        HistoryCardItem(
                            title = clientName,
                            subtitle1 = "${session.durationMinutes} minutes session",
                            subtitle2 = com.scribesoul.app.utils.DateTimeUtils.getFormattedDate(session.dateTimestamp),
                            gradientBrushs = gradientBrushs
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryCardItem(
    title: String,
    subtitle1: String,
    subtitle2: String,
    gradientBrushs: Brush
) {
    val gradientBorderBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFFFFF47A), Color(0xFFFFA8CF), Color(0xFFA774FF))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = gradientBorderBrush, shape = RoundedCornerShape(25))
            .padding(1.dp) // Ketebalan border gradasi
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25))
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = Color(0xFF2B395B)
                )
                Text(
                    text = subtitle1,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.Black
                )
                Text(
                    text = subtitle2,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.Black,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Arrow Icon",
                modifier = Modifier
                    .size(18.dp)
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