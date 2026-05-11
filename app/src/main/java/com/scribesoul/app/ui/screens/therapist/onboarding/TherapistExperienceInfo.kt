package com.scribesoul.app.ui.screens.therapist.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.scribesoul.app.ui.components.OnboardingTemplate
import com.scribesoul.app.ui.components.OnboardingTextField
import com.scribesoul.app.viewModels.TherapistOnboardingViewModel

// 1. Data model untuk riwayat pekerjaan
data class WorkExperienceEntry(
    val clinicName: String = "",
    val position: String = "",
    val yearOfPractice: String = ""
)

@Composable
fun TherapistExperienceInfo(
    navController: NavController,
    onboardingViewModel: TherapistOnboardingViewModel
) {

    val darkBlue = Color(0xFF2B395B)
    val lightBlueTitle = Color(0xFF74A8FF)
    val scrollState = rememberScrollState()

    OnboardingTemplate(
        title = "Professional\nExperience",
        subtitle = "Current Workplace",
        backgroundColor = Color(0xFFFFE6ED), // Warna pink sesuai Figma
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            // Logika submit data experienceList
             navController.navigate("therapist_license")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            onboardingViewModel.experienceList.forEachIndexed { index, experience ->

                if (index > 0) {
                    Spacer(modifier = Modifier.height(30.dp))
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    OnboardingTextField(
                        value = experience.clinicName,
                        onValueChange = { newValue ->
                            // Update langsung ke list di ViewModel menggunakan .copy()
                            onboardingViewModel.experienceList[index] =
                                onboardingViewModel.experienceList[index].copy(clinicName = newValue)
                        },
                        placeholder = "Name of Clinic / Institution"
                    )

                    OnboardingTextField(
                        value = experience.position,
                        onValueChange = { newValue ->
                            onboardingViewModel.experienceList[index] =
                                onboardingViewModel.experienceList[index].copy(position = newValue)
                        },
                        placeholder = "Position"
                    )

                    OnboardingTextField(
                        value = experience.yearOfPractice,
                        onValueChange = { newValue ->
                            onboardingViewModel.experienceList[index] =
                                onboardingViewModel.experienceList[index].copy(yearOfPractice = newValue)
                        },
                        placeholder = "Year of Practice"
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 4. Tombol Add Work Experience (Putih, Shadow, Bulat)
            Box(
                modifier = Modifier
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(50))
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .clickable {
                        onboardingViewModel.experienceList.add(WorkExperienceEntry())
                    }
                    .padding(horizontal = 24.dp, vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add Work Experience",
                    color = darkBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Spacer bawah agar konten tidak tertutup tombol navigasi template
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}