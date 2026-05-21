package com.scribesoul.app.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.scribesoul.app.models.Chat
import com.scribesoul.app.repository.GroupChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CommunityViewModel(
    private val repository: GroupChatRepository // Masukkan repository Firebase
) : ViewModel() {

    // Gunakan StateFlow untuk aliran data reaktif dari Firebase
    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats

    var joined by mutableStateOf(false)

    // Generate ID unik sementara untuk sesi anonim ini (agar tahu mana chat 'isMine')
    private val anonymousSessionId = UUID.randomUUID().toString()

    init {
        // Mulai ambil data secara real-time dari Firebase saat ViewModel dibuat
        viewModelScope.launch {
            repository.getGroupChats().collect { chatList ->
                // Urutkan berdasarkan waktu agar pesan terbaru ada di bawah
                _chats.value = chatList.sortedBy { it.timestamp }
            }
        }
    }

    fun chat(message: String) {
        viewModelScope.launch {
            val newChat = Chat(
                id = "", // Biarkan kosong, Firebase akan generate ID dokumen
                message = message,
                sender = "Anonymous",
                senderId = anonymousSessionId, // Tandai ini chat milikmu
                timestamp = System.currentTimeMillis()
            )
            // Kirim ke Firebase
            repository.sendMessage(newChat)
        }
    }

    // Fungsi tambahan untuk mengecek apakah chat ini milik user yang sedang aktif
    fun isMyChat(chat: Chat): Boolean {
        return chat.senderId == anonymousSessionId
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // 1. Ambil Application Context
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as com.scribesoul.app.ScribeSoulApplication)

                // 2. Hubungkan dengan FirebaseGroupChatRepository asli!
                CommunityViewModel(
                    repository = application.container.groupChatRepository
                )
            }
        }
    }
}