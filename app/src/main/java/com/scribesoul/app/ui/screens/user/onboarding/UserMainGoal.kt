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
fun UserMainGoalScreen(
    navController: NavController,
    onboardingViewModel: UserOnboardingViewModel
) {
    // State untuk scroll
    val scrollState = rememberScrollState()

    val goals = listOf(
        "Reduce Stress and Anxiety",
        "Heal Childhood Trauma",
        "Improve Sleep Quality",
        "Enhance Productivity",
        "Build Self Confidence",
        "Develop Emotional Resilience",
        "Strengthen Relationship",
        "Overcome Negative Thought Patterns",
        "Manage Anger",
        "Increase Energy and Motivation",
        "Foster Mindfulness",
        "Achieve Work-Life Balance",
        "Cultivate Happiness",
        "Recover from Eating Disorders",
        "Break Addictive Behaviours",
        "Regain a Sense of Purpose"
    )

    OnboardingTemplate(
        title = "What are your\rmain goal?",
        subtitle = "It’ll help us to understand you more\n" +
                "and help you to achieve your goals!",
        backgroundColor = Color(0xFFE0ECFF),
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            if(onboardingViewModel.selectedGoals.isNotEmpty()){
                navController.navigate("user_feeling")
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
                goals.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        pair.forEach { text ->
                            OnboardingSelectableItem(
                                text = text,
                                isSelected = onboardingViewModel.selectedGoals.contains(text),
                                onClick = {
                                    if (onboardingViewModel.selectedGoals.contains(text)) {
                                        onboardingViewModel.selectedGoals.remove(text)
                                    } else {
                                        onboardingViewModel.selectedGoals.add(text)
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

