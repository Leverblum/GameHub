package com.example.gamehub.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gamehub.R
import com.example.gamehub.models.CartItem
import com.example.gamehub.models.Product
import com.example.gamehub.repository.PrefsRepository

class ProductDetailFragment : Fragment() {

    // Vistas del layout
    private lateinit var ivDetailImage: ImageView
    private lateinit var tvDetailTitle: TextView
    private lateinit var tvDetailPlatform: TextView
    private lateinit var tvDetailRating: TextView
    private lateinit var tvDetailPrice: TextView
    private lateinit var tvDetailDescription: TextView
    private lateinit var btnMinus: ImageButton
    private lateinit var btnPlus: ImageButton
    private lateinit var tvQuantity: TextView
    private lateinit var btnAddToCartDetail: Button

    // Propiedades de la clase
    private var productId: Int = -1
    private var product: Product? = null
    private lateinit var prefsRepository: PrefsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getInt("PRODUCT_ID", -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_detail, container, false)

        prefsRepository = PrefsRepository(requireContext())
        initializeViews(view)
        loadProductData()
        setupClickListeners()

        return view
    }

    private fun initializeViews(view: View) {
        ivDetailImage = view.findViewById(R.id.ivDetailImage)
        tvDetailTitle = view.findViewById(R.id.tvDetailTitle)
        tvDetailPlatform = view.findViewById(R.id.tvDetailPlatform)
        tvDetailRating = view.findViewById(R.id.tvDetailRating)
        tvDetailPrice = view.findViewById(R.id.tvDetailPrice)
        tvDetailDescription = view.findViewById(R.id.tvDetailDescription)
        btnMinus = view.findViewById(R.id.btnMinus)
        btnPlus = view.findViewById(R.id.btnPlus)
        tvQuantity = view.findViewById(R.id.tvQuantity)
        btnAddToCartDetail = view.findViewById(R.id.btnAddToCartDetail)
    }

    private fun loadProductData() {
        if (productId == -1) {
            Toast.makeText(context, "Error: Producto no encontrado", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
            return
        }

        val sampleProducts = mapOf(
            1 to Product(1, "The Legend of Zelda: Breath of the Wild", 59.99, "url_1", "Nintendo Switch • Aventura", "★ 4.8", "Una aventura épica en el vasto reino de Hyrule."),
            2 to Product(2, "Red Dead Redemption 2", 49.99, "url_2", "PlayStation 4 • Acción", "★ 4.9", "La historia de Arthur Morgan y la banda de Van der Linde."),
            3 to Product(3, "Cyberpunk 2077", 39.99, "url_3", "PC • RPG", "★ 4.2", "Explora Night City, una megalópolis obsesionada con el poder."),
            4 to Product(4, "Elden Ring", 59.99, "url_4", "PC • RPG de Acción", "★ 4.7", "Levántate, Sinluz, y que la gracia te guíe para abrazar el poder del Círculo de Elden.")
        )
        // Guardamos el producto encontrado en la variable de la clase
        this.product = sampleProducts[productId]

        if (product != null) {
            tvDetailTitle.text = product!!.name
            tvDetailPrice.text = "$${product!!.price}"
            tvDetailPlatform.text = product!!.platform
            tvDetailRating.text = product!!.rating
            tvDetailDescription.text = product!!.description
        }
    }

    private fun setupClickListeners() {
        btnMinus.setOnClickListener {
            var currentQuantity = tvQuantity.text.toString().toIntOrNull() ?: 1
            if (currentQuantity > 1) {
                currentQuantity--
                tvQuantity.text = currentQuantity.toString()
            }
        }

        btnPlus.setOnClickListener {
            var currentQuantity = tvQuantity.text.toString().toIntOrNull() ?: 1
            currentQuantity++
            tvQuantity.text = currentQuantity.toString()
        }

        btnAddToCartDetail.setOnClickListener {
            product?.let { prod ->
                val cartItems = prefsRepository.getCartItems()
                val selectedQuantity = tvQuantity.text.toString().toIntOrNull() ?: 1

                val existingItem = cartItems.find { it.product.id == prod.id }

                if (existingItem != null) {
                    existingItem.quantity += selectedQuantity
                } else {
                    val newCartItem = CartItem(product = prod, quantity = selectedQuantity)
                    cartItems.add(newCartItem)
                }

                prefsRepository.saveCartItems(cartItems)
                Toast.makeText(context, "'${prod.name}' añadido al carrito", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
