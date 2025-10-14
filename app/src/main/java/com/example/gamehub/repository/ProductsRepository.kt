package com.example.gamehub.repository

import android.content.Context
import com.example.gamehub.models.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductsRepository(context: Context) {

    private val prefs = context.getSharedPreferences("gamehub_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_PRODUCTS = "products_list"
    }

    // 🟢 Obtener todos los productos
    fun getProducts(): MutableList<Product> {
        val json = prefs.getString(KEY_PRODUCTS, null)
        return if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<Product>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    // 🟣 Guardar lista completa
    private fun saveProducts(products: List<Product>) {
        val json = gson.toJson(products)
        prefs.edit().putString(KEY_PRODUCTS, json).apply()
    }

    // 🟩 Agregar un nuevo producto
    fun addProduct(product: Product) {
        val products = getProducts()
        products.add(product.copy(id = generateProductId(products)))
        saveProducts(products)
    }

    // 🟡 Editar producto existente
    fun updateProduct(updated: Product) {
        val products = getProducts()
        val index = products.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            products[index] = updated
            saveProducts(products)
        }
    }

    // 🔴 Eliminar producto
    fun deleteProduct(id: Int) {
        val products = getProducts().filter { it.id != id }
        saveProducts(products)
    }

    // 🆔 Generar ID autoincremental
    private fun generateProductId(products: List<Product>): Int {
        return if (products.isEmpty()) 1 else (products.maxOf { it.id } + 1)
    }
}
