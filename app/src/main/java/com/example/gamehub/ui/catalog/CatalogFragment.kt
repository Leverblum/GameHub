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

class CatalogFragment : Fragment() {

    // CAMBIO: Obtenemos la instancia del ViewModel compartida a nivel de actividad
    private val cartViewModel: CartViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    // private lateinit var prefsRepository: PrefsRepository // CAMBIO: Ya no se necesita aquí

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)

        // prefsRepository = PrefsRepository(requireContext()) // CAMBIO: Ya no se necesita aquí

        val sampleProducts = createSampleCatalogProducts()

        recyclerView = view.findViewById(R.id.rvProducts)

        // SOLUCIÓN: Pasamos el 'cartViewModel' al constructor del adaptador, que es lo que ahora espera.
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
            Product(1, "The Legend of Zelda: Breath of the Wild", 59.99, "url_imagen_1"),
            Product(2, "Red Dead Redemption 2", 49.99, "url_imagen_2"),
            Product(3, "Cyberpunk 2077", 39.99, "url_imagen_3"),
            Product(4, "Elden Ring", 59.99, "url_imagen_4"),
            Product(8, "Diablo IV", 69.99, "url_imagen_8"),
            Product(9, "Starfield", 69.99, "url_imagen_9"),
            Product(10, "Baldur's Gate 3", 59.99, "url_imagen_10"),
            Product(11, "Hogwarts Legacy", 69.99, "url_imagen_11")
        )
    }
}
