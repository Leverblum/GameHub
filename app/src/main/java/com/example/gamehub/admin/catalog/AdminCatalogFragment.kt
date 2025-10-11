package com.example.gamehub.ui.admin.catalog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.adapters.AdminProductAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminCatalogFragment : Fragment(R.layout.fragment_admin_products) {

    // Usamos 'by activityViewModels()' para compartir la instancia del ViewModel
    private val viewModel: AdminCatalogViewModel by activityViewModels()
    private lateinit var productAdapter: AdminProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rvProducts)
        val fabAddProduct: FloatingActionButton = view.findViewById(R.id.fabAddProduct)

        setupRecyclerView(recyclerView)
        observeViewModel()

        // Navegación para CREAR un nuevo producto
        fabAddProduct.setOnClickListener {
            // Usamos newInstance() SIN argumentos para modo CREAR
            val formFragment = AdminProductFormFragment.newInstance()
            parentFragmentManager.beginTransaction()
                .replace(android.R.id.content, formFragment) // Asegúrate de que el ID del contenedor sea correcto
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        productAdapter = AdminProductAdapter(
            // Navegación para EDITAR un producto existente
            onEditClicked = { product ->
                // Usamos newInstance() CON el ID del producto para modo EDITAR
                val formFragment = AdminProductFormFragment.newInstance(product.id)
                parentFragmentManager.beginTransaction()
                    .replace(android.R.id.content, formFragment) // Asegúrate de que el ID del contenedor sea correcto
                    .addToBackStack(null)
                    .commit()
            },
            onDeleteClicked = { product ->
                // La lógica para borrar el producto irá aquí en el siguiente paso
                Toast.makeText(context, "Borrar: ${product.name}", Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        // Observamos la lista de productos del ViewModel
        viewModel.products.observe(viewLifecycleOwner) { productsList ->
            // Cuando la lista cambia, la enviamos al adapter para que actualice la UI
            productAdapter.submitList(productsList)
        }
    }
}
