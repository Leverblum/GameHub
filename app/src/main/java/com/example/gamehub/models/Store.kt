package com.example.gamehub.models

data class Store(
    val id: Int,
    val name: String,
    val address: String,
    val distance: String,
    val imageUrl: String? = null
)
