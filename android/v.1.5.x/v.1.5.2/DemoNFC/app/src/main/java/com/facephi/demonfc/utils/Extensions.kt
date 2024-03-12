package com.facephi.demonfc.utils

import java.text.SimpleDateFormat
import java.util.*

fun String.validNfcDate(): Boolean {
    if (this.isEmpty()) return false
    return try {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = formatter.parse(this)
        date != null
    }catch (e: Exception){
        false
    }

}