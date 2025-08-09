package com.example.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProductApprovalActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var approveProductsButton: CardView
    private lateinit var cardAll: CardView
    private lateinit var productsRef: DatabaseReference
    private lateinit var adapter: PendingProductsAdapter
    private val pendingProducts = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_approval)

        productsRef = FirebaseDatabase.getInstance().getReference("products")
        recyclerView = findViewById(R.id.pendingProductsRecyclerView)
        approveProductsButton = findViewById(R.id.cardApprove)
        cardAll = findViewById(R.id.cardAll)

        adapter = PendingProductsAdapter(pendingProducts) { product -> onProductApproved(product) }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        approveProductsButton.setOnClickListener {
            recyclerView.visibility = View.VISIBLE
            loadPendingProducts()
        }

        cardAll.setOnClickListener {
            startActivity(Intent(this, AllProductsActivity::class.java))
        }

    }

    private fun loadPendingProducts() {
        productsRef.orderByChild("status").equalTo("pending")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    pendingProducts.clear()
                    for (snapshot in dataSnapshot.children) {
                        snapshot.getValue(Product::class.java)?.let { product ->
                            product.id = snapshot.key
                            pendingProducts.add(product)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@ProductApprovalActivity,
                        "Failed to load products: ${databaseError.message}",
                        Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun onProductApproved(product: Product) {
        val prodId = product.id ?: return
        productsRef.child(prodId).child("status").setValue("approved")
            .addOnSuccessListener {
                Toast.makeText(this, "Product approved successfully", Toast.LENGTH_SHORT).show()
                pendingProducts.remove(product)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to approve product: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
