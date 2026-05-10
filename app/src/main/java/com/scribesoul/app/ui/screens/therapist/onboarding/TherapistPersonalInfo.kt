package com.scribesoul.app.ui.screens.therapist.onboarding
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.scribesoul.app.ui.components.OnboardingTemplate
import com.scribesoul.app.ui.components.OnboardingTextField

@Composable
fun TherapistPersonalInfo(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }

    OnboardingTemplate(
        title = "Personal\nInformation",
        backgroundColor = Color(0xFFBCD5FF),
        onBackClick = { navController.popBackStack() },
        onNextClick = { navController.navigate("therapist_gender") }
    ) {
        // Semua yang ada di sini akan muncul di bagian 'content' template
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OnboardingTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = "Full Name"
                )
                Text(
                    text = "Ex: Dr. Lisa Hermawan, S.Psi., M.Psi., Psikolog",
                    fontSize = 12.sp,
                    color = Color(0xFF2B395B).copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            OnboardingTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholder = "Phone Numbers"
            )

            OnboardingTextField(
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                placeholder = "Date of Birth"
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewTherapistPersonalInfo() {
    TherapistPersonalInfo(navController = NavController(LocalContext.current))
}