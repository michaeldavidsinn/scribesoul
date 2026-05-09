package com.scribesoul.app.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.scribesoul.app.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var username by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Automatically checks if a user is already logged in when the app opens
    var isLoggedIn by mutableStateOf(authRepository.currentUser != null)
        private set

    fun reset(){
        email =""
        password =""
        confirmPassword = ""
        username = ""
    }

    fun login() {
        if (email.isBlank() || password.isBlank()) return

        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Please fill out all fields."
            return
        }


        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                isLoggedIn = true
            } else {
                errorMessage = result.exceptionOrNull()?.message
            }
            isLoading = false
        }
    }

    fun signUp() {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            errorMessage = "Please fill out all fields."
            return
        }

        // Check if passwords match
        if (password != confirmPassword) {
            errorMessage = "Passwords do not match!"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = authRepository.signUp(email, password, username)
            if (result.isSuccess) {
                isLoggedIn = true
            } else {
                errorMessage = result.exceptionOrNull()?.message
            }
            isLoading = false
        }
    }

    fun logout() {
        authRepository.logout()
        isLoggedIn = false
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as com.scribesoul.app.ScribeSoulApplication)
                AuthViewModel(application.container.authRepository)
            }
        }
    }
}