package com.example.gamehub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehub.R
import com.example.gamehub.models.Order
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class OrderAdapter(private val orders: List<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    // 1. ViewHolder: Contiene las referencias a las vistas de un solo item (item_order.xml)
    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderId: TextView = itemView.findViewById(R.id.tvOrderId)
        val orderDate: TextView = itemView.findViewById(R.id.tvOrderDate)
        val orderStatus: TextView = itemView.findViewById(R.id.tvOrderStatus)
        val orderTotal: TextView = itemView.findViewById(R.id.tvOrderTotal)
    }

    // 2. onCreateViewHolder: Crea una nueva vista de fila cuando es necesario.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    // 3. getItemCount: Devuelve el número total de pedidos.
    override fun getItemCount(): Int = orders.size

    // 4. onBindViewHolder: Vincula los datos de un pedido a las vistas del ViewHolder.
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        // Formateadores para la fecha y la moneda
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)

        // Asignamos los datos del pedido a las vistas
        holder.orderId.text = "Pedido #${order.id}"
        holder.orderDate.text = "Fecha: ${dateFormat.format(order.date)}"
        holder.orderStatus.text = "Estado: ${order.status}"
        holder.orderTotal.text = "Total: ${currencyFormat.format(order.total)}"

        // Podríamos añadir un OnClickListener aquí para ir al detalle del pedido
        holder.itemView.setOnClickListener {
            // Lógica para navegar a OrderDetailFragment, pasando el order.id
        }
    }
}
