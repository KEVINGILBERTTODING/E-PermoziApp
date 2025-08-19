package com.example.e_permoziapp.core.extention

import android.content.res.Resources

fun Int.toPx(): Int =
    (this * Resources.getSystem().displayMetrics.density).toInt()