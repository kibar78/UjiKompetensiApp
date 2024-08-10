package com.example.ujikompetensiapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ujikompetensiapp.database.DatabaseHelper
import com.example.ujikompetensiapp.database.Mahasiswa
import com.example.ujikompetensiapp.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var db : DatabaseHelper
    private lateinit var mahasiswaList: List<Mahasiswa>
    private lateinit var adapter: ArrayAdapter<String>
    private var titlesList: MutableList<String> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Data Mahasiswa"
        actionBar.setDisplayHomeAsUpEnabled(true)

        db = DatabaseHelper(this)

        binding.fab.setOnClickListener {
            val goEdit = Intent(this, AddActivity::class.java)
            startActivity(goEdit)
        }

        binding.listView.setOnItemClickListener { adapterView, view, i, l ->
            val options = arrayOf("Detail","Edit", "Delete")
            MaterialAlertDialogBuilder(this).apply {
                setTitle("Pilihan")
                setItems(options) {_, which ->
                    when(which){
                        0 -> {
                            val goDetail = Intent(this@MainActivity, DetailActivity::class.java)
                            goDetail.putExtra("mahasiswa_id", mahasiswaList[i].id)
                            startActivity(goDetail)
                        }
                        1 -> {
                            val goEdit = Intent(this@MainActivity, AddActivity::class.java)
                            goEdit.putExtra("mahasiswa_id", mahasiswaList[i].id)
                            startActivity(goEdit)
                        }
                        2 -> {
                            db.deleteMahasiswa(mahasiswaList[i])
                            loadMahasiswa()
                        }
                    }
                }
                create()
                show()
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchMahasiswa(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

    }

    private fun loadMahasiswa() {
        mahasiswaList = db.getAllMahasiswa()
        titlesList.clear()
        for (mahasiswa in mahasiswaList) {
            titlesList.add(mahasiswa.nim + "\n" + mahasiswa.nama + "\n" + mahasiswa.tglLahir
                            + "\n" + mahasiswa.jenisKelamin + "\n" + mahasiswa.alamat
                            + "\n" + mahasiswa.date)
        }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, titlesList)
        binding.listView.adapter = adapter
    }

    private fun searchMahasiswa(keyword: String) {
        mahasiswaList = db.searchMahasiswa(keyword)
        titlesList.clear()
        for (mahasiswa in mahasiswaList) {
            titlesList.add(mahasiswa.nama)
        }
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        loadMahasiswa()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}