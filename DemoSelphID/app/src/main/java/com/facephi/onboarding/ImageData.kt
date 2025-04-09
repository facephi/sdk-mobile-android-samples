package com.facephi.onboarding

import android.graphics.Bitmap

object ImageData {
    fun clear() {
        documentFace = null
        documentFront = null
        documentBack = null
        documentTokenFaceImage = null
    }

    var documentFace: Bitmap? = null
    var documentFront: Bitmap? = null
    var documentBack: Bitmap? = null
    var documentTokenFaceImage: String? = null
}