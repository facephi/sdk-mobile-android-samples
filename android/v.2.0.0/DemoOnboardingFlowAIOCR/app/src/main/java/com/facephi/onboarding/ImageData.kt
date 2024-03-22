package com.facephi.onboarding

import android.graphics.Bitmap

object ImageData {
    fun clear() {
        selphiFace = null
        documentFace = null
        documentFront = null
        documentBack = null
        tokenFaceImage = null
        selphiFaceB64 = null
    }

    var selphiFace: Bitmap? = null
    var selphiFaceB64: String? = null
    var documentFace: Bitmap? = null
    var documentFront: Bitmap? = null
    var documentBack: Bitmap? = null
    var tokenFaceImage: String? = null
}