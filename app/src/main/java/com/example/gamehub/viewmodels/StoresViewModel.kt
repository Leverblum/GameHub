package com.example.gamehub.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamehub.models.Store

class StoresViewModel : ViewModel() {

    // LiveData que contendrá la lista de tiendas y que el Fragment observará
    private val _stores = MutableLiveData<List<Store>>()
    val stores: LiveData<List<Store>> = _stores

    // El bloque init se ejecuta cuando el ViewModel es creado por primera vez
    init {
        // Cargamos los datos quemados inmediatamente
        loadStores()
    }

    private fun loadStores() {
        val hardcodedStores = listOf(
            Store(
                id = 1,
                name = "GameHub - Centro",
                address = "Calle Falsa 123, Centro Ciudad",
                distance = "1.2 km"
            ),
            Store(
                id = 2,
                name = "GameHub - Plaza Norte",
                address = "Centro Comercial Plaza Norte, Local 34",
                distance = "3.5 km"
            ),
            Store(
                id = 3,
                name = "Player 1 Start",
                address = "Av. de los Gamers 45, Sector Arcade",
                distance = "4.1 km"
            ),
            Store(
                id = 4,
                name = "Retro Zone",
                address = "Pasaje Nostalgia 8-bit, Casco Antiguo",
                distance = "6.8 km"
            ),
            Store(
                id = 5,
                name = "La Cueva del Gamer",
                address = "Subsuelo Comercial, Local 12",
                distance = "7.0 km"
            )
        )

        // Asignamos la lista a nuestro LiveData.
        // Cualquier Fragment que esté observando 'stores' será notificado de este cambio.
        _stores.value = hardcodedStores
    }
}
