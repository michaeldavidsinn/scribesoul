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
fun UserSeekingHelpDurationScreen(
    navController: NavController,
    onboardingViewModel: UserOnboardingViewModel
) {
    // State untuk scroll agar tidak menabrak tombol navigasi di bawah
    val scrollState = rememberScrollState()

    // Daftar spesialisasi sesuai desain Figma
    val duration = listOf(
        "Less than 1 month",
        "1-6 months",
        "6 month - 1 year",
        "1-3 years",
        "Over 3 years",
        "I haven’t sought professional help\n" +
                "yet"
        )

    OnboardingTemplate(
        title = "How long have you been seeking professional help?",
        subtitle = "This question is designed to help us select\n" +
                "content that suits your age.",
        backgroundColor = Color(0xFFFFFDE6), // Kuning muda sesuai Screenshot 15.22.54
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            if(onboardingViewModel.seekingHelpDuration != ""){
                navController.navigate("user_importance")
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
                duration.forEach { text ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OnboardingSelectableItem(
                            text = text,
                            isSelected = onboardingViewModel.seekingHelpDuration.contains(text),
                            onClick = {
                                onboardingViewModel.seekingHelpDuration = text
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