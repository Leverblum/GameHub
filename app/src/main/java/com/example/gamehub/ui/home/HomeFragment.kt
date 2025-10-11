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
import java.math.BigDecimal // 1. Importamos BigDecimal

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var latestProductsAdapter: ProductAdapter
    private lateinit var featuredProductsAdapter: ProductAdapter
    private lateinit var allProducts: List<Product>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allProducts = createSampleProducts() // Esta función ahora creará los productos con el nuevo formato

        // --- Configuración del RecyclerView para "Recomendados" ---
        val recyclerViewFeatured: RecyclerView = view.findViewById(R.id.rvFeatured)
        featuredProductsAdapter = ProductAdapter(allProducts.take(3), cartViewModel)
        featuredProductsAdapter.onProductClick = { product -> navigateToDetail(product) }
        recyclerViewFeatured.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewFeatured.adapter = featuredProductsAdapter

        // --- Configuración del RecyclerView para "Últimos Lanzamientos" ---
        val recyclerViewLatest: RecyclerView = view.findViewById(R.id.rvLatest)
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
        return listOf(
            Product(1, "The Legend of Zelda", "Aventura épica en Hyrule", BigDecimal("59.99"), 10, "Nintendo Switch", "url_zelda", true),
            Product(2, "Red Dead Redemption 2", "La historia de Arthur Morgan", BigDecimal("39.99"), 20, "PlayStation 4", "url_rdr2", true),
            Product(3, "Elden Ring", "Levántate, Sinluz", BigDecimal("59.99"), 15, "PC", "url_elden", true),
            Product(4, "Cyberpunk 2077", "Explora Night City", BigDecimal("49.99"), 25, "PC", "url_cyberpunk", true),
            Product(5, "God of War: Ragnarök", "El fin se acerca", BigDecimal("69.99"), 5, "PlayStation 5", "url_gow", true),
            Product(6, "Stardew Valley", "Crea la granja de tus sueños", BigDecimal("14.99"), 100, "PC", "url_stardew", true),
            Product(7, "Hollow Knight", "Aventura en un reino de insectos", BigDecimal("14.99"), 50, "PC", "url_hollow", true),
            Product(8, "Diablo IV", "El regreso de Lilith", BigDecimal("69.99"), 30, "PC", "url_imagen_8", true),
            Product(9, "Starfield", "Aventura espacial de Bethesda", BigDecimal("69.99"), 12, "Xbox", "url_imagen_9", true),
            Product(10, "Baldur's Gate 3", "RPG basado en D&D", BigDecimal("59.99"), 18, "PC", "url_imagen_10", true),
            Product(11, "Hogwarts Legacy", "Vive el mundo mágico", BigDecimal("69.99"), 22, "PlayStation 5", "url_imagen_11", true)
        )
    }
}
