package com.example.gamehub.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.adapters.ProductAdapter
import com.example.gamehub.models.Product
import com.example.gamehub.ui.detail.GameDetailFragment
import com.example.gamehub.viewmodels.CartViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    // CAMBIO: Obtenemos la instancia del ViewModel compartida a nivel de actividad
    private val cartViewModel: CartViewModel by activityViewModels()

    private lateinit var latestProductsAdapter: ProductAdapter
    private lateinit var featuredProductsAdapter: ProductAdapter
    private lateinit var allProducts: List<Product>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allProducts = createSampleProducts()

        // --- Configuración del RecyclerView para "Recomendados" ---
        val recyclerViewFeatured: RecyclerView = view.findViewById(R.id.rvFeatured)
        // CAMBIO: Pasamos el ViewModel al adaptador
        featuredProductsAdapter = ProductAdapter(allProducts.take(3), cartViewModel)
        featuredProductsAdapter.onProductClick = { product -> navigateToDetail(product) }
        recyclerViewFeatured.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewFeatured.adapter = featuredProductsAdapter

        // --- Configuración del RecyclerView para "Últimos Lanzamientos" ---
        val recyclerViewLatest: RecyclerView = view.findViewById(R.id.rvLatest)
        // CAMBIO: Pasamos el ViewModel también a este adaptador
        latestProductsAdapter = ProductAdapter(allProducts, cartViewModel)
        latestProductsAdapter.onProductClick = { product -> navigateToDetail(product) }
        recyclerViewLatest.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewLatest.adapter = latestProductsAdapter

        // --- Configuración del Buscador ---
        val searchEditText: EditText = view.findViewById(R.id.etSearchHome)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProducts(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun navigateToDetail(product: Product) {
        val detailFragment = GameDetailFragment.newInstance(product.id)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun filterProducts(query: String) {
        val filteredList = if (query.isEmpty()) {
            allProducts
        } else {
            allProducts.filter { product -> product.name.contains(query, ignoreCase = true) }
        }
        latestProductsAdapter.updateProducts(filteredList)
    }

    private fun createSampleProducts(): List<Product> {
        // Asegúrate de que esta lista contenga todos los productos posibles de tu app
        return listOf(
            Product(1, "The Legend of Zelda", 59.99, "url_zelda"),
            Product(2, "Red Dead Redemption 2", 39.99, "url_rdr2"),
            Product(3, "Elden Ring", 59.99, "url_elden"),
            Product(4, "Cyberpunk 2077", 49.99, "url_cyberpunk"),
            Product(5, "God of War: Ragnarök", 69.99, "url_gow"),
            Product(6, "Stardew Valley", 14.99, "url_stardew"),
            Product(7, "Hollow Knight", 14.99, "url_hollow"),
            Product(8, "Diablo IV", 69.99, "url_imagen_8"),
            Product(9, "Starfield", 69.99, "url_imagen_9"),
            Product(10, "Baldur's Gate 3", 59.99, "url_imagen_10"),
            Product(11, "Hogwarts Legacy", 69.99, "url_imagen_11")
        )
    }
}
