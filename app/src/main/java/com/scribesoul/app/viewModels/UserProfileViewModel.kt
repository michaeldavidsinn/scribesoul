package com.scribesoul.app.viewModels

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
            // Fetch email from Firebase Auth
            userEmail = auth.currentUser?.email ?: "No email linked"

            // Fetch User Details (Name, Age, etc.) from Firestore
            val result = repository.getUserProfile()
            if (result.isSuccess) {
                userProfile = result.getOrNull()
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