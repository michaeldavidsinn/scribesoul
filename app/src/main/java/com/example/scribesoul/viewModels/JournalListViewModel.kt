package com.example.scribesoul.viewModels

import Journal
import JournalSection
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController

class JournalListViewModel: ViewModel() {
    private val _journals = mutableStateListOf<Journal>()
    val journals: List<Journal> = _journals

    fun addJournal(name: String){
        _journals.add(
            Journal(
                id = _journals.size + 1,
                uid = 0,
                name = name
            )
        )


    }

    fun changeJournalName(id: Int, name: String){
        _journals[id].name = name
    }

    fun deleteJournal(id: Int, navController: NavController){
        _journals.removeAt(id)
        navController.popBackStack()
    }

    fun findJournalIndex(id: Int): Int {
        return _journals.indexOfFirst { journal -> journal.id == id }
    }




    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { JournalListViewModel() }
        }
    }
}