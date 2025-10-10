package com.example.gamehub.models

import java.util.Date

/**
 * Representa un pedido realizado por un usuario.
 * @param id El identificador único del pedido.
 * @param date La fecha en que se realizó el pedido.
 * @param status El estado actual del pedido (ej: "Procesando", "Enviado", "Entregado").
 * @param total El monto total del pedido.
 * @param items La lista de productos que contiene el pedido.
 */
data class Order(
    val id: String,
    val date: Date,
    val status: String,
    val total: Double,
    val items: List<CartItem>
)
