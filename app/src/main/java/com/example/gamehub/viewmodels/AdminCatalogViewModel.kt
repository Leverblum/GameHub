package com.example.gamehub.ui.admin.catalog

import com.example.gamehub.R
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamehub.models.Product
import java.math.BigDecimal

class AdminCatalogViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    init {
        loadProducts()
    }

    private fun loadProducts() {
        val hardcodedProducts = listOf(
            Product(1, "The Last of Us Part II", "Aventura y acción post-apocalíptica.", BigDecimal("59.99"), 30, "PS4", R.drawable.ic_zelda, isActive = true),
            Product(2, "Cyberpunk 2077", "RPG de mundo abierto en Night City.", BigDecimal("49.99"), 15, "PC", R.drawable.ic_ciberpunk, isActive = true),
            Product(3, "Animal Crossing: NH", "Simulador de vida en una isla.", BigDecimal("55.00"), 50, "Nintendo Switch", R.drawable.ic_animalcrossing, isActive = true),
            Product(4, "Halo Infinite", "Shooter en primera persona de ciencia ficción.", BigDecimal("69.99"), 22, "Xbox", R.drawable.ic_halo, isActive = true),
            Product(5, "God of War Ragnarök", "Aventura épica nórdica.", BigDecimal("70.00"), 0, "PS5", R.drawable.ic_gowragnarok, isActive = false)
        )
        _products.value = hardcodedProducts
    }

    /**
     * Añade un nuevo producto a la lista.
     */
    fun addProduct(name: String, description: String, price: BigDecimal, stock: Int, category: String) {
        val currentList = _products.value?.toMutableList() ?: mutableListOf()
        val newId = (currentList.maxOfOrNull { it.id } ?: 0) + 1

        val newProduct = Product(
            id = newId,
            name = name,
            description = description,
            price = price,
            stock = stock,
            category = category,
            // Asignamos una imagen por defecto para los nuevos productos
            imageResId = R.drawable.ic_gamepad,
            isActive = true
        )

        currentList.add(newProduct)
        _products.value = currentList
    }

    /**
     * Busca un producto por su ID en la lista actual.
     * @return El [Product] si se encuentra, o null si no.
     */
    fun getProductById(id: Int): Product? {
        return _products.value?.find { it.id == id }
    }

    /**
     * Actualiza un producto existente en la lista.
     */
    fun updateProduct(id: Int, name: String, description: String, price: BigDecimal, stock: Int, category: String) {
        val currentList = _products.value?.toMutableList() ?: return

        val productIndex = currentList.indexOfFirst { it.id == id }
        if (productIndex != -1) {
            val currentImageResId = currentList[productIndex].imageResId

            val updatedProduct = currentList[productIndex].copy(
                name = name,
                description = description,
                price = price,
                stock = stock,
                category = category,
                imageResId = currentImageResId
            )
            currentList[productIndex] = updatedProduct
            _products.value = currentList
        }
    }
}
