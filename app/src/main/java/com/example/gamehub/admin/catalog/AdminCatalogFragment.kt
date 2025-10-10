package com.example.gamehub.ui.admin.catalog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels // 1. CAMBIO: Importa activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.adapters.AdminProductAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminCatalogFragment : Fragment(R.layout.fragment_admin_products) {

    // ================== INICIO DE LA CORRECCIÓN #1 ==================
    // Usamos 'by activityViewModels()' para compartir la instancia del ViewModel con otros fragmentos
    private val viewModel: AdminCatalogViewModel by activityViewModels()
    // =================== FIN DE LA CORRECCIÓN #1 ====================

    private lateinit var productAdapter: AdminProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rvProducts)
        val fabAddProduct: FloatingActionButton = view.findViewById(R.id.fabAddProduct)

        setupRecyclerView(recyclerView)
        observeViewModel()

        // ================== INICIO DE LA CORRECCIÓN #2 ==================
        // Cambiamos el Toast por la lógica de navegación al formulario
        fabAddProduct.setOnClickListener {
            // Reemplazamos el contenedor actual con el fragmento del formulario
            // NOTA: Si este fragmento se muestra dentro de AdminActivity, el ID del contenedor
            // que definimos era android.R.id.content. Si es otro, ajústalo aquí.
            parentFragmentManager.beginTransaction()
                .replace(android.R.id.content, AdminProductFormFragment())
                .addToBackStack(null) // Esto permite que el botón "Atrás" regrese a esta lista
                .commit()
        }
        // =================== FIN DE LA CORRECCIÓN #2 ====================
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        // Inicializamos el adapter pasándole las acciones para los botones
        productAdapter = AdminProductAdapter(
            onEditClicked = { product ->
                // Lógica para editar el producto
                // En el futuro, esto navegará al formulario pasándole el ID del producto
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
            // Cuando la lista cambia, la enviamos al adapter para que se actualice la UI
            productAdapter.submitList(productsList)
        }
    }
}
