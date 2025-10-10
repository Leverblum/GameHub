package com.example.gamehub.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gamehub.models.PaymentMethod
import com.example.gamehub.repository.PrefsRepository

class PaymentViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsRepository = PrefsRepository(application)
    private val activeUserEmail: String? = prefsRepository.getActiveUserEmail()

    private val _paymentMethods = MutableLiveData<List<PaymentMethod>>()
    val paymentMethods: LiveData<List<PaymentMethod>> = _paymentMethods

    init {
        loadPaymentMethods()
    }

    private fun loadPaymentMethods() {
        if (activeUserEmail != null) {
            val allMethods = prefsRepository.getPaymentMethods()
            _paymentMethods.value = allMethods.filter { it.userEmail == activeUserEmail }
        } else {
            _paymentMethods.value = emptyList()
        }
    }

    fun addPaymentMethod(cardNumber: String, cardHolderName: String, expiryDate: String, cardType: String) {
        if (activeUserEmail != null) {
            val newMethod = PaymentMethod(
                userEmail = activeUserEmail,
                cardNumber = cardNumber,
                cardHolderName = cardHolderName,
                expiryDate = expiryDate,
                cardType = cardType
            )

            val allMethods = prefsRepository.getPaymentMethods()
            allMethods.add(newMethod)
            prefsRepository.savePaymentMethods(allMethods)

            // Recargamos la lista para notificar a la UI
            loadPaymentMethods()
        }
    }

    fun deletePaymentMethod(paymentMethod: PaymentMethod) {
        if (activeUserEmail != null) {
            val allMethods = prefsRepository.getPaymentMethods()
            // Eliminamos el método por su ID único
            allMethods.removeAll { it.id == paymentMethod.id }
            prefsRepository.savePaymentMethods(allMethods)

            // Recargamos la lista
            loadPaymentMethods()
        }
    }
}
