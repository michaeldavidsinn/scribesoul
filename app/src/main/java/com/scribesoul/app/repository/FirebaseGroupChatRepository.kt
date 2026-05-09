package com.scribesoul.app.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.scribesoul.app.models.Chat
import com.scribesoul.app.models.ChatDTO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import toDTO
import toUIModel

class FirebaseGroupChatRepository(
    private val firestore: FirebaseFirestore
) : GroupChatRepository {

    private val chatCollection = firestore.collection("community_chats")

    // 1. Ambil Chat secara Real-time
    override fun getGroupChats(): Flow<List<Chat>> = callbackFlow {
        // Susun query berdasarkan waktu (timestamp)
        val subscription = chatCollection
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    // Ambil sebagai DTO dulu, lalu ubah ke UI Model menggunakan Mapper
                    val chats = snapshot.toObjects(ChatDTO::class.java).map { it.toUIModel() }
                    trySend(chats)
                }
            }

        // Tutup koneksi saat tidak digunakan lagi
        awaitClose { subscription.remove() }
    }

    // 2. Kirim Chat
    override suspend fun sendMessage(chat: Chat) {
        try {
            // Ubah UI Model ke DTO sebelum dikirim ke Firebase
            val chatDTO = chat.toDTO()
            chatCollection.add(chatDTO).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}