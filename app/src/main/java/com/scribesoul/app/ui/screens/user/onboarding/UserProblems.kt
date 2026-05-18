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
fun UserProblemsScreen(
    navController: NavController,
    onboardingViewModel: UserOnboardingViewModel
) {
    // State untuk scroll
    val scrollState = rememberScrollState()

    val problems = listOf(
        "Anxiety",
        "No Motivation",
        "Insomnia",
        "Insecurity",
        "Depression",
        "Panic Attack",
        "Overthinking",
        "Loss",
        "Toxic Relationship",
        "Toxic Friendship",
        "Stress",
        "Work Stress",
        "Exam Stress",
        "Trauma",
        "Low Energy",
        "Hard to Focus",
        "Self Esteem",
        "Health Issues",
        "LGBTQ+",
        "Eating Disorder"
    )

    OnboardingTemplate(
        title = "What kind of problem you dealt with recently?",
        subtitle = "Add your personal challenges that bother\n" +
                "you daily! You can pick more than one.",
        backgroundColor = Color(0xFFE0ECFF),
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            if(onboardingViewModel.selectedProblems.isNotEmpty()){
                navController.navigate("user_goals")
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
                problems.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        pair.forEach { text ->
                            OnboardingSelectableItem(
                                text = text,
                                isSelected = onboardingViewModel.selectedProblems.contains(text),
                                onClick = {
                                    if (onboardingViewModel.selectedProblems.contains(text)) {
                                        onboardingViewModel.selectedProblems.remove(text)
                                    } else {
                                        onboardingViewModel.selectedProblems.add(text)
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

