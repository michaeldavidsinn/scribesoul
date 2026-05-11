package com.scribesoul.app.viewModels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.scribesoul.app.ScribeSoulApplication
import com.scribesoul.app.models.ClientInfoDTO
import com.scribesoul.app.models.Therapist
import com.scribesoul.app.models.TherapySession
import com.scribesoul.app.models.toUIModel
import com.scribesoul.app.repository.TherapistRepository
import com.scribesoul.app.ui.screens.therapist.ClientStage
import kotlinx.coroutines.launch
import toUIModel
import java.time.LocalTime

class TherapistHomeViewModel(
    private val therapistRepository: TherapistRepository
) : ViewModel() {

    var therapistProfile by mutableStateOf<Therapist?>(null)
        private set

    var therapistSessions = mutableStateListOf<TherapySession>()
        private set

    val currentTime: LocalTime = LocalTime.now()

    var therapistClients = mutableStateListOf<ClientInfoDTO>()
        private set

    var selectedDate by mutableStateOf(java.time.LocalDate.now())
        private set

    fun selectDate(date: java.time.LocalDate) {
        selectedDate = date
    }

    // Fungsi salam (Greeting) tetap dibutuhkan di Dashboard Therapist
    fun getGreeting(): String {
        return when {
            currentTime < LocalTime.of(12, 0) -> "Good Morning"
            currentTime < LocalTime.of(18, 0) -> "Good Afternoon"
            else -> "Good Night"
        }
    }

    fun loadTherapistDashboard() {
        viewModelScope.launch {
            // 1. Ambil Profil (Nama, Spesialisasi, dsb)
            val profileResult = therapistRepository.getTherapistProfile()
            profileResult.onSuccess { dto ->
                therapistProfile = dto?.toUIModel() as Therapist?
            }

            // 2. Ambil Sesi (Jadwal & Client)
            val sessionResult = therapistRepository.getTherapistSessions()
            sessionResult.onSuccess { dtos ->
                therapistSessions.clear()
                therapistSessions.addAll(dtos.map { it.toUIModel() })
            }
        }
    }

    fun updateBirthday(newDate: String) {
        viewModelScope.launch {
            val result = therapistRepository.updateTherapistBirthday(newDate)
            if (result.isSuccess) {
                // Refresh data dashboard agar UI di semua page terupdate
                loadTherapistDashboard()
            }
        }
    }

    fun loadClients() {
        viewModelScope.launch {
            val result = therapistRepository.getClientsForTherapist()
            result.onSuccess { clients ->
                therapistClients.clear()
                therapistClients.addAll(clients)
            }
        }
    }

    fun getFilteredClients(stage: ClientStage): List<ClientInfoDTO> {
        return therapistClients.filter { it.urgencyLevel == stage.name }
    }

    var selectedClientSessions = mutableStateListOf<TherapySession>()
        private set

    fun loadClientDetailData(clientId: String) {
        viewModelScope.launch {
            val result = therapistRepository.getClientSessionHistory(clientId)
            result.onSuccess { dtos ->
                selectedClientSessions.clear()
                selectedClientSessions.addAll(dtos.map { it.toUIModel() })
            }
        }
    }

    fun getSessionsForSelectedDate(): List<com.scribesoul.app.models.TherapySession> {
        return therapistSessions.filter {
            com.scribesoul.app.utils.DateTimeUtils.isSameDay(it.dateTimestamp, selectedDate)
        }.sortedBy { it.dateTimestamp }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ScribeSoulApplication)
                TherapistHomeViewModel(application.container.therapistRepository)
            }
        }
    }
}