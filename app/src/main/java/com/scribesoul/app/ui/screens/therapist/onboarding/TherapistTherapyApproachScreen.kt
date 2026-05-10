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

@Composable
fun TherapistTherapyApproachScreen(navController: NavController) {
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

    val selectedApproaches = remember { mutableStateListOf<String>() }
    var otherSelected by remember { mutableStateOf(false) }

    OnboardingTemplate(
        title = "Therapy\nApproaches",
        subtitle = "Select theraphy methods you use",
        backgroundColor = Color(0xFFE0ECFF),
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            navController.navigate("home_therapist") {
                // Opsional: Hapus stack onboarding agar user tidak bisa kembali ke pendaftaran dengan tombol back
                popUpTo("therapist_personal_info") { inclusive = true }
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
                                isSelected = selectedApproaches.contains(text),
                                onClick = {
                                    if (selectedApproaches.contains(text)) {
                                        selectedApproaches.remove(text)
                                    } else {
                                        selectedApproaches.add(text)
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
                    isSelected = otherSelected,
                    onClick = { otherSelected = !otherSelected },
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

@Preview(showBackground = true)
@Composable
fun PreviewTherapyApproach() {
    TherapistTherapyApproachScreen(navController = NavController(LocalContext.current))
}