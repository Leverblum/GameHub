package com.example.gamehub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.models.PaymentMethod

class PaymentMethodAdapter(
    private val onItemClicked: (PaymentMethod) -> Unit,
    private val onDeleteClicked: (PaymentMethod) -> Unit
) : ListAdapter<PaymentMethod, PaymentMethodAdapter.PaymentViewHolder>(DiffCallback) {
// =================== FIN DEL CAMBIO ====================

    // El ViewHolder ahora también tiene una referencia al botón de borrar.
    class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconView: ImageView = itemView.findViewById(R.id.img_payment_icon)
        private val nameView: TextView = itemView.findViewById(R.id.tv_payment_name)
        val deleteButton: ImageView = itemView.findViewById(R.id.btn_delete_payment) // <-- Referencia al botón

        fun bind(paymentMethod: PaymentMethod) {
            nameView.text = "${paymentMethod.cardType} **** ${paymentMethod.cardNumber.takeLast(4)}"

            // He mejorado esta lógica para que uses tus futuros íconos si los tienes
            val iconRes = when {
                paymentMethod.cardNumber.startsWith("4") -> R.drawable.ic_card
                paymentMethod.cardNumber.startsWith("5") -> R.drawable.ic_card
                else -> R.drawable.ic_card
            }
            iconView.setImageResource(iconRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_payment_method, parent, false)
        return PaymentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val current = getItem(position)

        // Asignamos el listener a toda la fila
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }

        // Asignamos el listener específico al botón de eliminar
        holder.deleteButton.setOnClickListener {
            onDeleteClicked(current)
        }

        holder.bind(current)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<PaymentMethod>() {
            override fun areItemsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean {
                return oldItem == newItem
            }
        }
    }
}
