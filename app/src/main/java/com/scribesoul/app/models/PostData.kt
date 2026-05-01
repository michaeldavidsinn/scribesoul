package com.scribesoul.app.model

data class PostData(
    val id: Int,
    val title: String,
    val description: String,
    val initialLikeCount: Int,
    val commentCount: Int,
    var isLiked: Boolean = false,
    val date: String = "Just Now"
)