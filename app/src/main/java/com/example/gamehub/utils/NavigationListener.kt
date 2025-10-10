package com.example.gamehub.utils

import com.example.gamehub.models.Product

/**
 * Interfaz para manejar eventos de navegación iniciados desde fragments o adapters.
 */
interface NavigationListener {
    // Cuando el usuario quiera ver el detalle de un producto
    fun onNavigateToProductDetail(product: Product)

    // Podríamos añadir más funciones aquí en el futuro
    // fun onNavigateToCheckout()
    // fun onNavigateToStoreProfile(storeId: Int)
}
