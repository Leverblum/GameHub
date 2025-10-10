package com.example.gamehub.ui.admin.catalog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.adapters.AdminProductAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminCatalogFragment : Fragment(R.layout.fragment_admin_products) {

    // Usamos 'by viewModels()' para obtener una instancia del ViewModel
    private val viewModel: AdminCatalogViewModel by viewModels()
    private lateinit var productAdapter: AdminProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rvProducts)
        val fabAddProduct: FloatingActionButton = view.findViewById(R.id.fabAddProduct)

        setupRecyclerView(recyclerView)
        observeViewModel()

        fabAddProduct.setOnClickListener {
            // Lógica para ir al formulario de añadir nuevo producto
            Toast.makeText(context, "Ir a formulario de nuevo producto", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        // Inicializamos el adapter pasándole las acciones para los botones
        productAdapter = AdminProductAdapter(
            onEditClicked = { product ->
                // Lógica para editar el producto
                Toast.makeText(context, "Editar: ${product.name}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClicked = { product ->
                // Lógica para borrar el producto
                Toast.makeText(context, "Borrar: ${product.name}", Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        // Observamos la lista de productos del ViewModel
        viewModel.products.observe(viewLifecycleOwner) { productsList ->
            // Cuando la lista cambia, la enviamos al adapter
            productAdapter.submitList(productsList)
        }
    }
}
