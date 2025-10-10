package com.example.gamehub.models

data class User(
    val id: Int,
    var name: String,
    val email: String,
    val password: String,
    var role: String,
    var phone: String? = null,
    var address: String? = null,
    var dateOfBirth: String? = null,
    var avatarUrl: String? = null
)
