package com.example.gamehub.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.adapters.CartAdapter
import com.example.gamehub.models.CartItem
import com.example.gamehub.ui.checkout.CheckoutFragment
import com.example.gamehub.viewmodels.CartViewModel
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale
import kotlin.collections.sumOf

class CartFragment : Fragment() {

    // CAMBIO: Obtenemos la instancia del ViewModel compartida
    private val cartViewModel: CartViewModel by activityViewModels()

    private lateinit var rvCartItems: RecyclerView
    private lateinit var tvSubtotal: TextView
    private lateinit var tvTaxes: TextView
    private lateinit var tvTotal: TextView
    private lateinit var btnCheckout: Button
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        initializeViews(view)
        setupRecyclerView()
        setupClickListeners()

        // CAMBIO CLAVE: Nos suscribimos a los cambios del carrito
        observeCartChanges()

        return view
    }

    private fun initializeViews(view: View) {
        rvCartItems = view.findViewById(R.id.rvCartItems)
        tvSubtotal = view.findViewById(R.id.tvSubtotal)
        tvTaxes = view.findViewById(R.id.tvTaxes)
        tvTotal = view.findViewById(R.id.tvTotal)
        btnCheckout = view.findViewById(R.id.btnCheckout)
    }

    private fun setupRecyclerView() {
        // El adaptador necesita recibir el ViewModel para que los botones de +/- dentro del carrito funcionen
        // Asumimos que el constructor de CartAdapter se ha ajustado para esto
        cartAdapter = CartAdapter(mutableListOf(), cartViewModel)
        rvCartItems.layoutManager = LinearLayoutManager(context)
        rvCartItems.adapter = cartAdapter
    }

    private fun observeCartChanges() {
        // Esto se ejecuta inmediatamente y cada vez que la lista en el ViewModel cambia
        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            // 1. Actualiza la lista en el adaptador
            cartAdapter.updateItems(items)

            // 2. Actualiza los totales en la UI
            updateTotalsUI(items)
        }
    }

    private fun updateTotalsUI(items: List<CartItem>) {
        val subtotal = items.sumOf {
            it.product.price.multiply(it.quantity.toBigDecimal())
        }
        val taxes = subtotal.multiply(BigDecimal("0.19"))

        val total = subtotal + taxes

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
        tvSubtotal.text = currencyFormat.format(subtotal)
        tvTaxes.text = currencyFormat.format(taxes)
        tvTotal.text = currencyFormat.format(total)
    }

    private fun setupClickListeners() {
        btnCheckout.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CheckoutFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
