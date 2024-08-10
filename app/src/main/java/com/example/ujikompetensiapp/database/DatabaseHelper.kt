package com.example.ujikompetensiapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "app_database"

        // User Table
        private const val USER_TABLE_NAME = "user_table"
        private const val USER_COL_1 = "ID"
        private const val USER_COL_2 = "EMAIL"
        private const val USER_COL_3 = "PASSWORD"

        // Mahasiswa Table
        private const val MAHASISWA_TABLE_NAME = "mahasiswa"
        private const val MAHASISWA_COLUMN_ID = "id"
        private const val MAHASISWA_COLUMN_NIM = "nim"
        private const val MAHASISWA_COLUMN_NAMA = "nama"
        private const val MAHASISWA_COLUMN_TGL_LAHIR = "tgl_lahir"
        private const val MAHASISWA_COLUMN_JENIS_KELAMIN = "jenis_kelamin"
        private const val MAHASISWA_COLUMN_ALAMAT = "alamat"
        private const val MAHASISWA_COLUMN_DATE = "date"

    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(
            "CREATE TABLE $USER_TABLE_NAME (" +
                    "$USER_COL_1 INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$USER_COL_2 TEXT, " +
                    "$USER_COL_3 TEXT)"
        )

        p0?.execSQL(
            "CREATE TABLE $MAHASISWA_TABLE_NAME (" +
                    "$MAHASISWA_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$MAHASISWA_COLUMN_NIM TEXT, " +
                    "$MAHASISWA_COLUMN_NAMA TEXT, " +
                    "$MAHASISWA_COLUMN_TGL_LAHIR TEXT, " +
                    "$MAHASISWA_COLUMN_JENIS_KELAMIN TEXT, " +
                    "$MAHASISWA_COLUMN_ALAMAT TEXT, " +
                    "$MAHASISWA_COLUMN_DATE TEXT)"
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $USER_TABLE_NAME")
        p0?.execSQL("DROP TABLE IF EXISTS $MAHASISWA_TABLE_NAME")
        onCreate(p0)
    }

    fun insertUser(email: String, password: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(USER_COL_2, email)
            put(USER_COL_3, password)
        }
        val result = db.insert(USER_TABLE_NAME, null, contentValues)
        return result != -1L
    }

    fun checkEmail(email: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $USER_TABLE_NAME WHERE EMAIL = ?", arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun checkEmailPassword(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $USER_TABLE_NAME WHERE EMAIL = ? AND PASSWORD = ?", arrayOf(email, password))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Mahasiswa Methods
    fun addMahasiswa(mahasiswa: Mahasiswa): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(MAHASISWA_COLUMN_NIM, mahasiswa.nim)
            put(MAHASISWA_COLUMN_NAMA, mahasiswa.nama)
            put(MAHASISWA_COLUMN_TGL_LAHIR, mahasiswa.tglLahir)
            put(MAHASISWA_COLUMN_JENIS_KELAMIN, mahasiswa.jenisKelamin)
            put(MAHASISWA_COLUMN_ALAMAT, mahasiswa.alamat)
            put(MAHASISWA_COLUMN_DATE, mahasiswa.date)
        }
        val id = db.insert(MAHASISWA_TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun getMahasiswa(id: Int): Mahasiswa {
        val db = this.readableDatabase
        val cursor = db.query(
            MAHASISWA_TABLE_NAME, arrayOf(MAHASISWA_COLUMN_ID, MAHASISWA_COLUMN_NIM, MAHASISWA_COLUMN_NAMA, MAHASISWA_COLUMN_TGL_LAHIR, MAHASISWA_COLUMN_JENIS_KELAMIN, MAHASISWA_COLUMN_ALAMAT, MAHASISWA_COLUMN_DATE),
            "$MAHASISWA_COLUMN_ID=?", arrayOf(id.toString()), null, null, null, null
        )
        cursor?.moveToFirst()
        val mahasiswa = Mahasiswa(
            cursor.getInt(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_NIM)),
            cursor.getString(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_NAMA)),
            cursor.getString(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_TGL_LAHIR)),
            cursor.getString(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_JENIS_KELAMIN)),
            cursor.getString(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_ALAMAT)),
            cursor.getString(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_DATE))
        )
        cursor.close()
        return mahasiswa
    }

    fun getAllMahasiswa(): List<Mahasiswa> {
        val mahasiswaList = mutableListOf<Mahasiswa>()
        val selectQuery = "SELECT * FROM $MAHASISWA_TABLE_NAME ORDER BY $MAHASISWA_COLUMN_NAMA ASC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val mahasiswa = Mahasiswa(
                    cursor.getInt(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_NIM)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_NAMA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_TGL_LAHIR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_JENIS_KELAMIN)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_ALAMAT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_DATE))
                )
                mahasiswaList.add(mahasiswa)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return mahasiswaList
    }

    fun updateMahasiswa(mahasiswa: Mahasiswa): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(MAHASISWA_COLUMN_NAMA, mahasiswa.nama)
            put(MAHASISWA_COLUMN_TGL_LAHIR, mahasiswa.tglLahir)
            put(MAHASISWA_COLUMN_JENIS_KELAMIN, mahasiswa.jenisKelamin)
            put(MAHASISWA_COLUMN_ALAMAT, mahasiswa.alamat)
            put(MAHASISWA_COLUMN_DATE, mahasiswa.date)
        }
        return db.update(MAHASISWA_TABLE_NAME, values, "$MAHASISWA_COLUMN_ID = ?", arrayOf(mahasiswa.id.toString()))
    }

    fun deleteMahasiswa(mahasiswa: Mahasiswa) {
        val db = this.writableDatabase
        db.delete(MAHASISWA_TABLE_NAME, "$MAHASISWA_COLUMN_ID = ?", arrayOf(mahasiswa.id.toString()))
        db.close()
    }

    fun searchMahasiswa(keyword: String): List<Mahasiswa> {
        val mahasiswaList = mutableListOf<Mahasiswa>()
        val searchQuery =
            "SELECT * FROM $MAHASISWA_TABLE_NAME WHERE $MAHASISWA_COLUMN_NAMA LIKE ? OR $MAHASISWA_COLUMN_ALAMAT LIKE ? ORDER BY $MAHASISWA_COLUMN_NAMA ASC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(searchQuery, arrayOf("%$keyword%", "%$keyword%"))
        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val mahasiswa = Mahasiswa(
                        cursor.getInt(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_NIM)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_NAMA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_TGL_LAHIR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_JENIS_KELAMIN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MAHASISWA_COLUMN_ALAMAT))
                    )
                    mahasiswaList.add(mahasiswa)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return mahasiswaList
        }
    }
}