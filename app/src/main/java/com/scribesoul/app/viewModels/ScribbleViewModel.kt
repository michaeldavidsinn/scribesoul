package com.scribesoul.app.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.scribesoul.app.commands.Command
import com.scribesoul.app.models.*
import com.scribesoul.app.repository.FirebaseScribbleRepository
import kotlinx.coroutines.launch
import toDTO
import java.util.UUID

class ScribbleViewModel(
    private val repository: FirebaseScribbleRepository
) : ViewModel() {
    val savedScribbles = mutableStateListOf<FirestoreScribbleMeta>()

    init {
        loadScribbles()
    }

    fun loadScribbles() {
        viewModelScope.launch {
            val list = repository.getAllScribbles()
            savedScribbles.clear()
            savedScribbles.addAll(list)
        }
    }

    // Now accepts all data as parameters
    fun saveScribble(
        id: String,
        name: String,
        paths: List<DrawablePath>,
        shapes: List<ShapeItem>,
        texts: List<EditableText>,
        imageLayers: List<ImageLayer>
    ) {
        viewModelScope.launch {
            val scribbleData = ScribbleDataDTO(
                id = id,
                paths = paths.map { it.toDTO() },
                shapes = shapes.map { it.toDTO() },
                texts = texts.map { it.toDTO() },
                imageLayers = imageLayers.map { it.toDTO() }
            )

            repository.saveScribble(id, name, scribbleData)
            loadScribbles()
        }
    }

    // Accepts the ID of the scribble to delete
    fun deleteScribble(id: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.deleteScribble(id)
            loadScribbles()
            onComplete()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Get the application context to access the AppContainer
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as com.scribesoul.app.ScribeSoulApplication)
                val repository = application.container.scribbleRepository


                ScribbleViewModel(repository = repository)
            }

        }
    }
}