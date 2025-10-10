package com.example.gamehub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.models.Product // ¡Importamos el modelo correcto!
import java.text.NumberFormat
import java.util.Locale

class AdminProductAdapter(
    private val onEditClicked: (Product) -> Unit,
    private val onDeleteClicked: (Product) -> Unit
) : ListAdapter<Product, AdminProductAdapter.ProductViewHolder>(DiffCallback) {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvProductName)
        private val categoryTextView: TextView = itemView.findViewById(R.id.tvProductCategory)
        private val priceTextView: TextView = itemView.findViewById(R.id.tvProductPrice)
        private val imageView: ImageView = itemView.findViewById(R.id.ivProductImage)
        private val editButton: ImageButton = itemView.findViewById(R.id.btnEdit)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(product: Product, onEdit: (Product) -> Unit, onDelete: (Product) -> Unit) {
            nameTextView.text = product.name
            categoryTextView.text = product.category
            priceTextView.text = formatPrice(product.price.toDouble())

            // Lógica para cargar imagen (ej. con Glide/Coil)
            // Glide.with(itemView.context).load(product.imageUrl).into(imageView)

            editButton.setOnClickListener { onEdit(product) }
            deleteButton.setOnClickListener { onDelete(product) }
        }

        private fun formatPrice(price: Double): String {
            return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(price)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position), onEditClicked, onDeleteClicked)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}
