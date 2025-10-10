package com.example.gamehub.models

/**
 * Representa a un usuario en la aplicación.
 * @param id Un identificador único, podemos usar el email por simplicidad.
 * @param name El nombre completo del usuario.
 * @param email El correo electrónico del usuario, que también funciona como su login.
 * @param password La contraseña del usuario.
 * @param role El rol del usuario ("user" o "admin") para distinguir los flujos.
 */
data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val role: String = "user"
)
