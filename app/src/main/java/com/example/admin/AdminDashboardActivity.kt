package com.example.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.admin.User.UserManagementActivity
import com.example.admin.Video.UploadVideoActivity

class AdminDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Agrisarthi)
        setContentView(R.layout.activity_admin_dashboard)

        findViewById<CardView>(R.id.cardProductApproval).setOnClickListener {
            startActivity(Intent(this, ProductApprovalActivity::class.java))
        }
        findViewById<CardView>(R.id.cardAddTutorial).setOnClickListener {
            startActivity(Intent(this, UploadVideoActivity::class.java))
        }
        findViewById<CardView>(R.id.cardUserManagement).setOnClickListener {
            startActivity(Intent(this, UserManagementActivity::class.java))
        }
//        findViewById<CardView>(R.id.cardOrders).setOnClickListener {
//            startActivity(Intent(this, OrdersActivity::class.java))
//        }
    }
} 