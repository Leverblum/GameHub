package com.example.gamehub.models

import java.math.BigDecimal

data class Game(
    val id: Int,
    val title: String,
    val price: BigDecimal,
    val imageUrl: String
)
