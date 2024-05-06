package com.facephi.onboarding

import android.graphics.Bitmap

object ImageData {
    fun clear() {
        selphiBestImage = null
        documentFace = null
        documentFront = null
        documentBack = null
        tokenFaceImage = null
        selphiBestImageB64 = null
    }

    var selphiBestImage: Bitmap? = null
    var selphiBestImageB64: String? = null
    var documentFace: Bitmap? = null
    var documentFront: Bitmap? = null
    var documentBack: Bitmap? = null
    var tokenFaceImage: String? = null
}