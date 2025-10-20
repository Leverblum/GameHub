package com.example.gamehub.models

import androidx.annotation.DrawableRes
import java.math.BigDecimal

data class Product(
    val id: Int,
    var name: String,
    var description: String? = null,
    var price: BigDecimal,
    var stock: Int,
    var category: String,
    @DrawableRes var imageResId: Int,
    var isActive: Boolean = true
)
