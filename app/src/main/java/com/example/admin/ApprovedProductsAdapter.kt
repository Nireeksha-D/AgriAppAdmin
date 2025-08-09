package com.example.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class ApprovedProductsAdapter(
    private val originList: MutableList<Product>
) : RecyclerView.Adapter<ApprovedProductsAdapter.VH>(), Filterable {

    private var displayList = originList.toMutableList()

    class VH(item: View) : RecyclerView.ViewHolder(item) {
        val name: TextView = item.findViewById(R.id.tvName)
        val categoryBrand: TextView = item.findViewById(R.id.tvCategoryBrand)
        val priceDiscount: TextView = item.findViewById(R.id.tvPriceDiscount)
        val description: TextView = item.findViewById(R.id.tvDescription)
        val timestamp: TextView = item.findViewById(R.id.tvTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product, parent, false)
        )

    override fun getItemCount(): Int = displayList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = displayList[position]
        holder.name.text = p.name.orEmpty()
        holder.categoryBrand.text = "${p.category.orEmpty()} • ${p.brand.orEmpty()}"
        val discountTxt = if ((p.discount ?: 0) > 0) " (${p.discount}% off)" else ""
        holder.priceDiscount.text = "₹${p.price}$discountTxt"
        holder.description.text = p.description.orEmpty()
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        holder.timestamp.text = "Approved on ${sdf.format(Date(p.timestamp))}"
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(cs: CharSequence?): FilterResults {
            val q = cs?.toString()?.trim()?.lowercase().orEmpty()
            val filtered = if (q.isEmpty()) {
                originList.toList()
            } else {
                originList.filter {
                    it.name.orEmpty().lowercase().contains(q) ||
                            it.category.orEmpty().lowercase().contains(q) ||
                            it.brand.orEmpty().lowercase().contains(q)
                }
            }
            return FilterResults().apply { values = filtered }
        }

        override fun publishResults(cs: CharSequence?, result: FilterResults) {
            displayList = (result.values as List<Product>).toMutableList()
            notifyDataSetChanged()
        }
    }
}
