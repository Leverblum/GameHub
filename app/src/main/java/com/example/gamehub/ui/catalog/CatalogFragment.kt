package com.example.gamehub.ui.catalog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.adapters.ProductAdapter
import com.example.gamehub.models.Product
import com.example.gamehub.utils.NavigationListener

class CatalogFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private var navigationListener: NavigationListener? = null

    // El fragment "se adjunta" a la actividad. Aquí es donde obtenemos el listener.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NavigationListener) {
            navigationListener = context
        } else {
            throw RuntimeException("$context must implement NavigationListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)

        // 1. Datos de ejemplo (más adelante vendrán de un ViewModel)
        val sampleProducts = listOf(
            Product(1, "The Legend of Zelda: Breath of the Wild", 59.99, "url_imagen_1"),
            Product(2, "Red Dead Redemption 2", 49.99, "url_imagen_2"),
            Product(3, "Cyberpunk 2077", 39.99, "url_imagen_3"),
            Product(4, "Elden Ring", 59.99, "url_imagen_4")
        )

        // 2. Inicializar el RecyclerView y el Adapter
        recyclerView = view.findViewById(R.id.rvProducts)
        // Aseguramos que el listener no es nulo antes de pasarlo
        productAdapter = ProductAdapter(sampleProducts, navigationListener!!)

        // 3. Configurar el LayoutManager del RecyclerView
        // GridLayoutManager muestra los items en una cuadrícula. ¡Cámbialo si prefieres una lista vertical!
        recyclerView.layoutManager = GridLayoutManager(context, 2) // 2 columnas
        recyclerView.adapter = productAdapter

        return view
    }

    // Es buena práctica limpiar la referencia al listener cuando el fragment se "des-adjunta".
    override fun onDetach() {
        super.onDetach()
        navigationListener = null
    }
}
