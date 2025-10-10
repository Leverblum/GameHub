package com.example.gamehub.repository

import android.content.Context
import com.example.gamehub.models.CartItem
import com.example.gamehub.models.Order
import com.example.gamehub.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Repositorio para manejar toda la lógica de lectura y escritura en SharedPreferences.
 * Utiliza Gson para serializar/deserializar objetos complejos.
 */
class PrefsRepository(context: Context) {

    private val gson = Gson()
    private val prefs = context.getSharedPreferences("GameHubPrefs", Context.MODE_PRIVATE)

    // --- Gestión de Sesión ---
    private val KEY_ACTIVE_USER_EMAIL = "active_user_email"

    fun saveActiveUser(email: String) {
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

    fun getUsers(): MutableList<User> {
        val json = prefs.getString(KEY_USERS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<User>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf() // Devuelve una lista vacía si no hay usuarios
        }
    }

    // --- Gestión del Carrito de Compras ---
    private val KEY_CART_ITEMS = "cart_items"

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
}
