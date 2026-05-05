package com.scribesoul.app.models

import kotlinx.serialization.Serializable

@Serializable
data class PostDTO(
    val id: String = "",
    val title: String = "Anonymous",
    val description: String = "",
    val initialLikeCount: Int = 0,
    val commentCount: Int = 0,
    val date: String = ""
)