package com.scribesoul.app.ui.screens.therapist

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import com.scribesoul.app.viewModels.TherapistHomeViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TherapistHistoryScreen(
    navController: NavController,
    viewModel: TherapistHomeViewModel
) {

    LaunchedEffect(Unit) {
        viewModel.loadTherapistDashboard()
    }

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
                        Color(0xFFF6F6F6), // Putih
                        Color(0xFFFFFFFF), // Putih
                        Color(0xFFF6F6F6), // Putih
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
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clip(RoundedCornerShape(50))
                        .clickable { navController.popBackStack() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black, modifier = Modifier.size(20.dp))
                }

                Text(
                    text = "Therapy History",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight(650), fontSize = 25.sp),
                    color = Color(0xFF2B395B)
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


                    Spacer(modifier = Modifier.height(10.dp))

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

                        val chipItems = listOf(
                            Triple("Dr. Lisa", "3 hours session", "Date/ Time")
                        )

                        chipItems.forEach { (title, subtitle1, subtitle2) ->
                            Box(
                                modifier = Modifier
                                    .background(brush = gradientBrush, shape = RoundedCornerShape(25))
                                    .padding(1.dp)
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
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(12.dp),
                                        contentPadding = PaddingValues(vertical = 10.dp)
                                    ) {
                                        val sessions = viewModel.therapistSessions

                                        if (sessions.isEmpty()) {
                                            item {
                                                Text(
                                                    "No history found",
                                                    modifier = Modifier.fillMaxWidth().padding(top = 50.dp),
                                                    textAlign = TextAlign.Center,
                                                    color = Color.Gray
                                                )
                                            }
                                        } else {
                                            items(sessions.size) { index ->
                                                val session = sessions[index]

                                                // Cari nama client dari list client yang ada di ViewModel
                                                val clientName = viewModel.therapistClients
                                                    .find { it.clientId == session.clientId }?.name ?: "Client ${session.clientId.takeLast(4)}"

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
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFFFFF47A), Color(0xFFFFA8CF), Color(0xFFA774FF))
    )

    Box(
        modifier = Modifier
            .background(brush = gradientBrush, shape = RoundedCornerShape(25))
            .padding(1.dp)
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
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp, fontWeight = FontWeight.ExtraBold),
                    color = Color(0xFF2B395B)
                )
                Text(
                    text = subtitle1,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Normal),
                    color = Color.Black
                )
                Text(
                    text = subtitle2,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Normal),
                    color = Color.Black
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