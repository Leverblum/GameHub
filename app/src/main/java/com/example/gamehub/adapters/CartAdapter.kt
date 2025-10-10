package com.example.gamehub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.models.CartItem
import com.example.gamehub.repository.PrefsRepository
import com.example.gamehub.utils.CartListener

class CartAdapter(
    private var cartItems: MutableList<CartItem>,
    private val listener: CartListener,
    private val prefsRepository: PrefsRepository
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.ivCartProductImage)
        val productName: TextView = itemView.findViewById(R.id.tvCartProductName)
        val productPrice: TextView = itemView.findViewById(R.id.tvCartProductPrice)
        val quantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val btnMinus: ImageButton = itemView.findViewById(R.id.btnMinus)
        val btnPlus: ImageButton = itemView.findViewById(R.id.btnPlus)
        val btnRemove: ImageButton = itemView.findViewById(R.id.btnRemoveItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_product, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]

        holder.productName.text = cartItem.product.name
        holder.productPrice.text = "$${cartItem.product.price}"
        holder.quantity.text = cartItem.quantity.toString()
        // Aquí cargarías la imagen del producto con Glide/Picasso

        holder.btnMinus.setOnClickListener {
            if (cartItem.quantity > 1) {
                cartItem.quantity--
                holder.quantity.text = cartItem.quantity.toString()
                prefsRepository.saveCartItems(cartItems)
                listener.onQuantityChanged()
            }
        }

        holder.btnPlus.setOnClickListener {
            cartItem.quantity++
            holder.quantity.text = cartItem.quantity.toString()
            prefsRepository.saveCartItems(cartItems)
            listener.onQuantityChanged()
        }

        holder.btnRemove.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                val itemToRemove = cartItems[currentPosition]
                cartItems.removeAt(currentPosition)
                prefsRepository.saveCartItems(cartItems)
                notifyItemRemoved(currentPosition)
                listener.onItemRemoved(itemToRemove)
            }
        }
    }
}
