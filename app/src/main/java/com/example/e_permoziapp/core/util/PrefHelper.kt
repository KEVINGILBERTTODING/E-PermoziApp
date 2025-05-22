package com.example.e_permoziapp.core.util

import android.content.Context
import com.example.e_permoziapp.core.constant.Constant

class PrefHelper(context: Context) {
    private val sharedPref = context.getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)

    fun getString(key: String): String? {
        return sharedPref.getString(key, null)
    }
    fun getBoolen(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }
    fun putString(key: String, value: String) {
        sharedPref.edit().putString(key, value).apply()
    }
    fun putBoolen(key: String, value: Boolean) {
        sharedPref.edit().putBoolean(key, value).apply()
    }
    fun getInt(key: String): Int {
        return sharedPref.getInt(key, 0)
    }
    fun putInt(key: String, value: Int) {
        sharedPref.edit().putInt(key, value).apply()
    }
    fun clear() {
        sharedPref.edit().clear().apply()
    }
}