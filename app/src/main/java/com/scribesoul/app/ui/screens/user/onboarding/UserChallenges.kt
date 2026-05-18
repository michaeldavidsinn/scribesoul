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
fun UserChallengesScreen(
    navController: NavController,
    onboardingViewModel: UserOnboardingViewModel
) {
    // State untuk scroll
    val scrollState = rememberScrollState()

    val challenges = listOf(
        "Stigma or fear being judged",
        "Difficulty finding affordable or\n" +
                "accessible resources",
        "Not knowing where to start or who to talk to",
        "Feeling isolated or misunderstood",
    )

    OnboardingTemplate(
        title = "What are the biggest challenges you face when it comes to mental health?",
        subtitle = "It’ll help us to understand you more\n" +
                "and help you to achieve your goals!",
        backgroundColor = Color(0xFFE0ECFF),
        onBackClick = { navController.popBackStack() },
        onNextClick = {
                if(onboardingViewModel.challenges.isNotEmpty()){
                    navController.navigate("user_motivation")
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
                challenges.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        pair.forEach { text ->
                            OnboardingSelectableItem(
                                text = text,
                                isSelected = onboardingViewModel.challenges.contains(text),
                                onClick = {
                                    if (onboardingViewModel.challenges.contains(text)) {
                                        onboardingViewModel.challenges.remove(text)
                                    } else {
                                        onboardingViewModel.challenges.add(text)
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



