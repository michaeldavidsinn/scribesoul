package com.scribesoul.app.ui.screens.therapist.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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

data class EducationEntry(
    val degree: String = "",
    val university: String = "",
    val graduationYear: String = ""
)

@Composable
fun TherapistQualificationInfo(navController: NavController) {

    val educationList = remember { mutableStateListOf(EducationEntry()) }

    val darkBlue = Color(0xFF2B395B)
    val lightBlueTitle = Color(0xFF74A8FF)
    val scrollState = rememberScrollState()

    OnboardingTemplate(
        title = "Qualification &\nEducation",
        subtitle = "Highest Education",
        backgroundColor = Color(0xFFEBDEFF),
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            navController.navigate("therapist_experience")
        }
    ) {
        // 3. Tambahkan verticalScroll agar konten bisa di-scroll saat kolom bertambah
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 4. Loop melalui list untuk merender setiap blok input
            educationList.forEachIndexed { index, education ->

                // Header opsional jika ada lebih dari 1 pendidikan
                if (index > 0) {
                    Text(
                        text = "",
                        fontSize = 14.sp,
                        color = lightBlueTitle,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 20.dp, bottom = 12.dp)
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    OnboardingTextField(
                        value = education.degree,
                        onValueChange = { newValue ->
                            // Update list pada index tertentu
                            educationList[index] = educationList[index].copy(degree = newValue)
                        },
                        placeholder = "Degree"
                    )

                    OnboardingTextField(
                        value = education.university,
                        onValueChange = { newValue ->
                            educationList[index] = educationList[index].copy(university = newValue)
                        },
                        placeholder = "University"
                    )

                    OnboardingTextField(
                        value = education.graduationYear,
                        onValueChange = { newValue ->
                            educationList[index] = educationList[index].copy(graduationYear = newValue)
                        },
                        placeholder = "Year of Graduation"
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 5. Logika klik: Tambah objek EducationEntry baru ke dalam list
            Box(
                modifier = Modifier
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(50))
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .clickable {
                        educationList.add(EducationEntry())
                    }
                    .padding(horizontal = 24.dp, vertical = 18.dp), // Ditinggikan sedikit sesuai request sebelumnya
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add Another Education",
                    color = darkBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Spacer tambahan agar tidak menempel ke tombol NEXT di bawah
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTherapistQualificationInfo() {
    TherapistQualificationInfo(navController = NavController(LocalContext.current))
}