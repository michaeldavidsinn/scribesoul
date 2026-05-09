package com.scribesoul.app.viewModels

import Journal
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.scribesoul.app.repository.JournalRepository
import kotlinx.coroutines.launch
import kotlin.math.abs

class JournalListViewModel(
    private val repository: JournalRepository
): ViewModel() {
    private val _journals = mutableStateListOf<Journal>()
    val journals: List<Journal> = _journals

    init {
        // 2. Fetch the journals from Firebase when the app opens
        loadJournalsFromCloud()
    }

    private fun loadJournalsFromCloud() {
        viewModelScope.launch {
            val metas = repository.getAllJournals()
            _journals.clear()

            // Convert the lightweight Firestore metadata back into UI Journal objects
            _journals.addAll(metas.map { meta ->
                Journal(
                    id = meta.id,
                    uid = 0, // Unused locally
                    name = meta.name
                )
            })
        }
    }

    fun addJournal(name: String){
        // 3. Generate a mathematically unique ID so Firebase files never accidentally overwrite each other
        val uniqueId = abs(System.currentTimeMillis().toInt())

        val newJournal = Journal(
            id = uniqueId,
            uid = 0,
            name = name
        )
        _journals.add(newJournal)

        // Note: We do NOT need to save to Firebase right here.
        // When the user opens this blank canvas and then hits the "Back" button,
        // JournalViewModel.saveJournal() will automatically trigger and save it to the cloud!
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
            initializer {
                // 5. Provide the repository via the AppContainer
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as com.scribesoul.app.ScribeSoulApplication)
                JournalListViewModel(repository = application.container.journalRepository)
            }
        }
    }
}