package com.example.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Agrisarthi)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(3000)
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }
    }
} 