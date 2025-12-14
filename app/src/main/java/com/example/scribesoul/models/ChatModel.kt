package com.example.scribesoul.models

import java.time.LocalDate

data class Chat(
    val message: String,
    var sender: String,
    val isMine: Boolean,

)