package com.example.gamehub.ui.checkout

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.gamehub.R
import com.example.gamehub.ui.home.HomeFragment
import com.example.gamehub.viewmodels.CartViewModel
import com.example.gamehub.viewmodels.CheckoutViewModel

class CheckoutFragment : Fragment(R.layout.fragment_checkout) {

    // Obtenemos las instancias de los ViewModels compartidos
    private val checkoutViewModel: CheckoutViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    private lateinit var etAddress: EditText
    private lateinit var spinnerPayment: Spinner
    private lateinit var btnConfirmPurchase: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupPaymentSpinner()
        setupConfirmButton()

        // Observador para reaccionar cuando la compra se completa
        checkoutViewModel.orderCompletedEvent.observe(viewLifecycleOwner) { hasCompleted ->
            if (hasCompleted) {
                Toast.makeText(requireContext(), "¡Compra realizada con éxito!", Toast.LENGTH_LONG).show()

                // Navegamos al Home y limpiamos la pila de navegación para que no pueda volver al checkout
                parentFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment())
                    .commit()

                // Reseteamos el evento en el ViewModel
                checkoutViewModel.onOrderCompletedEventHandled()
            }
        }
    }

    private fun initializeViews(view: View) {
        etAddress = view.findViewById(R.id.etAddress)
        spinnerPayment = view.findViewById(R.id.spinnerPayment)
        // El ID de tu botón en el layout anterior era btnConfirmPurchase, lo mantenemos por consistencia
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
                // La única acción aquí es llamar al método del ViewModel.
                // Le pasamos el cartViewModel para que pueda acceder a los items del carrito.
                checkoutViewModel.completeCheckout(cartViewModel)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
