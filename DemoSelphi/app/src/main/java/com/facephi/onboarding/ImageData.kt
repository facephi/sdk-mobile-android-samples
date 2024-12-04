package com.facephi.onboarding

import android.graphics.Bitmap

object ImageData {
    fun clear() {
        selphiBestImage = null
        selphiBestImageTokenized = null
    }

    var selphiBestImage: Bitmap? = null
    var selphiBestImageTokenized: String? = null
}