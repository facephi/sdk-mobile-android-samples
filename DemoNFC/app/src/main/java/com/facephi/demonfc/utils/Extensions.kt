package com.facephi.demonfc.utils

import java.text.SimpleDateFormat
import java.util.Locale

private val nfcDateRegex = Regex("""\d{2}/\d{2}/\d{4}""")

fun String.validNfcDate(): Boolean {
    if (!matches(nfcDateRegex)) return false
    return try {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US).apply {
            isLenient = false
        }
        val date = formatter.parse(this)
        date != null && formatter.format(date) == this
    } catch (e: Exception) {
        false
    }
}
