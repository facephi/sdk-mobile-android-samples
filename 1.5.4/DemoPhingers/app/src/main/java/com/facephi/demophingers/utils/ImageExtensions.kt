package com.facephi.demophingers.utils

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Bitmap.toBase64(): String? {
    return Base64.encodeToString(this.toByteArray(), Base64.NO_WRAP)
}

fun Bitmap.toByteArray(quality: Int = 95): ByteArray {
    ByteArrayOutputStream().apply {
        compress(Bitmap.CompressFormat.JPEG, quality, this)
        return toByteArray()
    }
}
