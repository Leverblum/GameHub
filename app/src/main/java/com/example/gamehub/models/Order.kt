package com.example.gamehub.models

import java.util.Date


data class Order(
    val id: String,
    val date: Date,
    val status: String,
    val total: Double,
    val items: List<CartItem>
)
