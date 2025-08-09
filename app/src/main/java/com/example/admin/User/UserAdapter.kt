package com.example.admin.User

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admin.databinding.ItemUserBinding

class UserAdapter(private val users: List<User>) :
    RecyclerView.Adapter<UserAdapter.VH>() {

    inner class VH(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val u = users[position]
        holder.binding.apply {
            tvName.text = u.name
            tvPhone.text = u.phone
            tvEmail.text = u.email
            tvAddress.text = u.address
            tvDescription.text = u.description
        }
    }
}
