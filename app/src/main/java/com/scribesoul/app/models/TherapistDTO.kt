package com.scribesoul.app.models

import kotlinx.serialization.Serializable

@Serializable
data class TherapistDTO(
    val id: String = "",
    val name: String = "",
    val title: String = "",
    val strNumber: String = "",
    val pricePerSession: Double = 0.0,
    val experienceYears: Int = 0,
    val totalClients: Int = 0,
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val biography: String = "",
    val specializations: List<String> = emptyList(),
    val languages: List<String> = emptyList(),
    val therapyApproaches: List<String> = emptyList(),
    val educations: List<EducationDTO> = emptyList(),
    val workPractices: List<String> = emptyList()
)

@Serializable
data class EducationDTO(
    val institution: String = "",
    val degree: String = "",
    val year: String = ""
)

@Serializable
data class ClientInfoDTO(
    val clientId: String = "",
    val name: String = "",
    val mainCondition: String = "",
    val urgencyLevel: String = "MILD", // Simpan enum sebagai String di Firebase
    val lastSessionDate: Long = 0L,
    val totalSessions: Int = 0,
    val latestNote: String = ""
)

@Serializable
data class TherapySessionDTO(
    val sessionId: String = "",
    val therapistId: String = "",
    val clientId: String = "",
    val dateTimestamp: Long = 0L,
    val durationMinutes: Int = 60,
    val location: String = "Online Zoom",
    val status: String = "SCHEDULED", // Simpan enum sebagai String
    val therapistNote: String = "",
    val billingId: String? = null
)

@Serializable
data class BillingDTO(
    val billingId: String = "",
    val sessionId: String = "",
    val paymentMethod: String = "",
    val accountIdentifier: String = "",
    val payerFirstName: String = "",
    val payerLastName: String = "",
    val amount: Double = 0.0,
    val status: String = "PENDING"
)