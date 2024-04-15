package com.facephi.demovoice.extensions

import android.util.Base64

fun Array<ByteArray>.getBase64Array(): Array<String> {
    val base64array = arrayListOf<String>()

    for (item in this) {
        val base64 = Base64.encodeToString(item, Base64.DEFAULT)
        base64array.add(base64.replace("\n", ""))
    }
    return base64array.toTypedArray()
}
