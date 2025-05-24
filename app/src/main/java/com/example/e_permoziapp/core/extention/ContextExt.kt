package com.example.e_permoziapp.core.extention

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import java.io.Serializable

inline fun <reified T> Context.launchActivity(
    vararg extras: Pair<String, Any?>,
    flags: Int? = null
) {
    val intent = Intent(this, T::class.java).apply {
        flags?.let { this.flags = it }
        for ((key, value) in extras) {
            when (value) {
                null -> putExtra(key, null as java.io.Serializable?)
                is Int -> putExtra(key, value)
                is Long -> putExtra(key, value)
                is CharSequence -> putExtra(key, value)
                is String -> putExtra(key, value)
                is Float -> putExtra(key, value)
                is Double -> putExtra(key, value)
                is Char -> putExtra(key, value)
                is Short -> putExtra(key, value)
                is Boolean -> putExtra(key, value)
                is Serializable -> putExtra(key, value)
                is Parcelable -> putExtra(key, value)
                is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> putExtra(key, value as Array<CharSequence>)
                    value.isArrayOf<String>() -> putExtra(key, value as Array<String>)
                    value.isArrayOf<Parcelable>() -> putExtra(key, value as Array<Parcelable>)
                    else -> throw IllegalArgumentException("Unsupported array type for key \"$key\"")
                }
                is IntArray -> putExtra(key, value)
                is LongArray -> putExtra(key, value)
                is FloatArray -> putExtra(key, value)
                is DoubleArray -> putExtra(key, value)
                is CharArray -> putExtra(key, value)
                is ShortArray -> putExtra(key, value)
                is BooleanArray -> putExtra(key, value)
                else -> throw IllegalArgumentException("Unsupported type for key \"$key\"")
            }
        }
    }
    startActivity(intent)
}
