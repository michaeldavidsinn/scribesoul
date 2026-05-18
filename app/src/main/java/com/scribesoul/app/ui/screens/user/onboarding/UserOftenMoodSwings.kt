package com.scribesoul.app.ui.screens.user.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.scribesoul.app.ui.components.OnboardingSelectableItem
import com.scribesoul.app.ui.components.OnboardingTemplate
import com.scribesoul.app.viewModels.TherapistOnboardingViewModel
import com.scribesoul.app.viewModels.UserOnboardingViewModel

@Composable
fun UserOftenMoodSwingsScreen(
    navController: NavController,
    onboardingViewModel: UserOnboardingViewModel
) {
    // State untuk scroll agar tidak menabrak tombol navigasi di bawah
    val scrollState = rememberScrollState()

    // Daftar spesialisasi sesuai desain Figma
    val oftenFeelings = listOf(
        "Rarely (Once a week or less)",
        "Occasionally (2-3 times a week)",
        "Frequently (4-5 times a week)",
        "Almost everyday",
        "It varies, i can’t predict it",
        )

    OnboardingTemplate(
        title = "How often do you experience mood swings a week?",
        subtitle = "This question is designed to help us select\n" +
                "content that suits your age.",
        backgroundColor = Color(0xFFFFFDE6), // Kuning muda sesuai Screenshot 15.22.54
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            if(onboardingViewModel.oftenMoodSwings != ""){
                navController.navigate("user_often_emotional_support")
            }

        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Kontainer Grid 2 Kolom
            Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                oftenFeelings.forEach { text ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OnboardingSelectableItem(
                            text = text,
                            isSelected = onboardingViewModel.oftenFeeling.contains(text),
                            onClick = {
                                onboardingViewModel.oftenFeeling = text
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                        )
                    }
                }

            }

            // Spacer besar di bawah agar item terakhir bisa di-scroll ke atas tombol navigasi
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}