package com.scribesoul.app.repository

import com.scribesoul.app.models.Chat
import kotlinx.coroutines.flow.Flow

interface GroupChatRepository {
    // Mengambil aliran pesan secara real-time dari Firebase
    fun getGroupChats(): Flow<List<Chat>>

    // Mengirim pesan ke Firebase
    suspend fun sendMessage(chat: Chat)
}