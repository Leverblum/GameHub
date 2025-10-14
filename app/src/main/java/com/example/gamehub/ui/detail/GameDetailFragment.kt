package com.example.gamehub.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.gamehub.R
import com.example.gamehub.models.Product
import com.example.gamehub.viewmodels.CartViewModel
import java.math.BigDecimal // 1. Importamos BigDecimal

class GameDetailFragment : Fragment(R.layout.fragment_product_detail) {

    private val cartViewModel: CartViewModel by activityViewModels()
    private var currentProduct: Product? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productId = arguments?.getInt(ARG_PRODUCT_ID)
        if (productId != null) {
            // Usamos la misma función corregida para buscar el producto
            currentProduct = createSampleProducts().find { it.id == productId }
        }

        val titleTextView: TextView = view.findViewById(R.id.tvDetailTitle)
        val priceTextView: TextView = view.findViewById(R.id.tvDetailPrice)
        val descriptionTextView: TextView = view.findViewById(R.id.tvDetailDescription)
        val imageView: ImageView = view.findViewById(R.id.ivDetailImage)
        val addToCartButton: Button = view.findViewById(R.id.btnAddToCartDetail)

        currentProduct?.let { product ->
            titleTextView.text = product.name
            // Usamos .toDouble() para compatibilidad si la lógica de formato de texto lo requiere,
            // pero es mejor formatear desde BigDecimal si es posible.
            priceTextView.text = "$${product.price.toString()}"
            descriptionTextView.text = product.description // Usamos la descripción real del producto
            // imageView.load(product.imageUrl)

            addToCartButton.setOnClickListener {
                cartViewModel.addProductToCart(product)
                Toast.makeText(context, "${product.name} añadido al carrito", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            titleTextView.text = "Producto no encontrado"
            addToCartButton.isEnabled = false
        }
    }

    companion object {
        private const val ARG_PRODUCT_ID = "product_id"
        fun newInstance(productId: Int): GameDetailFragment {
            val fragment = GameDetailFragment()
            val args = Bundle()
            args.putInt(ARG_PRODUCT_ID, productId)
            fragment.arguments = args
            return fragment
        }
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
