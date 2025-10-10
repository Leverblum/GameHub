package com.example.gamehub.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gamehub.models.Order
import com.example.gamehub.repository.PrefsRepository
import java.util.*

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsRepository = PrefsRepository(application)

    // LiveData para notificar que la compra se completó exitosamente
    private val _orderCompletedEvent = MutableLiveData<Boolean>()
    val orderCompletedEvent: LiveData<Boolean> = _orderCompletedEvent

    /**
     * Procesa la finalización de la compra.
     */
    fun completeCheckout(cartViewModel: CartViewModel) {
        // Obtenemos los datos necesarios
        val userEmail = prefsRepository.getActiveUserEmail()
        val cartItems = cartViewModel.cartItems.value

        // Solo procedemos si tenemos un usuario y hay items en el carrito
        if (userEmail != null && !cartItems.isNullOrEmpty()) {
            val totalAmount = cartItems.sumOf { it.product.price * it.quantity } * 1.19 // Suma 19% de impuestos

            // ================== INICIO DE LA CORRECCIÓN ==================
            // Creamos la nueva orden usando los parámetros CORRECTOS de tu clase Order.kt

            val newOrder = Order(
                id = UUID.randomUUID().toString().substring(0, 6).uppercase(), // ID de pedido aleatorio y corto
                items = cartItems,
                total = totalAmount,
                date = Date(),
                status = "Procesando" // Añadimos el estado inicial requerido
                // El campo 'userEmail' no se pasa porque tu modelo no lo tiene
            )
            // =================== FIN DE LA CORRECCIÓN ====================


            // 2. Guardamos la orden en el historial
            val allOrders = prefsRepository.getOrders()
            allOrders.add(newOrder)
            prefsRepository.saveOrders(allOrders)

            // 3. Limpiamos el carrito
            cartViewModel.clearCart()

            // 4. Notificamos que la orden fue completada
            _orderCompletedEvent.value = true
        }
    }

    fun onOrderCompletedEventHandled() {
        _orderCompletedEvent.value = false
    }
}
