package com.example.ujikompetensiapp.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ujikompetensiapp.R
import com.example.ujikompetensiapp.database.DatabaseHelper
import com.example.ujikompetensiapp.database.Mahasiswa
import com.example.ujikompetensiapp.databinding.ActivityAddBinding
import com.example.ujikompetensiapp.utils.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddActivity : AppCompatActivity(),DatePickerFragment.DialogDateListener {

    private lateinit var binding: ActivityAddBinding

    private lateinit var db: DatabaseHelper
    private var mahasiswa: Mahasiswa? = null
    private var isEdit = false

    companion object{
        private const val DATE_PICKER_TAG = "DatePicker"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Tambah Data"
        actionBar.setDisplayHomeAsUpEnabled(true)

        db = DatabaseHelper(this)
        
        binding.btnTglLahir.setOnClickListener{
            val datePicker = DatePickerFragment()
            datePicker.show(supportFragmentManager, DATE_PICKER_TAG)
        }

        val jenisKelamin = arrayOf("Laki-Laki","Perempuan")
        val adapter = ArrayAdapter(this,R.layout.drop_down,jenisKelamin)
        (binding.textInputLayout4.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        if (intent.hasExtra("mahasiswa_id")){
            val mhsId = intent.getIntExtra("mahasiswa_id", -1)
            mahasiswa = db.getMahasiswa(mhsId)
            mahasiswa.let {
                binding.edtNim.setText(it?.nim)
                binding.edtNama.setText(it?.nama)
                binding.btnTglLahir.setText(it?.tglLahir)
                val jk = jenisKelamin.indexOf(mahasiswa?.jenisKelamin)
                if (jk != -1){
                    (binding.textInputLayout4.editText as? AutoCompleteTextView)?.setText(jenisKelamin[jk], false)
                }
                binding.edtAlamat.setText(it?.alamat)
                isEdit = true
                actionBar.title = "Update Data"
            }
        }

        binding.btnSimpan.setOnClickListener {
            val nim = binding.edtNim.text.toString()
            val nama = binding.edtNama.text.toString()
            val tglLahir = binding.btnTglLahir.text.toString()
            val jenisKelamin = binding.autotCompleteJenisKelamin.text.toString()
            val alamat = binding.edtAlamat.text.toString()
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            if (nim.isEmpty() || nama.isEmpty() || alamat.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
            if (isEdit){
                mahasiswa.let {
                    it?.nim = nim
                    it?.nama = nama
                    it?.tglLahir = tglLahir
                    it?.jenisKelamin = jenisKelamin
                    it?.alamat = alamat
                    it?.date = date
                    if (it != null) {
                        db.updateMahasiswa(it)
                    }
                }
            }else{
                mahasiswa = Mahasiswa(nim = nim, nama = nama, tglLahir = tglLahir, jenisKelamin = jenisKelamin, alamat = alamat, date = date)
                db.addMahasiswa(mahasiswa!!)
            }
            finish()
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        when(tag){
            DATE_PICKER_TAG->{
                binding.btnTglLahir.text = dateFormat.format(calendar.time)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}