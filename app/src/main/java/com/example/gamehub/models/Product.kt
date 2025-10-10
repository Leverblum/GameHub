package com.example.gamehub.models

import java.math.BigDecimal

data class Product(
    val id: Int,
    var name: String,
    var description: String,
    var price: BigDecimal,
    var stock: Int,
    var category: String,
    var imageUrl: String? = null,
    var isActive: Boolean = true
)
