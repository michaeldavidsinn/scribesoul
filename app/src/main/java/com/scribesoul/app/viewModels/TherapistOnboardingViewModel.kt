package com.scribesoul.app.viewModels

import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.scribesoul.app.ScribeSoulApplication
import com.scribesoul.app.models.EducationDTO
import com.scribesoul.app.models.TherapistDTO
import com.scribesoul.app.repository.TherapistRepository
import com.scribesoul.app.ui.screens.therapist.onboarding.EducationEntry
import com.scribesoul.app.ui.screens.therapist.onboarding.WorkExperienceEntry
import kotlinx.coroutines.launch

class TherapistOnboardingViewModel(
    private val repository: TherapistRepository = TherapistRepository()
) : ViewModel() {
    // Page 1: Personal Info
    var fullName by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var dateOfBirth by mutableStateOf("")

    // Page 2: Gender
    var selectedGender by mutableStateOf<String?>(null)

    // Page 3: Professional Info
    var professionalTitle by mutableStateOf("")
    var yearsOfExperience by mutableStateOf("")
    var sessionFee by mutableStateOf("")

    // Page 4: Specialization
    val selectedSpecializations = mutableStateListOf<String>()
    var otherSelected by mutableStateOf(false)

    // Page 5: Description
    var description by mutableStateOf("")

    // Page 6: Qualification
    val educationList = mutableStateListOf(EducationEntry())

    // Page 7: Experience
    val experienceList = mutableStateListOf(WorkExperienceEntry())

    // Page 8: License
    var strNumber by mutableStateOf("")
    var capturedLicenseImage by mutableStateOf<Bitmap?>(null)

    // Page 9: Therapy Approaches
    val selectedApproaches = mutableStateListOf<String>()
    var otherApproachSelected by mutableStateOf(false)

    fun completeOnboarding(onSuccess: () -> Unit) {
        viewModelScope.launch {
            // 1. Mapping EducationEntry -> EducationDTO
            val educationDTOs = educationList.map {
                EducationDTO(
                    institution = it.university,
                    degree = it.degree,
                    year = it.graduationYear
                )
            }

            // 2. Mapping WorkExperienceEntry -> String (workPractices)
            val practices = experienceList.map {
                "${it.position} at ${it.clinicName} (${it.yearOfPractice})"
            }

            var imageUrl = ""
            capturedLicenseImage?.let {
                imageUrl = repository.uploadLicenseImage(it)
            }

            // 3. Rakit TherapistDTO
            val therapistDTO = TherapistDTO(
                name = fullName,
                title = professionalTitle,
                strNumber = strNumber,
                // Konversi String ke Numeric dengan aman
                pricePerSession = sessionFee.toDoubleOrNull() ?: 0.0,
                experienceYears = yearsOfExperience.toIntOrNull() ?: 0,
                biography = description,
                specializations = selectedSpecializations.toList(),
                therapyApproaches = selectedApproaches.toList(),
                educations = educationDTOs,
                workPractices = practices,
                languages = listOf("Indonesia", "English"),
                licenseImageUrl = imageUrl
            )

            // 4. Kirim ke Repository
            val result = repository.saveTherapistProfile(therapistDTO)

            if (result.isSuccess) {
                onSuccess()
            } else {
                // Kamu bisa tambah state errorMessage di sini untuk show toast/error
                println("Error saving profile: ${result.exceptionOrNull()?.message}")
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ScribeSoulApplication)
                TherapistOnboardingViewModel(application.container.therapistRepository)
            }
        }
    }
}
