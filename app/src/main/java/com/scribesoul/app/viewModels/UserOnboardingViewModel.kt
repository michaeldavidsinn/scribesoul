package com.scribesoul.app.viewModels

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.scribesoul.app.ScribeSoulApplication
import com.scribesoul.app.models.EducationDTO
import com.scribesoul.app.models.TherapistDTO
import com.scribesoul.app.models.UserDTO
import com.scribesoul.app.repository.AuthRepository
import com.scribesoul.app.repository.TherapistRepository
import com.scribesoul.app.repository.UserRepository
import com.scribesoul.app.ui.screens.therapist.onboarding.EducationEntry
import com.scribesoul.app.ui.screens.therapist.onboarding.WorkExperienceEntry
import kotlinx.coroutines.launch

class UserOnboardingViewModel(
    private val repository: UserRepository
) : ViewModel() {

//    val id: String = "",
//    val name: String = "",
//    val age: String = "",
//    val gender: String = "",
//    val problems: List<String> = emptyList(),
//    val goals: List<String> = emptyList(),
//    val feeling: String = "",
//    val startFeeling: String = "",
//    val oftenFeeling: String = "",
//    val oftenMoodSwings: String = "",
//    val oftenEmotionalSupport: String = "",
//    val supportTypes: List<String> = emptyList(),
//    val challenges: List<String> = emptyList(),
//    val motivations: List<String> = emptyList(),

//      val soughtMentalHealth: String = "",
//    val supportKind: String = "",
//    val seekingHelpDuration: String = "",
//    val connectionImportance: String = ""
    private val auth = FirebaseAuth.getInstance()

    // Page 1: Personal Info
    var age by mutableStateOf("")
    var gender by mutableStateOf("")
    val selectedProblems = mutableStateListOf<String>()
    val selectedGoals = mutableStateListOf<String>()
    var feeling by mutableStateOf("")
    var startFeeling by mutableStateOf("")
    var oftenFeeling by mutableStateOf("")
    var oftenMoodSwings by mutableStateOf("")
    var oftenEmotionalSupport by mutableStateOf("")
    val supportTypes = mutableStateListOf<String>()
    val challenges = mutableStateListOf<String>()
    val motivations = mutableStateListOf<String>()
    var soughtMentalHealth by mutableStateOf("")
    var supportKind by mutableStateOf("")
    var seekingHelpDuration by mutableStateOf("")
    var connectionImportance by mutableStateOf("")

    fun completeOnboarding(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val firebaseUser = auth.currentUser

            // 2. Safely get the username. Fallback to email prefix if displayName is missing.
            val currentUserName = firebaseUser?.displayName?.takeIf { it.isNotBlank() }
                ?: firebaseUser?.email?.substringBefore("@")?.replaceFirstChar { it.uppercase() }
                ?: "User"
            val userDTO = UserDTO(
                name = currentUserName,
                age = age,
                gender = gender,
                problems = selectedProblems.toList(),
                goals = selectedGoals.toList(),
                feeling = feeling,
                startFeeling = startFeeling,
                oftenFeeling = oftenFeeling,
                oftenMoodSwings = oftenMoodSwings,
                oftenEmotionalSupport = oftenEmotionalSupport,
                supportTypes = supportTypes.toList(),
                challenges = challenges.toList(),
                motivations = motivations.toList(),
                soughtMentalHealth = soughtMentalHealth,
                supportKind = supportKind,
                seekingHelpDuration = seekingHelpDuration,
                connectionImportance = connectionImportance
            )

            // 4. Kirim ke Repository
            val result = repository.saveUserProfile(userDTO)

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
                UserOnboardingViewModel(application.container.userRepository)
            }
        }
    }
}