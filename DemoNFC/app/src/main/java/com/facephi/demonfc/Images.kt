package com.facephi.demonfc

import com.facephi.core.data.SdkImage

object Images {
    fun clear() {
        faceImage = null
        signatureImage = null
    }

    var faceImage: SdkImage? = null
    var signatureImage: SdkImage? = null

}