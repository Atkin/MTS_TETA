package ru.projectatkin.education

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(val context: Context) {
    private val PREFS_NAME = "kotlincodes"
    val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun save(KEY_NAME: String, status: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_NAME, status!!)
        editor.commit()
    }

    fun saveGenre(KEY_NAME: String, genre: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NAME,genre)
        editor.commit()
    }

    fun getValueBoolien(KEY_NAME: String, defaultValue: Boolean): Boolean {
        return sharedPref.getBoolean(KEY_NAME, defaultValue)
    }

    fun getGenre(KEY_NAME: String, Value: String): String {
        return sharedPref.getString(KEY_NAME, Value)!!
    }
}