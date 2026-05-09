package com.scribesoul.app.models

enum class PaymentStatus {
    PENDING, DONE, FAILED
}

// Model untuk Kuitansi / Penagihan
data class Billing(
    val billingId: String = "",
    val sessionId: String = "",
    val paymentMethod: String = "", // e.g., "Gopay"
    val accountIdentifier: String = "",
    val payerFirstName: String = "",
    val payerLastName: String = "",
    val amount: Double = 0.0,
    val status: PaymentStatus = PaymentStatus.PENDING
)