package com.scribesoul.app.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.scribesoul.app.ScribeSoulApplication
import com.scribesoul.app.models.Therapist
import com.scribesoul.app.repository.TherapistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import toUIModel

class TherapistDirectoryViewModel(
    private val therapistRepository: TherapistRepository
) : ViewModel() {

    private val _therapists = MutableStateFlow<List<Therapist>>(emptyList())
    val therapists: StateFlow<List<Therapist>> = _therapists

    init {
        fetchAllTherapists()
    }

    private fun fetchAllTherapists() {
        viewModelScope.launch {
            val result = therapistRepository.getAllTherapists()
            result.onSuccess { dtoList ->
                // Ubah dari DTO Firebase ke UI Model
                _therapists.value = dtoList.map { it.toUIModel() }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ScribeSoulApplication)
                TherapistDirectoryViewModel(application.container.therapistRepository)
            }
        }
    }
}