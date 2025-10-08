package com.example.gamehub.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.adapters.CartAdapter
import com.example.gamehub.models.Product


class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var adapter: CartAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvCartItems)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Productos de prueba
        val demoProducts = listOf(
            Product(1, "Control DualSense", 299.99, 1, R.drawable.ic_gamepad),
            Product(2, "The Last of Us Part II", 79.99, 1, R.drawable.ic_catalog),
            Product(3, "Auriculares Pulse 3D", 129.99, 2, R.drawable.ic_add)
        )

        adapter = CartAdapter(demoProducts)
        recyclerView.adapter = adapter
    }
}
