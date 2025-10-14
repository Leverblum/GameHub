package com.example.gamehub.ui.payment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels // Importante: Asegúrate de que este import se añada
import com.example.gamehub.R
import com.example.gamehub.viewmodels.PaymentViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class CardFormFragment : Fragment(R.layout.fragment_card_form) {

    private val viewModel: PaymentViewModel by activityViewModels()

    private lateinit var etCardHolderName: TextInputEditText
    private lateinit var etCardNumber: TextInputEditText
    private lateinit var etExpiryDate: TextInputEditText
    private lateinit var btnSaveCard: MaterialButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vinculamos las vistas
        etCardHolderName = view.findViewById(R.id.etCardHolderName)
        etCardNumber = view.findViewById(R.id.etCardNumber)
        etExpiryDate = view.findViewById(R.id.etExpiryDate)
        btnSaveCard = view.findViewById(R.id.btnSaveCard)

        // Configuramos el listener del botón
        setupListeners()
    }

    private fun setupListeners() {
        btnSaveCard.setOnClickListener {
            handleSaveCard()
        }
    }

    private fun handleSaveCard() {
        val cardHolderName = etCardHolderName.text.toString().trim()
        val cardNumber = etCardNumber.text.toString().trim()
        val expiryDate = etExpiryDate.text.toString().trim()

        // Validaciones simples
        if (cardHolderName.isEmpty() || cardNumber.isEmpty() || expiryDate.isEmpty()) {
            Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (cardNumber.length != 16) {
            etCardNumber.error = "El número de tarjeta debe tener 16 dígitos"
            return
        }

        // Lógica para determinar el tipo de tarjeta (simplificado)
        val cardType = when {
            cardNumber.startsWith("4") -> "Visa"
            cardNumber.startsWith("5") -> "Mastercard"
            else -> "Tarjeta"
        }

        // Llamamos al ViewModel para que añada el nuevo método de pago
        viewModel.addPaymentMethod(
            cardNumber = cardNumber,
            cardHolderName = cardHolderName,
            expiryDate = expiryDate,
            cardType = cardType
        )

        Toast.makeText(context, "Tarjeta guardada correctamente", Toast.LENGTH_LONG).show()

        // Volvemos a la pantalla anterior (PaymentMethodsFragment)
        parentFragmentManager.popBackStack()
    }
}
