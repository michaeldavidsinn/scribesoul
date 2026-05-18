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
import com.scribesoul.app.viewModels.UserOnboardingViewModel

@Composable
fun UserFeelingScreen(
    navController: NavController,
    onboardingViewModel: UserOnboardingViewModel
) {
    // State untuk scroll agar tidak menabrak tombol navigasi di bawah
    val scrollState = rememberScrollState()

    // Daftar spesialisasi sesuai desain Figma
    val feelings = listOf(
        "I’ve been feeling happy and content",
        "I’ve been feeling okay but could be\n" +
                "better",
        "I’ve been feeling stressed or\n" +
                "overwhelmed",
        "I’ve been feeling sad or low most of\n" +
                "the time",
        "I’ve been feeling emotionally numb\n" +
                "or disconnected",
    )

    OnboardingTemplate(
        title = "How were you feeling lately?",
        subtitle = "This question is designed to help us select\n" +
                "content that suits your age.",
        backgroundColor = Color(0xFFFFFDE6), // Kuning muda sesuai Screenshot 15.22.54
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            if(onboardingViewModel.feeling != ""){
                navController.navigate("user_start_feeling")
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
                feelings.forEach { text ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OnboardingSelectableItem(
                            text = text,
                            isSelected = onboardingViewModel.feeling.contains(text),
                            onClick = {
                                onboardingViewModel.feeling = text
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