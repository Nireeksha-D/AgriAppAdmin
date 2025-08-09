package com.example.admin

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AllProductsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ApprovedProductsAdapter
    private lateinit var productsRef: DatabaseReference
    private val allProducts = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_products)

        productsRef = FirebaseDatabase.getInstance().getReference("products")
        recyclerView = findViewById(R.id.allProductsRecyclerView)
        adapter = ApprovedProductsAdapter(allProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        setupSearch()
        loadApprovedProducts()
    }

    private fun setupSearch() {
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(text: String): Boolean {
                adapter.filter.filter(text)
                return true
            }
            override fun onQueryTextSubmit(text: String): Boolean = false
        })
    }

    private fun loadApprovedProducts() {
        productsRef.orderByChild("status").equalTo("approved")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    allProducts.clear()
                    snapshot.children.mapNotNull { it.getValue(Product::class.java)?.apply { id = it.key } }
                        .sortedByDescending { it.timestamp }
                        .also { allProducts.addAll(it) }

                    adapter.notifyDataSetChanged()
                    val query = findViewById<SearchView>(R.id.searchView).query
                    adapter.filter.filter(query)
                }

                override fun onCancelled(e: DatabaseError) {
                    Toast.makeText(this@AllProductsActivity,
                        "Failed to load: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
