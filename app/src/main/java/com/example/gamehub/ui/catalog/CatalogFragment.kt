package com.example.gamehub.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels // CAMBIO: Import para obtener el ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.adapters.ProductAdapter
import com.example.gamehub.models.Product
import com.example.gamehub.ui.detail.GameDetailFragment
import com.example.gamehub.viewmodels.CartViewModel // CAMBIO: Import del ViewModel
import java.math.BigDecimal

class CatalogFragment : Fragment() {

    private val cartViewModel: CartViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    // private lateinit var prefsRepository: PrefsRepository // Ya no se necesita aquí

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)

        // prefsRepository = PrefsRepository(requireContext()) // Ya no se necesita aquí

        val sampleProducts = createSampleCatalogProducts()

        recyclerView = view.findViewById(R.id.rvProducts)

        productAdapter = ProductAdapter(sampleProducts, cartViewModel)

        // El propio adapter ya maneja el clic del botón '+', así que esta parte sigue igual.
        // Solo necesitamos decirle qué hacer cuando se pulsa en la tarjeta para ir al detalle.
        productAdapter.onProductClick = { product ->
            val detailFragment = GameDetailFragment.newInstance(product.id)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = productAdapter

        return view
    }

    private fun createSampleCatalogProducts(): List<Product> {
        return listOf(
            Product(1, "The Legend of Zelda: Breath of the Wild", "", BigDecimal(59.99), 10,"","url_imagen_1"),
            Product(2, "Red Dead Redemption 2","", BigDecimal(49.99),10,"", "url_imagen_2"),
            Product(3, "Cyberpunk 2077","", BigDecimal(39.99),10,"", "url_imagen_3"),
            Product(4, "Elden Ring","", BigDecimal(59.99),10, "","url_imagen_4"),
            Product(8, "Diablo IV","", BigDecimal(69.99),10, "","url_imagen_8"),
            Product(9, "Starfield","", BigDecimal(69.99),10, "","url_imagen_9"),
            Product(10, "Baldur's Gate 3","", BigDecimal(59.99),10, "","url_imagen_10"),
            Product(11, "Hogwarts Legacy","", BigDecimal(69.99) , 10,"","url_imagen_11")
        )
    }
}
