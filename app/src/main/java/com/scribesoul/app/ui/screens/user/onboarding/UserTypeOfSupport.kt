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
fun UserTypeOfSupportScreen(
    navController: NavController,
    onboardingViewModel: UserOnboardingViewModel
) {
    // State untuk scroll
    val scrollState = rememberScrollState()

    val supports = listOf(
        "Anonymous Chat",
        "Access to educational resources\n" +
                "about mental health",
        "Professional consultations with\n" +
                "therapists",
        "Creative coping methods like Art\n" +
                "Therapy",
        "Journaling and tracking my\n" +
                "emotions",
    )

    OnboardingTemplate(
        title = "What type of support you interest the most?",
        subtitle = "It’ll help us to understand you more\n" +
                "and help you to achieve your goals!",
        backgroundColor = Color(0xFFE0ECFF),
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            if(onboardingViewModel.supportTypes.isNotEmpty()){
                navController.navigate("user_challenges")
            }


        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState), // Menjadikan area ini scrollable
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                supports.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        pair.forEach { text ->
                            OnboardingSelectableItem(
                                text = text,
                                isSelected = onboardingViewModel.supportTypes.contains(text),
                                onClick = {
                                    if (onboardingViewModel.supportTypes.contains(text)) {
                                        onboardingViewModel.supportTypes.remove(text)
                                    } else {
                                        onboardingViewModel.supportTypes.add(text)
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                            )
                        }
                    }
                }

            }

            // Spacer bawah ditingkatkan agar item terakhir tidak tertutup tombol 'paten'
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}

