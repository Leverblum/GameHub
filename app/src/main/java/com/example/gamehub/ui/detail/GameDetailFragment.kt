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

class GameDetailFragment : Fragment(R.layout.fragment_product_detail) {

    // CAMBIO: Obtenemos la instancia del ViewModel compartida
    private val cartViewModel: CartViewModel by activityViewModels()

    private var currentProduct: Product? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recuperamos el ID del producto y lo buscamos en nuestra lista de ejemplo
        val productId = arguments?.getInt(ARG_PRODUCT_ID)
        if (productId != null) {
            currentProduct = createSampleProducts().find { it.id == productId }
        }

        // Vinculamos las vistas
        val titleTextView: TextView = view.findViewById(R.id.tvDetailTitle)
        val priceTextView: TextView = view.findViewById(R.id.tvDetailPrice)
        val descriptionTextView: TextView = view.findViewById(R.id.tvDetailDescription)
        val imageView: ImageView = view.findViewById(R.id.ivDetailImage)
        val addToCartButton: Button = view.findViewById(R.id.btnAddToCartDetail)

        currentProduct?.let { product ->
            titleTextView.text = product.name
            priceTextView.text = "$${product.price}"
            descriptionTextView.text = "Esta es una descripción detallada para ${product.name} que viene de una fuente de datos."
            // imageView.load(product.imageUrl) // Aquí usarías Glide o Picasso

            addToCartButton.setOnClickListener {
                // CAMBIO: Llama al método centralizado del ViewModel
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
        // Esta lista debe ser idéntica a la del HomeFragment para encontrar el producto por ID
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
