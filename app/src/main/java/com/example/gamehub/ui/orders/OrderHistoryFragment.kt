package com.example.gamehub.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.adapters.OrderAdapter
// IMPORTA EL REPOSITORIO
import com.example.gamehub.repository.PrefsRepository

class OrderHistoryFragment : Fragment() {

    private lateinit var rvOrders: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    // DECLARA EL REPOSITORIO
    private lateinit var prefsRepository: PrefsRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_orders, container, false)

        // INICIALIZA EL REPOSITORIO
        prefsRepository = PrefsRepository(requireContext())

        rvOrders = view.findViewById(R.id.rvOrders)

        // YA NO CREAMOS DATOS DE EJEMPLO, CONFIGURAMOS DIRECTAMENTE
        setupRecyclerView()

        return view
    }

    // ELIMINA EL MÉTODO createSampleData()

    private fun setupRecyclerView() {
        // 1. Obtenemos los pedidos guardados desde SharedPreferences
        val savedOrders = prefsRepository.getOrders()

        // 2. Creamos una instancia del adaptador con los datos reales
        orderAdapter = OrderAdapter(savedOrders.reversed()) // .reversed() para mostrar el más reciente primero

        // 3. Configuramos el RecyclerView
        rvOrders.layoutManager = LinearLayoutManager(context)
        rvOrders.adapter = orderAdapter
    }
}
