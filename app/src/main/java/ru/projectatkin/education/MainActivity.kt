package ru.projectatkin.education

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreference:SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreference = SharedPreference(this)
        sharedPreference.save("downloaded", false)
    }
}

interface CellClickListener {
    fun onCellClickListener(title: String?)
}

interface CellClickListenerGenre {
    fun onCellClickListenerGenre(title: String?)
}