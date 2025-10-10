package com.example.gamehub.ui.stores

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.adapters.StoreAdapter
import com.example.gamehub.viewmodels.StoresViewModel

// Heredamos de Fragment y le pasamos el layout que va a usar.
class StoresFragment : Fragment(R.layout.fragment_stores) {

    // Creamos una instancia del ViewModel. Usamos 'by viewModels()' porque este ViewModel
    // no necesita ser compartido con otros fragmentos.
    private val storesViewModel: StoresViewModel by viewModels()

    // Declaramos las variables para el adapter y el RecyclerView.
    private lateinit var storeAdapter: StoreAdapter
    private lateinit var storesRecyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Vinculamos el RecyclerView desde nuestro layout.
        storesRecyclerView = view.findViewById(R.id.rvStores)

        // 2. Configuramos el RecyclerView y el Adapter.
        setupRecyclerView()

        // 3. Empezamos a observar los datos del ViewModel.
        observeViewModel()
    }

    private fun setupRecyclerView() {
        // Inicializamos el StoreAdapter.
        // Le pasamos la lógica que se ejecutará cuando se haga clic en el botón "Ver más".
        storeAdapter = StoreAdapter { store ->
            // Por ahora, solo mostramos un Toast con el nombre de la tienda.
            // En el futuro, aquí podría navegarse a una pantalla de detalle.
            Toast.makeText(
                context,
                "Ver más sobre: ${store.name}",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Asignamos el adapter y el layout manager a nuestro RecyclerView.
        storesRecyclerView.adapter = storeAdapter
        storesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        // Observamos la lista de tiendas que vive en el ViewModel.
        storesViewModel.stores.observe(viewLifecycleOwner) { storesList ->
            // Cuando la lista de tiendas cambia (o se carga por primera vez),
            // se la enviamos al adapter para que la muestre.
            storeAdapter.submitList(storesList)
        }
    }
}
