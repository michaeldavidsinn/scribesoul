package com.scribesoul.app.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun WelcomeRoleScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF4F9FF), Color(0xFFE4F0FF))
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // --- HEADER ---
        Text(
            text = "WELCOME!",
            fontSize = 42.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF2B395B)
        )
        Text(
            text = "How would you like to enter?",
            fontSize = 18.sp,
            color = Color(0xFF2B395B),
            modifier = Modifier.padding(top = 4.dp, bottom = 40.dp)
        )

        // --- BUTTON: USER ---
        Box(
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 10.dp)
                .fillMaxWidth()
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(50))
                .clip(RoundedCornerShape(50))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF82D9D2), Color(0xFF7CC3E6), Color(0xFF74A8FF))
                    )
                )
                .clickable {
                    // Arahkan ke Onboarding User
                    navController.navigate("user_LetUsKnow") {
                        popUpTo("welcome_role") { inclusive = true }
                    }
                }
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Continue as User",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Share your thoughts, explore your emotions,\nand find the support you need.",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 12.sp,
                    modifier = Modifier.padding(start = 20.dp, top = 4.dp, end = 20.dp)
                )
            }
        }

        // --- BUTTON: THERAPIST ---
        Box(
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 10.dp)
                .fillMaxWidth()
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(50))
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFF7FFF9)) // Warna putih-hijau sangat pucat sesuai desain
                .clickable {
                    // Arahkan ke Onboarding Therapist
                    navController.navigate("therapist_personal_info") {
                        popUpTo("welcome_role") { inclusive = true }
                    }
                }
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Continue as Therapist",
                color = Color(0xFF5A6070), // Warna abu-abu gelap
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_9_pro_xl")
@Composable
fun PreviewWelcomeRoleScreen() {
    WelcomeRoleScreen(navController = rememberNavController())
}