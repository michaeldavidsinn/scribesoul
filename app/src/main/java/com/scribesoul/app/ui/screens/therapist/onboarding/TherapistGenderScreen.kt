package com.scribesoul.app.ui.screens.therapist.onboarding

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.scribesoul.R
import com.scribesoul.app.ui.components.OnboardingSelectableItem
import com.scribesoul.app.ui.components.OnboardingTemplate

@Composable
fun TherapistGenderScreen(navController: NavController) {
    // State untuk scroll agar konten tidak bertabrakan dengan tombol navigasi
    val scrollState = rememberScrollState()

    // State untuk menyimpan pilihan gender (Single Select)
    var selectedGender by remember { mutableStateOf<String?>(null) }

    OnboardingTemplate(
        title = "What's your gender?",
        subtitle = "This question is designed to help us select\ncontent that suits your age.",
        backgroundColor = Color(0xFFEBDEFF), // Warna ungu sesuai desain
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            // Navigasi ke halaman berikutnya jika gender sudah dipilih
            if (selectedGender != null) {
                 navController.navigate("therapist_professional_info")
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
                // Pilihan Wanita
                OnboardingSelectableItem(
                    text = "Woman",
                    isSelected = selectedGender == "Woman",
                    onClick = { selectedGender = "Woman" }, // Logika single select
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )

                // Pilihan Pria
                OnboardingSelectableItem(
                    text = "Man",
                    isSelected = selectedGender == "Man",
                    onClick = { selectedGender = "Man" }, // Logika single select
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

@Preview(showBackground = true)
@Composable
fun PreviewTherapistGender() {
    TherapistGenderScreen(navController = NavController(LocalContext.current))
}