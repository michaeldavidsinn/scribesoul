package com.scribesoul.app.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatDTO(
    val id: String = "",
    val message: String = "",
    val sender: String = "Anonymous",
    val senderId: String = "",
    val timestamp: Long = 0L // Menggunakan Long lebih aman untuk Firestore
)