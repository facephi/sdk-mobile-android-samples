package com.facephi.demovoice

import com.facephi.core.data.SdkResult
import com.facephi.sdk.SDKController
import com.facephi.tracking_component.ExtraDataController
import io.github.aakira.napier.Napier

object VerificationManager {

    fun voiceEnroll(audios: Array<String>, onFinish: (String) -> Unit) {
        Napier.d("APP: Voice Enroll")
        SDKController.launchMethod(
            ExtraDataController { extraData ->
                when (extraData) {
                    is SdkResult.Success -> {
                        Napier.d("APP: EXTRA_DATA: OK")

                        if (extraData.data.isNotEmpty()){
                            // Voice Enroll audios, extraData.data
                        }

                    }

                    is SdkResult.Error -> Napier.d("APP: EXTRA_DATA: ExtraData error ${extraData.error}")
                }
            })
    }

    fun voiceAuthentication(audio: String, template: String, onFinish: (String) -> Unit) {
        Napier.d("APP: Voice auth")
        SDKController.launchMethod(
            ExtraDataController { extraData->
                when (extraData) {
                    is SdkResult.Success -> {
                        Napier.d("APP: EXTRA_DATA: OK")

                        if (extraData.data.isNotEmpty()){
                            // voiceAuthentication audio, template, extraData.data
                        }

                    }

                    is SdkResult.Error -> Napier.d("APP: EXTRA_DATA: ExtraData error ${extraData.error}")
                }
            })
    }
}