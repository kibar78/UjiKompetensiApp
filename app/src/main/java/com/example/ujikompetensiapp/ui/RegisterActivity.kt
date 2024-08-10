package com.example.ujikompetensiapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ujikompetensiapp.database.DatabaseHelper
import com.example.ujikompetensiapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Register"
        actionBar.setDisplayHomeAsUpEnabled(true)
        db = DatabaseHelper(this)

        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val checkEmail = db.checkEmail(email)
            if (!checkEmail) {
                val insert = db.insertUser(email, password)
                if (insert) {
                    Toast.makeText(applicationContext, "Registered Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Registration Failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Email Already Exists", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}