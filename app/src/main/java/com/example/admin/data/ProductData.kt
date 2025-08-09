package com.example.admin.data


import com.example.admin.Product
import com.google.firebase.database.*

object ProductData {
    fun getProductsByCategory(category: String, callback: (List<Product>) -> Unit) {
        val productsRef = FirebaseDatabase.getInstance().getReference("products")
        productsRef.orderByChild("category").equalTo(category)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val products = mutableListOf<Product>()
                    for (child in snapshot.children) {
                        val product = child.getValue(Product::class.java)
                        if (product != null && product.status == "approved") {
                            product.id = child.key
                            products.add(product)
                        }
                    }
                    callback(products)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(emptyList())
                }
            })
    }
}
