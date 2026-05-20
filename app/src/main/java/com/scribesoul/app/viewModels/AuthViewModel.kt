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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var username by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set

    // Field-specific error states
    var emailError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set
    var confirmPasswordError by mutableStateOf<String?>(null)
        private set
    var usernameError by mutableStateOf<String?>(null)
        private set

    // Global error for backend messages (like "Invalid credentials")
    var generalError by mutableStateOf<String?>(null)
        private set

    var isLoggedIn by mutableStateOf(authRepository.currentUser != null)
        private set

    fun reset() {
        email = ""
        password = ""
        confirmPassword = ""
        username = ""
        resetErrors()
    }

    private fun resetErrors() {
        emailError = null
        passwordError = null
        confirmPasswordError = null
        usernameError = null
        generalError = null
    }

    fun login() {
        resetErrors()
        var hasError = false

        if (email.isBlank()) {
            emailError = "Email cannot be empty"
            hasError = true
        }
        if (password.isBlank()) {
            passwordError = "Password cannot be empty"
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            isLoading = true
            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                isLoggedIn = true
                reset()
            } else {
                generalError = result.exceptionOrNull()?.message
            }
            isLoading = false
        }
    }

    fun signUp() {
        resetErrors()
        var hasError = false

        if (username.isBlank()) {
            usernameError = "Username cannot be empty"
            hasError = true
        }

        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        if (email.isBlank()) {
            emailError = "Email cannot be empty"
            hasError = true
        } else if (!email.matches(emailPattern)) {
            emailError = "Please enter a valid email"
            hasError = true
        }

        if (password.isBlank()) {
            passwordError = "Password cannot be empty"
            hasError = true
        } else if (password.length < 6) {
            passwordError = "Must be at least 6 characters"
            hasError = true
        }

        if (confirmPassword.isBlank()) {
            confirmPasswordError = "Please confirm password"
            hasError = true
        } else if (password != confirmPassword) {
            confirmPasswordError = "Passwords do not match"
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            isLoading = true
            val result = authRepository.signUp(email, password, username)
            if (result.isSuccess) {
                isLoggedIn = true
                reset()
            } else {
                generalError = result.exceptionOrNull()?.message
            }
            isLoading = false
        }
    }

    fun changePassword(newPassword: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = authRepository.updatePassword(newPassword)
            if (result.isSuccess) {
                onResult(true, null)
            } else {
                onResult(false, result.exceptionOrNull()?.message)
            }
        }
    }

    enum class OnboardingStatus {
        LOADING, COMPLETED, INCOMPLETE
    }

    // Inside your AuthViewModel (or similar global ViewModel):
    private val _onboardingStatus = MutableStateFlow(OnboardingStatus.LOADING)
    val onboardingStatus = _onboardingStatus.asStateFlow()

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