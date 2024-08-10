package com.example.ujikompetensiapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ujikompetensiapp.R

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val actionBar = supportActionBar
        actionBar!!.title = "Tentang Aplikasi"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true

    }
}