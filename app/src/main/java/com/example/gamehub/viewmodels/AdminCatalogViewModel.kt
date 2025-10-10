package com.example.gamehub.ui.admin.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamehub.models.Product // Usamos el modelo Product actualizado
import java.math.BigDecimal

class AdminCatalogViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    init {
        loadProducts()
    }

    private fun loadProducts() {
        // Creamos una lista de objetos 'Product' con la estructura completa.
        val hardcodedProducts = listOf(
            Product(1, "The Last of Us Part II", "Aventura y acción post-apocalíptica.", BigDecimal("59.99"), 30, "PS4", isActive = true),
            Product(2, "Cyberpunk 2077", "RPG de mundo abierto en Night City.", BigDecimal("49.99"), 15, "PC", isActive = true),
            Product(3, "Animal Crossing: NH", "Simulador de vida en una isla.", BigDecimal("55.00"), 50, "Nintendo Switch", isActive = true),
            Product(4, "Halo Infinite", "Shooter en primera persona de ciencia ficción.", BigDecimal("69.99"), 22, "Xbox", isActive = true),
            Product(5, "God of War Ragnarök", "Aventura épica nórdica.", BigDecimal("70.00"), 0, "PS5", isActive = false)
        )
        _products.value = hardcodedProducts
    }
}
