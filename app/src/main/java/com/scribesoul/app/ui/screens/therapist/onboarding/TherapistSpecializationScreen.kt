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
import com.scribesoul.app.ui.components.OnboardingSelectableItem
import com.scribesoul.app.ui.components.OnboardingTemplate
import com.scribesoul.app.viewModels.TherapistOnboardingViewModel

@Composable
fun TherapistSpecializationScreen(
    navController: NavController,
    onboardingViewModel: TherapistOnboardingViewModel
) {
    // State untuk scroll agar tidak menabrak tombol navigasi di bawah
    val scrollState = rememberScrollState()

    // Daftar spesialisasi sesuai desain Figma
    val specializations = listOf(
        "Anxiety Disorder",
        "Stress Management",
        "PTSD",
        "Burnout",
        "Depression",
        "Relationship Issues",
        "Self Esteem Issues",
        "Trauma Recovery"
    )

    OnboardingTemplate(
        title = "Specialization",
        subtitle = "Select all that apply",
        backgroundColor = Color(0xFFFFFDE6), // Kuning muda sesuai Screenshot 15.22.54
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            navController.navigate("therapist_description")
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
                specializations.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        pair.forEach { text ->
                            OnboardingSelectableItem(
                                text = text,
                                // Cek ke list yang ada di ViewModel
                                isSelected = onboardingViewModel.selectedSpecializations.contains(text),
                                onClick = {
                                    if (onboardingViewModel.selectedSpecializations.contains(text)) {
                                        onboardingViewModel.selectedSpecializations.remove(text)
                                    } else {
                                        onboardingViewModel.selectedSpecializations.add(text)
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
                    isSelected = onboardingViewModel.otherSelected,
                    onClick = {
                        onboardingViewModel.otherSelected = !onboardingViewModel.otherSelected
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                )
            }

            // Spacer besar di bawah agar item terakhir bisa di-scroll ke atas tombol navigasi
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}