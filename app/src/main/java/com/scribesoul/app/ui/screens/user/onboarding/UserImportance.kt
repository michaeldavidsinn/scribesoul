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
fun UserImportanceScreen(
    navController: NavController,
    onboardingViewModel: UserOnboardingViewModel
) {
    // State untuk scroll agar tidak menabrak tombol navigasi di bawah
    val scrollState = rememberScrollState()

    // Daftar spesialisasi sesuai desain Figma
    val duration = listOf(
        "Very important",
        "I want to feel understood add\n" +
                "supported",
        "Somewhat important, but i also\n" +
                "like keeping to myself",
        "Not very important",
        "I prefer focusing on my own journey",
        "I can’t remember exactly when it\n" +
                "started"
        )

    OnboardingTemplate(
        title = "How important for you to connect with others who share similar experiences?",
        subtitle = "This question is designed to help us select\n" +
                "content that suits your age.",
        backgroundColor = Color(0xFFFFFDE6), // Kuning muda sesuai Screenshot 15.22.54
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            if(onboardingViewModel.connectionImportance != ""){
                onboardingViewModel.completeOnboarding {
                    navController.navigate("user_thank_you")
                }
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