package com.example.ujikompetensiapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ujikompetensiapp.R
import com.example.ujikompetensiapp.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Dashboard"

        binding.btnList.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this,AddActivity::class.java)
            startActivity(intent)
        }
        binding.btnInfo.setOnClickListener {
            val intent = Intent(this,InfoActivity::class.java)
            startActivity(intent)
        }
    }
}