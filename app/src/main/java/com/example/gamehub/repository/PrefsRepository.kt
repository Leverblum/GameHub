package com.example.gamehub.repository

import com.example.gamehub.R
import android.content.Context
import com.example.gamehub.models.CartItem
import com.example.gamehub.models.Game
import com.example.gamehub.models.Order
import com.example.gamehub.models.PaymentMethod
import com.example.gamehub.models.Product
import com.example.gamehub.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal

class PrefsRepository(context: Context) {

    private val gson = Gson()
    private val prefs = context.getSharedPreferences("GameHubPrefs", Context.MODE_PRIVATE)

    // --- Gestión de Sesión ---
    private val KEY_ACTIVE_USER_EMAIL = "active_user_email"

    fun setActiveUserEmail(email: String) {
        prefs.edit().putString(KEY_ACTIVE_USER_EMAIL, email).apply()
    }

    fun getActiveUserEmail(): String? {
        return prefs.getString(KEY_ACTIVE_USER_EMAIL, null)
    }

    fun clearActiveUser() {
        prefs.edit().remove(KEY_ACTIVE_USER_EMAIL).apply()
    }

    // --- Gestión de Usuarios (Registro/Login) ---
    private val KEY_USERS = "users_list"

    fun saveUsers(users: List<User>) {
        val json = gson.toJson(users)
        prefs.edit().putString(KEY_USERS, json).apply()
    }

    fun saveUser(user: User) {
        val currentUsers = getUsers().toMutableList() // obtiene los usuarios actuales
        currentUsers.add(user) // añade el nuevo usuario
        saveUsers(currentUsers) // guarda la lista completa
    }


    fun getUsers(): MutableList<User> {
        val json = prefs.getString(KEY_USERS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<User>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    // --- Gestión del Carrito de Compras ---
    private val KEY_CART_ITEMS = "cart_items"

    fun addProductToCart(product: Product) {
        val items = getCartItems()
        val existingItem = items.find { it.product.id == product.id }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            val cartItem = CartItem(product = product, quantity = 1)
            items.add(cartItem)
        }
        saveCartItems(items)
    }

    fun addGameToCart(game: Game) {
        val product = Product(
            id = game.id,
            name = game.title,
            description = "Juego disponible en GameHub",
            price = (game.price),
            stock = 10,
            category = "Videojuego",
            imageResId = R.drawable.ic_gamepad,
            isActive = true
        )
        addProductToCart(product)
    }

    fun saveCartItems(cartItems: List<CartItem>) {
        val json = gson.toJson(cartItems)
        prefs.edit().putString(KEY_CART_ITEMS, json).apply()
    }

    fun getCartItems(): MutableList<CartItem> {
        val json = prefs.getString(KEY_CART_ITEMS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<CartItem>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    fun clearCart() {
        prefs.edit().remove(KEY_CART_ITEMS).apply()
    }

    // --- Gestión de Pedidos ---
    private val KEY_ORDERS = "orders_list"

    fun saveOrders(orders: List<Order>) {
        val json = gson.toJson(orders)
        prefs.edit().putString(KEY_ORDERS, json).apply()
    }

    fun getOrders(): MutableList<Order> {
        val json = prefs.getString(KEY_ORDERS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Order>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    // --- Gestión de Métodos de Pago ---
    private val KEY_PAYMENT_METHODS = "payment_methods_list"

    fun savePaymentMethods(methods: List<PaymentMethod>) {
        val json = gson.toJson(methods)
        prefs.edit().putString(KEY_PAYMENT_METHODS, json).apply()
    }

    fun getPaymentMethods(): MutableList<PaymentMethod> {
        val json = prefs.getString(KEY_PAYMENT_METHODS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<PaymentMethod>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    fun logout() {
        prefs.edit()
            .remove(KEY_ACTIVE_USER_EMAIL)
            .apply()
    }

}
