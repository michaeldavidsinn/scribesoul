package com.scribesoul.app.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.scribesoul.app.models.Chat

class CommunityViewModel: ViewModel() {
    private val _chats = mutableStateListOf<Chat>()
    val chats: SnapshotStateList<Chat> get() = _chats

    var joined by mutableStateOf(false)

    fun chat(message: String){
        _chats.add(Chat(
            message = message,
            sender = "Me",
            isMine = true
        ))
    }



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { CommunityViewModel() }
        }
    }
}