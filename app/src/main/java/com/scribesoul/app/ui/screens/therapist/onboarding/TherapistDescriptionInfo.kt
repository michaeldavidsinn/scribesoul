package com.scribesoul.app.ui.screens.therapist.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.scribesoul.app.ui.components.OnboardingTemplate
import com.scribesoul.app.viewModels.TherapistOnboardingViewModel

@Composable
fun TherapistDescriptionInfo(
    navController: NavController,
    onboardingViewModel: TherapistOnboardingViewModel
) {

    val darkBlue = Color(0xFF2B395B)

    OnboardingTemplate(
        title = "Short Professional\nDescription",
        subtitle = "Tell clients about yourself",
        backgroundColor = Color(0xFFE0EEFF), // Gradasi biru sesuai Figma
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            // Logika simpan deskripsi
             navController.navigate("therapist_qualification")
        }
    ) {
        Column(
            modifier = Modifier.width(280.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Input Area Besar (TextArea)
            TextField(
                value = onboardingViewModel.description,
                onValueChange = { onboardingViewModel.description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp) // Ukuran tinggi kotak besar sesuai gambar
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(25.dp))
                    .clip(RoundedCornerShape(25.dp)),
                placeholder = {
                    Text(
                        text = "Fill here",
                        color = darkBlue.copy(alpha = 0.4f),
                        fontSize = 18.sp
                    )
                },
                // Garis vertikal di awal input
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 20.dp, bottom = 340.dp) // Posisi garis di pojok kiri atas
                            .width(2.dp)
                            .height(24.dp)
                            .background(darkBlue)
                    )
                },
                singleLine = false, // Memungkinkan banyak baris
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Footer Note (Teks di bawah kotak)
            Text(
                text = "\"Share a short introduction about your experience, therapy approach, and how you help clients.\"",
                fontSize = 12.sp,
                color = darkBlue.copy(alpha = 0.6f),
                textAlign = TextAlign.Start,
                lineHeight = 16.sp,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}