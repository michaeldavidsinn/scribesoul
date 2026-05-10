package com.scribesoul.app.ui.screens.therapist.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.scribesoul.app.ui.components.OnboardingTemplate
import com.scribesoul.app.ui.components.OnboardingTextField

@Composable
fun TherapistProfessionalInfo(navController: NavController) {
    // State untuk menyimpan input
    var professionalTitle by remember { mutableStateOf("") }
    var yearsOfExperience by remember { mutableStateOf("") }
    var sessionFee by remember { mutableStateOf("") }

    val darkBlue = Color(0xFF2B395B)

    // Menggunakan Template Onboarding dengan background PINK
    OnboardingTemplate(
        title = "Professional\nInformation",
        backgroundColor = Color(0xFFFFE6ED), // Warna pink sesuai Figma
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            // Navigasi ke page berikutnya (misal: Workplace Info)
             navController.navigate("therapist_specialization")
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            // 1. Professional Title Input
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OnboardingTextField(
                    value = professionalTitle,
                    onValueChange = { professionalTitle = it },
                    placeholder = "Professional Title"
                )
                Text(
                    text = "Ex: Clinical Psychologist / Therapist / Counselor",
                    fontSize = 13.sp,
                    color = darkBlue.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 8.dp, bottom = 10.dp),
                    textAlign = TextAlign.Center
                )
            }

            // 2. Years of Experience Input
            OnboardingTextField(
                value = yearsOfExperience,
                onValueChange = { yearsOfExperience = it },
                placeholder = "Years of Experience"
            )

            // 3. Session Fee Input
            OnboardingTextField(
                value = sessionFee,
                onValueChange = { sessionFee = it },
                placeholder = "Session Fee (per session)"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTherapistProfessionalInfo() {
    TherapistProfessionalInfo(navController = NavController(LocalContext.current))
}