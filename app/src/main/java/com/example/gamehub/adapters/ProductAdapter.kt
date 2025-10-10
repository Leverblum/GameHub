package com.example.gamehub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.models.Product
import com.example.gamehub.utils.NavigationListener

// El Adapter recibe una lista de productos y el listener de navegación.
class ProductAdapter(
    private var products: List<Product>,
    private val navigationListener: NavigationListener
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // 1. El ViewHolder: Representa una única fila (item_product.xml)
    // Contiene las referencias a las vistas (TextViews, ImageView, etc.)
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root: CardView = itemView.findViewById(R.id.rootCardView)
        val productImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        val productTitle: TextView = itemView.findViewById(R.id.tvProductTitle)
        val productPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val addButton: ImageButton = itemView.findViewById(R.id.btnAdd)
    }

    // 2. onCreateViewHolder: Se llama cuando RecyclerView necesita una nueva fila.
    // Infla (crea) el layout XML y lo pasa al ViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    // 3. onBindViewHolder: Se llama para mostrar los datos en una fila específica.
    // Conecta los datos del producto (de la lista) con las vistas del ViewHolder.
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        // Asignamos los datos del producto a las vistas
        holder.productTitle.text = product.name
        holder.productPrice.text = "$${product.price}"
        // Aquí iría la lógica para cargar la imagen con una librería como Glide o Picasso
        // holder.productImage.load(product.imageUrl)

        // ¡AQUÍ ESTÁ LA MAGIA DE LA NAVEGACIÓN!
        // Configuramos el click en toda la tarjeta (el CardView).
        holder.root.setOnClickListener {
            // Usamos la interfaz para notificar que se hizo clic en este producto.
            navigationListener.onNavigateToProductDetail(product)
        }

        // También puedes manejar otros clics, como el del botón de añadir
        holder.addButton.setOnClickListener {
            // Lógica para añadir al carrito (lo haremos más adelante)
        }
    }

    // 4. getItemCount: Devuelve el número total de elementos en la lista.
    override fun getItemCount(): Int {
        return products.size
    }

    // TAREA PARA TI: Añade un ID al CardView en item_product.xml
    // Reemplaza <androidx.cardview.widget.CardView ...>
    // con <androidx.cardview.widget.CardView android:id="@+id/rootCardView" ...>
}
