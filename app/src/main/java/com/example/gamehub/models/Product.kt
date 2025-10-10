package com.example.gamehub.models

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val platform: String? = null,
    val rating: String? = null,
    val description: String? = null
)
