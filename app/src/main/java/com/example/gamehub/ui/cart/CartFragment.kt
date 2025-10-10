package com.example.gamehub.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.adapters.CartAdapter
import com.example.gamehub.models.CartItem
import com.example.gamehub.repository.PrefsRepository
import com.example.gamehub.ui.checkout.CheckoutFragment
import com.example.gamehub.utils.CartListener
import java.text.NumberFormat
import java.util.Locale

class CartFragment : Fragment(), CartListener {

    private lateinit var rvCartItems: RecyclerView
    private lateinit var tvSubtotal: TextView
    private lateinit var tvTaxes: TextView
    private lateinit var tvTotal: TextView
    private lateinit var btnCheckout: Button

    private lateinit var cartAdapter: CartAdapter
    private lateinit var prefsRepository: PrefsRepository
    private var cartItems = mutableListOf<CartItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        prefsRepository = PrefsRepository(requireContext())
        initializeViews(view)
        loadCartData()
        setupRecyclerView()
        setupClickListeners()
        updateTotals()

        return view
    }

    private fun initializeViews(view: View) {
        rvCartItems = view.findViewById(R.id.rvCartItems)
        tvSubtotal = view.findViewById(R.id.tvSubtotal)
        tvTaxes = view.findViewById(R.id.tvTaxes)
        tvTotal = view.findViewById(R.id.tvTotal)
        btnCheckout = view.findViewById(R.id.btnCheckout)
    }

    private fun loadCartData() {
        cartItems = prefsRepository.getCartItems()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(cartItems, this, prefsRepository)
        rvCartItems.layoutManager = LinearLayoutManager(context)
        rvCartItems.adapter = cartAdapter
    }

    private fun setupClickListeners() {
        btnCheckout.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CheckoutFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun updateTotals() {
        val subtotal = cartItems.sumOf { it.product.price * it.quantity }
        val taxes = subtotal * 0.19 // Simulamos un 19% de impuestos
        val total = subtotal + taxes

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
        tvSubtotal.text = currencyFormat.format(subtotal)
        tvTaxes.text = currencyFormat.format(taxes)
        tvTotal.text = currencyFormat.format(total)
    }

    override fun onQuantityChanged() {
        updateTotals()
    }

    override fun onItemRemoved(item: CartItem) {
        updateTotals()
    }
}
