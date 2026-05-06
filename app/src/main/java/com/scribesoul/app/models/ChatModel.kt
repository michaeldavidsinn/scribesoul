package com.scribesoul.app.models

data class Chat(
    val id: String = "",
    val message: String = "",
    val sender: String = "Anonymous",
    val senderId: String = "", // Untuk mengecek isMine
    val timestamp: Long = System.currentTimeMillis()
)