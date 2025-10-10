package com.example.gamehub.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gamehub.models.CartItem
import com.example.gamehub.models.Product
import com.example.gamehub.repository.PrefsRepository

// Se usa AndroidViewModel para poder tener el 'context' y así usar PrefsRepository
class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsRepository = PrefsRepository(application)

    // _cartItems es PRIVADO y Mutable. Solo el ViewModel puede cambiarlo.
    private val _cartItems = MutableLiveData<MutableList<CartItem>>()

    // cartItems es PÚBLICO e inmutable. Los fragmentos solo pueden leerlo/observarlo.
    val cartItems: LiveData<MutableList<CartItem>> = _cartItems

    init {
        // Cuando el ViewModel se crea por primera vez, carga los datos del disco.
        _cartItems.value = prefsRepository.getCartItems()
    }

    /**
     * Esta es la ÚNICA función que otros usarán para añadir productos.
     * Centraliza toda la lógica.
     */
    fun addProductToCart(product: Product) {
        val currentItems = _cartItems.value ?: mutableListOf()
        val existingItem = currentItems.find { it.product.id == product.id }

        if (existingItem != null) {
            // Si ya existe, solo aumenta la cantidad
            existingItem.quantity++
        } else {
            // Si no, añade el nuevo item
            currentItems.add(CartItem(product = product, quantity = 1))
        }

        // 1. Notifica a todos los observadores (como CartFragment) que la lista ha cambiado.
        _cartItems.value = currentItems

        // 2. Guarda la lista actualizada en el disco para la próxima vez que se abra la app.
        prefsRepository.saveCartItems(currentItems)
    }

    // Aquí puedes añadir más funciones como removeItem, clearCart, etc.
    fun removeItemFromCart(cartItem: CartItem) {
        val currentItems = _cartItems.value ?: return
        currentItems.remove(cartItem)
        _cartItems.value = currentItems
        prefsRepository.saveCartItems(currentItems)
    }

    fun updateItemQuantity(cartItem: CartItem, newQuantity: Int) {
        val currentItems = _cartItems.value ?: return
        val itemToUpdate = currentItems.find { it.product.id == cartItem.product.id }
        itemToUpdate?.let {
            if (newQuantity > 0) {
                it.quantity = newQuantity
            } else {
                currentItems.remove(it)
            }
        }
        _cartItems.value = currentItems
        prefsRepository.saveCartItems(currentItems)
    }

    fun clearCart() {
        _cartItems.value = mutableListOf()
        prefsRepository.clearCart()
    }
}
