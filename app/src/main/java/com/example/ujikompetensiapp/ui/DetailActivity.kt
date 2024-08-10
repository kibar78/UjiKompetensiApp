package com.example.ujikompetensiapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ujikompetensiapp.database.DatabaseHelper
import com.example.ujikompetensiapp.database.Mahasiswa
import com.example.ujikompetensiapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var db: DatabaseHelper
    private var mahasiswa: Mahasiswa? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)

        val actionBar = supportActionBar
        actionBar!!.title = "Detail Data"
        actionBar.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra("mahasiswa_id")){
            val mhsId = intent.getIntExtra("mahasiswa_id", -1)
            mahasiswa = db.getMahasiswa(mhsId)
            mahasiswa.let {
                binding.tvNim.text = it?.nim
                binding.tvNama.text = it?.nama
                binding.tvTglLahir.text = it?.tglLahir
                binding.tvJk.text = it?.jenisKelamin
                binding.tvAlamat.text = it?.alamat
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}