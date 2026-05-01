package com.scribesoul.app.models

data class Chat(
    val message: String,
    var sender: String,
    val isMine: Boolean,

)