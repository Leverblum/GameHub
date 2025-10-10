package com.example.gamehub.models

import java.util.UUID

data class PaymentMethod(
    val id: String = UUID.randomUUID().toString(),
    val userEmail: String,
    val cardNumber: String,
    val cardHolderName: String,
    val expiryDate: String,
    val cardType: String
)