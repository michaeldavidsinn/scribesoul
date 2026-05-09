package com.scribesoul.app.models

enum class UrgencyLevel {
    MILD, MEDIUM, URGENT
}

// Model untuk merepresentasikan data pasien di dashboard Therapist
data class ClientInfo(
    val clientId: String = "",
    val name: String = "",
    val mainCondition: String = "", // e.g., "Anxiety"
    val urgencyLevel: UrgencyLevel = UrgencyLevel.MILD,
    val lastSessionDate: Long = 0L,
    val totalSessions: Int = 0,
    val latestNote: String = ""
)