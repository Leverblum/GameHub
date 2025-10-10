package com.example.gamehub.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.gamehub.R
// NUEVOS IMPORTS
import com.example.gamehub.models.Order
import com.example.gamehub.repository.PrefsRepository
import com.example.gamehub.ui.home.HomeFragment
import java.util.Date
import java.util.UUID

class CheckoutFragment : Fragment() {

    private lateinit var etAddress: EditText
    private lateinit var spinnerPayment: Spinner
    private lateinit var btnConfirmPurchase: Button
    private lateinit var prefsRepository: PrefsRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_checkout, container, false)

        prefsRepository = PrefsRepository(requireContext())
        initializeViews(view)
        setupPaymentSpinner()
        setupConfirmButton()

        return view
    }

    private fun initializeViews(view: View) {
        etAddress = view.findViewById(R.id.etAddress)
        spinnerPayment = view.findViewById(R.id.spinnerPayment)
        btnConfirmPurchase = view.findViewById(R.id.btnConfirmPurchase)
    }

    private fun setupPaymentSpinner() {
        val paymentMethods = listOf("Tarjeta de Crédito", "PayPal", "Transferencia Bancaria")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            paymentMethods
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPayment.adapter = adapter
    }

    private fun setupConfirmButton() {
        btnConfirmPurchase.setOnClickListener {
            val address = etAddress.text.toString().trim()
            if (address.isEmpty()) {
                etAddress.error = "La dirección de envío es obligatoria"
                Toast.makeText(context, "Por favor, introduce una dirección de envío.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showConfirmationDialog(address, spinnerPayment.selectedItem.toString())
        }
    }

    private fun showConfirmationDialog(address: String, paymentMethod: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Compra")
            .setMessage("¿Estás seguro de que deseas realizar la compra?\n\nDirección: $address\nMétodo de pago: $paymentMethod")
            .setPositiveButton("Confirmar") { dialog, _ ->
                processPurchase()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun processPurchase() {
        // --- LÓGICA DE PERSISTENCIA DEL PEDIDO ---

        // 1. Obtener los items del carrito que se van a comprar
        val itemsToPurchase = prefsRepository.getCartItems()
        if (itemsToPurchase.isEmpty()) {
            Toast.makeText(context, "Tu carrito está vacío.", Toast.LENGTH_SHORT).show()
            return // No procesar si no hay nada que comprar
        }

        // 2. Calcular el total del pedido
        val total = itemsToPurchase.sumOf { it.product.price * it.quantity }

        // 3. Crear el nuevo objeto Order
        val newOrder = Order(
            id = UUID.randomUUID().toString().substring(0, 6).uppercase(), // ID de pedido aleatorio y corto
            date = Date(), // Fecha y hora actual
            status = "Procesando", // Estado inicial
            total = total,
            items = itemsToPurchase
        )

        // 4. Obtener la lista actual de pedidos y añadir el nuevo
        val allOrders = prefsRepository.getOrders()
        allOrders.add(newOrder)
        prefsRepository.saveOrders(allOrders)

        // 5. Limpiar el carrito
        prefsRepository.clearCart()

        // --- FIN DE LA LÓGICA DE PERSISTENCIA ---

        Toast.makeText(context, "¡Compra realizada con éxito! Pedido #${newOrder.id}", Toast.LENGTH_LONG).show()

        // Navegar a la pantalla de inicio
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()
    }
}
