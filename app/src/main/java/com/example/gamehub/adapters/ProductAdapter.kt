package com.example.gamehub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.models.Product
import com.example.gamehub.viewmodels.CartViewModel
import com.google.android.material.card.MaterialCardView

/**
 * Adaptador corregido y sincronizado para funcionar con el layout 'item_game_card.xml'.
 */
class ProductAdapter(
    private var products: List<Product>,
    private val cartViewModel: CartViewModel
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    var onProductClick: ((Product) -> Unit)? = null

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root: MaterialCardView = itemView.findViewById(R.id.cardRoot)
        val productImage: ImageView = itemView.findViewById(R.id.ivGameImage)
        val productTitle: TextView = itemView.findViewById(R.id.tvGameTitle)
        val productPrice: TextView = itemView.findViewById(R.id.tvGamePrice)
        val addButton: ImageButton = itemView.findViewById(R.id.btnAddGame)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game_card, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.productTitle.text = product.name
        holder.productPrice.text = String.format("$%.2f", product.price.toDouble())

        holder.productImage.setImageResource(product.imageResId)

        holder.root.setOnClickListener {
            onProductClick?.invoke(product)
        }

        holder.addButton.setOnClickListener {
            if (product.stock > 0) {
                cartViewModel.addProductToCart(product)
                Toast.makeText(holder.itemView.context, "${product.name} añadido al carrito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(holder.itemView.context, "${product.name} está agotado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount() = products.size

    /**
     * Función para actualizar la lista de productos si cambia.
     */
    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
