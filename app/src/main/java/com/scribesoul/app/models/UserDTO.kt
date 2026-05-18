package com.scribesoul.app.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: String = "",
    val name: String = "",
    val age: String = "",
    val gender: String = "",
    val problems: List<String> = emptyList(),
    val goals: List<String> = emptyList(),
    val feeling: String = "",
    val startFeeling: String = "",
    val oftenFeeling: String = "",
    val oftenMoodSwings: String = "",
    val oftenEmotionalSupport: String = "",
    val supportTypes: List<String> = emptyList(),
    val challenges: List<String> = emptyList(),
    val motivations: List<String> = emptyList(),

    val soughtMentalHealth: String = "",
    val supportKind: String = "",
    val seekingHelpDuration: String = "",
    val connectionImportance: String = ""
)