package com.scribesoul.app.models

import java.time.LocalDate

data class User(
    val id: Int,
    var name: String,
    val email: String,
    var birthday: LocalDate

)