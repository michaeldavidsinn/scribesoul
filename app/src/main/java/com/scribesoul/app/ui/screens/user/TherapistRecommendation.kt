package com.scribesoul.app.ui.screens.user

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.scribesoul.R
import com.scribesoul.app.ui.navigation.BottomNavItem
import com.scribesoul.app.viewModels.TherapistDirectoryViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TherapistRecommendationScreen(
    navController: NavController,
    // Deklarasikan ViewModel di parameter agar bisa diakses
    viewModel: TherapistDirectoryViewModel = viewModel(factory = TherapistDirectoryViewModel.Factory)
) {
    val context = LocalContext.current

    // Menarik list Therapist asli dari Firebase
    val therapistList by viewModel.therapists.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFE0ECFF), // biru muda
                        Color(0xFFE1F9DF)  // hijau muda
                    ),
                    radius = 1000f
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 38.dp)
                .padding(horizontal = 28.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Recommendation",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight(650),
                            fontSize = 32.sp
                        ),
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Jika data masih kosong atau loading
            if (therapistList.isEmpty()) {
                item {
                    Text(
                        text = "No therapists available at the moment.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            } else {
                // Mapping objek therapist dari ViewModel
                itemsIndexed(therapistList) { index, therapist ->

                    val imageRes = when (index % 8) {
                        0 -> R.drawable.ther_1_f
                        1 -> R.drawable.ther_1_m
                        2 -> R.drawable.ther_2_f
                        3 -> R.drawable.ther_2_m
                        4 -> R.drawable.ther_3_f
                        5 -> R.drawable.ther_3_m
                        6 -> R.drawable.ther_4_f
                        7 -> R.drawable.ther_4_m
                        else -> R.drawable.ther_4_m
                    }

                    // Format Harga ke format Rupiah
                    val formattedPrice = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
                        maximumFractionDigits = 0
                    }.format(therapist.pricePerSession)

                    // Gabungkan spesialisasi array jadi satu string
                    val issueText = therapist.specializations.joinToString(", ")
                        .takeIf { it.isNotEmpty() } ?: "General Counseling"

                    TherapistCard(
                        name = therapist.name,
                        specialization = therapist.title,
                        issue = issueText,
                        experienceYears = therapist.experienceYears,
                        compatibility = (90..100).random(),
                        price = formattedPrice,
                        imageRes = imageRes,
                        onInfoClick = {
                            // Navigasi dengan ID Therapist
                            navController.navigate("therapist_detail/${therapist.id}")
                        },
                        onChatClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/account/subscriptions"))
                            context.startActivity(intent)
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp)) // Bottom padding
            }
        }

        // Bottom bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
        ) {
            BottomBarTherapist(navController = navController)
        }
    }
}

@Composable
fun TherapistCard(
    name: String,
    specialization: String,
    issue: String,
    experienceYears: Int,
    compatibility: Int,
    price: String,
    imageRes: Int,
    onInfoClick: () -> Unit,
    onChatClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onInfoClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar box
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .width(80.dp)
                    .shadow(6.dp, shape = RoundedCornerShape(12.dp), clip = false)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Therapist Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                        color = Color(0xFF2B395B)
                    )
                    Text(
                        text = specialization,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = Color(0xFF2B395B)
                    )
                    Text(
                        text = issue,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = Color(0xFF2B395B)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Baris Experience + Compatibility
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
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
                            .weight(1f)
                            .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                            .padding(1.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(50))
                                .background(Color.White)
                                .padding(horizontal = 2.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$experienceYears Years Experience",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.5.sp),
                                color = Color(0xFF2B395B),
                                maxLines = 1,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Compatibility
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                            .padding(1.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(50))
                                .background(Color.White)
                                .padding(horizontal = 6.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$compatibility% Match",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.5.sp),
                                color = Color(0xFF2B395B),
                                maxLines = 1,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Baris Price + Chat
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = price,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold, fontSize = 12.sp),
                        color = Color(0xFF2B395B)
                    )

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
                            .clickable { onChatClick() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Chat",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBarTherapist(navController: NavController, modifier: Modifier = Modifier) {
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
            BottomNavItem(R.drawable.home_icon, "Home", 28.dp) {
                navController.navigate("home") {
                    launchSingleTop = true
                }
            }
            BottomNavItem(R.drawable.therapist_icon_clicked, "Therapist", 40.dp) {
                navController.navigate("therapist") {
                    launchSingleTop = true
                }
            }
            BottomNavItem(R.drawable.explore_icon, "Explore", 25.dp) {
                navController.navigate("explore") {
                    launchSingleTop = true
                }
            }
            BottomNavItem(R.drawable.scribble_icon, "Scribble", 28.dp) {
                navController.navigate("addScribble") {
                    launchSingleTop = true
                }
            }
            BottomNavItem(R.drawable.journal_icon, "Journal", 25.dp) {
                navController.navigate("journalList") {
                    launchSingleTop = true
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TherapistRecommendationPreview() {
    val navController = rememberNavController()
    Surface(modifier = Modifier.fillMaxSize()) {
        // Karena ini Preview, kita hanya menginisialisasi secara sederhana
        TherapistRecommendationScreen(navController)
    }
}