package com.facephi.onboarding

import android.graphics.Bitmap

object ImageData {
    fun clear() {
        selphiBestImage = null
        selphiBestImageTokenized = null
        documentFace = null
        documentFront = null
        documentBack = null
        documentTokenFaceImage = null
        documentTokenOcr = null
    }

    var selphiBestImage: Bitmap? = null
    var selphiBestImageTokenized: String? = null
    var documentFace: Bitmap? = null
    var documentFront: Bitmap? = null
    var documentBack: Bitmap? = null
    var documentTokenFaceImage: String? = null
    var documentTokenOcr: String? = null
}
