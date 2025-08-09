package com.example.admin.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.admin.R

class OrderAdapter(private val orderList: List<Order>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        val tvUserPhone: TextView = itemView.findViewById(R.id.tvUserPhone)
        val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val tvPaymentMode: TextView = itemView.findViewById(R.id.tvPaymentMode)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.tvUserName.text = "Name: ${order.userName}"
        holder.tvUserPhone.text = "Phone: ${order.userPhone}"
        holder.tvProductName.text = "Product: ${order.productName}"
        holder.tvPrice.text = "Price: ₹${order.price}"
        holder.tvAddress.text = "Address: ${order.address}"
        holder.tvPaymentMode.text = "Payment: ${order.paymentMode}"
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}
