package com.example.gamehub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.models.Store

// Este adaptador recibe una función lambda para manejar el clic en el botón "Ver más"
class StoreAdapter(
    private val onVerMasClicked: (Store) -> Unit
) : ListAdapter<Store, StoreAdapter.StoreViewHolder>(DiffCallback) {

    /**
     * El ViewHolder contiene las referencias a las vistas dentro de cada item (item_store.xml)
     * y una función 'bind' para asignarles los datos.
     */
    class StoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Vinculamos las vistas usando los IDs de tu layout item_store.xml
        private val nameTextView: TextView = itemView.findViewById(R.id.tvStoreName)
        private val addressTextView: TextView = itemView.findViewById(R.id.tvStoreAddress)
        private val distanceTextView: TextView = itemView.findViewById(R.id.tvStoreDistance)
        private val verMasButton: Button = itemView.findViewById(R.id.btnVerMas) // ID que he asumido para el botón

        // La función 'bind' asigna los datos de un objeto 'Store' a las vistas del ViewHolder
        fun bind(store: Store, onVerMasClicked: (Store) -> Unit) {
            nameTextView.text = store.name
            addressTextView.text = store.address
            distanceTextView.text = store.distance

            // Configuramos el listener para el botón "Ver más" de este item
            verMasButton.setOnClickListener {
                onVerMasClicked(store)
            }
        }
    }

    /**
     * Se llama cuando el RecyclerView necesita crear un nuevo ViewHolder (una nueva fila).
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        // Inflamos tu layout 'item_store.xml' para crear la vista de la fila
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_store, parent, false)
        return StoreViewHolder(view)
    }

    /**
     * Se llama para mostrar los datos en una posición específica.
     * Reutiliza los ViewHolders para ser eficiente.
     */
    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val currentStore = getItem(position)
        // Llamamos a la función 'bind' de nuestro ViewHolder para poblar las vistas
        holder.bind(currentStore, onVerMasClicked)
    }

    /**
     * DiffUtil es una herramienta de optimización. Ayuda al RecyclerView a saber exactamente
     * qué items de la lista han cambiado, evitando redibujar toda la lista innecesariamente.
     */
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Store>() {
            override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
                // Compara si dos items representan el mismo objeto (por su ID único)
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
                // Compara si el contenido de los items es idéntico
                return oldItem == newItem
            }
        }
    }
}
