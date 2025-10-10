package com.example.gamehub.utils

import com.example.gamehub.models.CartItem

/**
 * Interfaz para manejar eventos dentro del carrito de compras.
 */
interface CartListener {
    // Se llama cuando la cantidad de un ítem cambia.
    fun onQuantityChanged()
    // Se llama cuando un ítem es eliminado.
    fun onItemRemoved(item: CartItem)
}
