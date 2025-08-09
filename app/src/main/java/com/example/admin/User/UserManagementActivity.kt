package com.example.admin.User

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin.R
import com.example.admin.databinding.ActivityUserManagementBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserManagementBinding
    private lateinit var ref: DatabaseReference
    private val users = mutableListOf<User>()
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter(users)
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = adapter

        ref = FirebaseDatabase.getInstance().getReference("users")
        loadUsers()
    }

    private fun loadUsers() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                snapshot.children.forEach {
                    it.getValue(User::class.java)?.let { user ->
                        users.add(user)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UserManagementActivity,
                    "Failed: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
