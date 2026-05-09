package com.scribesoul.app.models

// Model UI untuk Therapist
data class Therapist(
    val id: String = "",
    val name: String = "",
    val title: String = "", // e.g., "Psikolog Klinis"
    val strNumber: String = "",
    val pricePerSession: Double = 0.0,
    val experienceYears: Int = 0,
    val totalClients: Int = 0,
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val biography: String = "",
    val specializations: List<String> = emptyList(), // e.g., ["Anxiety", "Depresi"]
    val languages: List<String> = emptyList(),
    val therapyApproaches: List<String> = emptyList(), // e.g., ["CBT", "Art Therapy"]
    val educations: List<Education> = emptyList(),
    val workPractices: List<String> = emptyList()
)

data class Education(
    val institution: String = "",
    val degree: String = "", // e.g., "S1", "S2"
    val year: String = ""
)