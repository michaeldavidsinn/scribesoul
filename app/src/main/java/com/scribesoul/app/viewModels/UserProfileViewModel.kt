package com.scribesoul.app.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.scribesoul.app.models.UserDTO
import com.scribesoul.app.repository.UserRepository
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var userProfile by mutableStateOf<UserDTO?>(null)
        private set

    var userEmail by mutableStateOf<String>("")
        private set

    fun loadUserProfile() {
        viewModelScope.launch {
            userEmail = auth.currentUser?.email ?: "No email linked"

            val result = repository.getUserProfile()
            if (result.isSuccess) {
                userProfile = result.getOrNull()
            } else {
                // 1. Log the exact error to Logcat so you can read it!
                val error = result.exceptionOrNull()
                Log.e("UserProfileError", "Failed to load profile: ${error?.message}", error)

                // 2. Stop the infinite loading state in the UI by creating a fallback
                userProfile = UserDTO(
                    name = "Error loading name",
                    age = "Error loading age"
                )
            }
        }
    }

    fun checkIfUserFinishedOnboarding(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = repository.getUserProfile()
            val errorMsg = result.exceptionOrNull()?.message

            if (result.isSuccess) {
                onResult(true) // Document exists! They finished onboarding.
            } else if (errorMsg?.contains("does not exist") == true) {
                onResult(false) // Document missing! Send them to onboarding.
            } else {
                // If it fails for another reason (like no internet), default to true
                // so they don't get forced to re-do onboarding offline.
                onResult(true)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Get the application context to access the AppContainer
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as com.scribesoul.app.ScribeSoulApplication)
                val repository = application.container.userRepository


                UserProfileViewModel(repository = repository)
            }

        }
    }
}