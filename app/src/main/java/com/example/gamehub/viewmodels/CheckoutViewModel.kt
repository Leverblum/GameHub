package com.example.gamehub.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gamehub.models.Order
import com.example.gamehub.repository.PrefsRepository
import java.math.BigDecimal
import java.util.*

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsRepository = PrefsRepository(application)
    private val _orderCompletedEvent = MutableLiveData<Boolean>()
    val orderCompletedEvent: LiveData<Boolean> = _orderCompletedEvent

    fun completeCheckout(cartViewModel: CartViewModel) {
        val userEmail = prefsRepository.getActiveUserEmail()
        val cartItems = cartViewModel.cartItems.value

        if (userEmail != null && !cartItems.isNullOrEmpty()) {
            // ================== INICIO DE LA CORRECCIÓN ==================
            // Calculamos el subtotal usando los métodos de BigDecimal
            val subtotal = cartItems.fold(BigDecimal.ZERO) { acc, cartItem ->
                acc + cartItem.product.price.multiply(BigDecimal(cartItem.quantity))
            }
            // Calculamos el total con impuestos
            val totalAmount = subtotal.multiply(BigDecimal("1.19"))
            // =================== FIN DE LA CORRECCIÓN ====================

            val newOrder = Order(
                id = UUID.randomUUID().toString().substring(0, 6).uppercase(),
                items = cartItems,
                total = totalAmount.toDouble(), // Convertimos a Double si el modelo Order lo requiere
                date = Date(),
                status = "Procesando"
            )

            val allOrders = prefsRepository.getOrders()
            allOrders.add(newOrder)
            prefsRepository.saveOrders(allOrders)

            cartViewModel.clearCart()
            _orderCompletedEvent.value = true
        }
    }

    fun onOrderCompletedEventHandled() {
        _orderCompletedEvent.value = false
    }
}
