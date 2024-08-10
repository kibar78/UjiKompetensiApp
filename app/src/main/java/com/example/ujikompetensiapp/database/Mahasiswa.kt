package com.example.ujikompetensiapp.database

data class Mahasiswa(
    val id: Int = 0,
    var nim: String = "",
    var nama: String = "",
    var tglLahir: String = "",
    var jenisKelamin: String = "",
    var alamat: String = "",
    var date: String = ""
)
