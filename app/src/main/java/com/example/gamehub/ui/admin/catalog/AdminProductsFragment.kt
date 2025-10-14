package com.example.gamehub.ui.admin.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.models.Product
import com.example.gamehub.repository.ProductsRepository

class AdminProductsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddProduct: Button
    private lateinit var etSearchProduct: EditText
    private lateinit var adapter: AdminProductAdapter
    private lateinit var productsRepository: ProductsRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_products, container, false)

        recyclerView = view.findViewById(R.id.rvProducts)
        btnAddProduct = view.findViewById(R.id.btnAddProduct)
        etSearchProduct = view.findViewById(R.id.etSearchProduct)
        productsRepository = ProductsRepository(requireContext())

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadProducts()

        // 🔍 Filtrar productos en tiempo real
        etSearchProduct.addTextChangedListener {
            val query = it.toString()
            val filtered = productsRepository.getProducts().filter { product ->
                product.name.contains(query, ignoreCase = true)
            }
            adapter.updateList(filtered)
        }

        // ➕ Abrir formulario de nuevo producto
        btnAddProduct.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.adminFragmentContainer, AdminProductFormFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun loadProducts() {
        val products = productsRepository.getProducts()

        if (products.isEmpty()) {
            Toast.makeText(requireContext(), "No hay productos registrados", Toast.LENGTH_SHORT).show()
        }

        adapter = AdminProductAdapter(
            products = products,
            onEdit = { product ->
                // 📝 Editar producto
                val fragment = AdminProductFormFragment.newInstance(product.id)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.adminFragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            },
            onDelete = { product ->
                // 🗑️ Eliminar producto
                productsRepository.deleteProduct(product.id)
                loadProducts()
            }
        )

        recyclerView.adapter = adapter
    }
}
