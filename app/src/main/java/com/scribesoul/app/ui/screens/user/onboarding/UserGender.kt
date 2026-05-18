package com.scribesoul.app.ui.screens.user.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.scribesoul.R
import com.scribesoul.app.ui.components.OnboardingSelectableItem
import com.scribesoul.app.ui.components.OnboardingTemplate
import com.scribesoul.app.viewModels.TherapistOnboardingViewModel
import com.scribesoul.app.viewModels.UserOnboardingViewModel

@Composable
fun UserGenderScreen(
    navController: NavController,
    onboardingViewModel: UserOnboardingViewModel
) {
    // State untuk scroll agar konten tidak bertabrakan dengan tombol navigasi
    val scrollState = rememberScrollState()

    OnboardingTemplate(
        title = "What's your gender?",
        subtitle = "This question is designed to help us select\ncontent that suits your age.",
        backgroundColor = Color(0xFFEBDEFF), // Warna ungu sesuai desain
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            if (onboardingViewModel.gender != "") {
                navController.navigate("user_problems")
            }
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Baris untuk pilihan gender (Hanya 2 item)
            Row(
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                OnboardingSelectableItem(
                    text = "Woman",
                    isSelected = onboardingViewModel.gender == "Woman", // Ambil dari ViewModel
                    onClick = { onboardingViewModel.gender = "Woman" }, // Update ke ViewModel
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )

                OnboardingSelectableItem(
                    text = "Man",
                    isSelected = onboardingViewModel.gender == "Man", // Ambil dari ViewModel
                    onClick = { onboardingViewModel.gender = "Man" }, // Update ke ViewModel
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )
            }

            // Gambar maskot kucing di bawah pilihan
            Image(
                painter = painterResource(id = R.drawable.cat_transgender),
                contentDescription = "Gender Mascot",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(473.dp)
                    .offset(y = (-60).dp)
            )
            // Spacer tambahan di bawah agar gambar bisa di-scroll melewati tombol navigasi
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}