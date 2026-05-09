package com.scribesoul.app.models

enum class SessionStatus {
    SCHEDULED, COMPLETED, CANCELLED
}

// Model untuk jadwal pertemuan dan riwayat sesi
data class TherapySession(
    val sessionId: String = "",
    val therapistId: String = "",
    val clientId: String = "",
    val dateTimestamp: Long = 0L, // Tanggal & Waktu spesifik
    val durationMinutes: Int = 60, // e.g., 60 minutes
    val location: String = "Online Zoom",
    val status: SessionStatus = SessionStatus.SCHEDULED,
    val therapistNote: String = "", // Catatan klinis pasca sesi
    val billingId: String? = null // Referensi ke data pembayaran
)