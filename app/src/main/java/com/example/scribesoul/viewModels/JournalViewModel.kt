package com.example.scribesoul.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
//import com.example.scribesoul.ScribeSoulApplication

class JournalViewModel: ViewModel() {
    var selectedSectionID by mutableStateOf<Int?>(0)
//    var sectionList: StateFlow<List<Section>> = _sectionList

    fun changeSelectedSection(sectionID: Int){
        selectedSectionID = sectionID
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
//                val application = (this[APPLICATION_KEY] as ScribeSoulApplication)
                JournalViewModel()
            }
        }
    }





}