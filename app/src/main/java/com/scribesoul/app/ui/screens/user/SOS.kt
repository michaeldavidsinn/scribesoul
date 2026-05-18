package com.scribesoul.app.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.scribesoul.R


@Composable
fun SOSScreen(navController: NavController) {
    val gradientBorderBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFFFF47A), // Kuning
            Color(0xFFFFA8CF), // Pink
            Color(0xFFA774FF)  // Ungu
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFA8CF).copy(alpha = 0.3f),
                        Color.White
                    ),
                    radius = 1200f
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(140.dp))

            // Title
            Text(
                text = "Emergency\nhelp needed?",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontFamily = FontFamily(Font(R.font.verdana_bold)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B395B),
                    textAlign = TextAlign.Center
                )
            )

            Text(
                text = "hold the button to call",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.verdana)),
                    color = Color.Gray
                ),
                modifier = Modifier.padding(top = 8.dp, bottom = 40.dp)
            )

            // SOS Button
            Box(
                modifier = Modifier
                    .size(310.dp)
                    .clip(CircleShape)
                    .border(
                        width = 10.dp,
                        color = Color(0xFFECECEC),
                        shape = CircleShape
                    )
                    .background(Color(0xFFD10000))
                    .clickable { /* Handle SOS */ },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SOS",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.verdana_bold))
                    )
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Use this feature section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Use this feature if you:",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B395B)
                    ),
                    modifier = Modifier.padding(start = 40.dp, bottom = 12.dp)
                )

                // Horizontal Scroll for Cards
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    EmergencyReasonCard(
                        text = "Feel overwhelmed, panicked, or out of control",
                        gradient = gradientBorderBrush
                    )
                    EmergencyReasonCard(
                        text = "Experience anxiety attacks or panic attacks",
                        gradient = gradientBorderBrush
                    )
                    EmergencyReasonCard(
                        text = "Have intense negative thoughts and need immediate help",
                        gradient = gradientBorderBrush
                    )
                    EmergencyReasonCard(
                        text = "Need someone to contact as soon as possible",
                        gradient = gradientBorderBrush
                    )
                }
            }
        }

        // Back Button
        Box(
            modifier = Modifier
                .padding(top = 56.dp, start = 8.dp)
                .clip(RoundedCornerShape(50))
                .clickable { navController.popBackStack() }
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }

        // Bottom Bar
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomBarHome(navController)
        }
    }
}

@Composable
fun EmergencyReasonCard(text: String, gradient: Brush) {
    Box(
        modifier = Modifier
            .width(140.dp) // Sesuaikan lebar agar terlihat seperti di screenshot
            .height(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(brush = gradient)
            .padding(1.5.dp) // Ketebalan border gradient
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(15.dp)) // Sedikit lebih kecil dari parent agar rapi
                .background(Color.White)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B395B),
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp
                )
            )
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_9_pro_xl")
@Composable
fun SOSScreenPreview() {
    val context = LocalContext.current
    Surface(modifier = Modifier.fillMaxSize()) {
        SOSScreen(navController = NavController(context))
    }
}