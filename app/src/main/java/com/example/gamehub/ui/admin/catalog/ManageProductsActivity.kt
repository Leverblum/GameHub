package com.example.gamehub.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.adapters.ProductAdapter
import com.example.gamehub.repository.PrefsRepository
import com.example.gamehub.viewmodels.CartViewModel

class ManageProductsFragment : Fragment() {

    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var prefsRepository: PrefsRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manage_products, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewManageProducts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        prefsRepository = PrefsRepository(requireContext())

        val cartItems = prefsRepository.getCartItems()
        val products = cartItems.map { it.product }

        if (products.isEmpty()) {
            Toast.makeText(requireContext(), "No tienes productos registrados", Toast.LENGTH_SHORT).show()
        } else {
            productAdapter = ProductAdapter(products, cartViewModel)
            recyclerView.adapter = productAdapter
        }

        return view
    }
}
