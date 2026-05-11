package com.scribesoul.app.ui.screens.therapist.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.scribesoul.app.ui.components.OnboardingSelectableItem
import com.scribesoul.app.ui.components.OnboardingTemplate
import com.scribesoul.app.viewModels.TherapistOnboardingViewModel

@Composable
fun TherapistTherapyApproachScreen(
    navController: NavController,
    onboardingViewModel: TherapistOnboardingViewModel
) {
    // State untuk scroll
    val scrollState = rememberScrollState()

    val approaches = listOf(
        "Cognitive Behavioral Therapy (CBT)",
        "Mindfulness Based Therapy",
        "Person Centered Therapy",
        "Trauma Focused Therapy",
        "Solution Focused Therapy",
        "Psychodynamic Therapy"
    )

    OnboardingTemplate(
        title = "Therapy\nApproaches",
        subtitle = "Select theraphy methods you use",
        backgroundColor = Color(0xFFE0ECFF),
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            onboardingViewModel.completeOnboarding {
                navController.navigate("home_therapist") {

                    popUpTo("therapist_personal_info") { inclusive = true }
                }
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
                approaches.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        pair.forEach { text ->
                            OnboardingSelectableItem(
                                text = text,
                                isSelected = onboardingViewModel.selectedApproaches.contains(text),
                                onClick = {
                                    if (onboardingViewModel.selectedApproaches.contains(text)) {
                                        onboardingViewModel.selectedApproaches.remove(text)
                                    } else {
                                        onboardingViewModel.selectedApproaches.add(text)
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                            )
                        }
                    }
                }

                OnboardingSelectableItem(
                    text = "Other",
                    isSelected = onboardingViewModel.otherApproachSelected,
                    onClick = {
                        onboardingViewModel.otherApproachSelected = !onboardingViewModel.otherApproachSelected
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                )
            }

            // Spacer bawah ditingkatkan agar item terakhir tidak tertutup tombol 'paten'
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}