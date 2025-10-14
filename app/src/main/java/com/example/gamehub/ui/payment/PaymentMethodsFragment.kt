package com.example.gamehub.ui.payment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.adapters.PaymentMethodAdapter
import com.example.gamehub.viewmodels.PaymentViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PaymentMethodsFragment : Fragment(R.layout.fragment_payment_methods) {

    private val viewModel: PaymentViewModel by activityViewModels()

    private lateinit var paymentAdapter: PaymentMethodAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddCard: FloatingActionButton
    private lateinit var tvNoPaymentMethods: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvPaymentMethods)
        fabAddCard = view.findViewById(R.id.fabAddCard)
        tvNoPaymentMethods = view.findViewById(R.id.tvNoPaymentMethodsMessage)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        paymentAdapter = PaymentMethodAdapter(
            onItemClicked = { paymentMethod ->
                // Lógica para el clic en la fila (la misma que tenías)
                Toast.makeText(context, "Tarjeta seleccionada: ${paymentMethod.cardType}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClicked = { paymentMethod ->
                viewModel.deletePaymentMethod(paymentMethod)
                Toast.makeText(context, "Método de pago eliminado", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.adapter = paymentAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupListeners() {
        fabAddCard.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CardFormFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun observeViewModel() {
        viewModel.paymentMethods.observe(viewLifecycleOwner) { methods ->
            paymentAdapter.submitList(methods)
            if (methods.isNullOrEmpty()) {
                recyclerView.visibility = View.GONE
                tvNoPaymentMethods.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                tvNoPaymentMethods.visibility = View.GONE
            }
        }
    }
}
