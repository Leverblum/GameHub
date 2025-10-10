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
import com.example.gamehub.viewmodels.CartViewModel
import java.text.NumberFormat
import java.util.Locale

class CartAdapter(
    private var items: MutableList<CartItem>,
    private val cartViewModel: CartViewModel
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // ================== INICIO DE LA CORRECCIÓN ==================
    // ViewHolder ahora busca los IDs de 'item_cart_product.xml'
    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.ivCartProductImage)
        val itemTitle: TextView = itemView.findViewById(R.id.tvCartProductName)
        val itemPrice: TextView = itemView.findViewById(R.id.tvCartProductPrice)
        val itemQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val btnPlus: ImageButton = itemView.findViewById(R.id.btnPlus)
        val btnMinus: ImageButton = itemView.findViewById(R.id.btnMinus)
        val btnRemove: ImageButton = itemView.findViewById(R.id.btnRemoveItem)
    }
    // =================== FIN DE LA CORRECCIÓN ====================

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        // ======================= CAMBIO CLAVE =======================
        // Inflamos TU layout existente: 'item_cart_product.xml'.
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_product, parent, false)
        return CartViewHolder(view)
        // ==========================================================
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]

        holder.itemTitle.text = item.product.name
        holder.itemQuantity.text = item.quantity.toString()

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
        holder.itemPrice.text = currencyFormat.format(item.product.price)
        // holder.itemImage.load(item.product.imageUrl) // Aquí usarías Glide/Picasso

        // La lógica de los clics ahora apunta a los botones correctos
        holder.btnPlus.setOnClickListener {
            val newQuantity = item.quantity + 1
            cartViewModel.updateItemQuantity(item, newQuantity)
        }

        holder.btnMinus.setOnClickListener {
            val newQuantity = item.quantity - 1
            cartViewModel.updateItemQuantity(item, newQuantity)
        }

        holder.btnRemove.setOnClickListener {
            cartViewModel.removeItemFromCart(item)
        }
    }

    override fun getItemCount() = items.size

    /**
     * Este método es llamado por el observador en CartFragment para refrescar la lista.
     */
    fun updateItems(newItems: List<CartItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
