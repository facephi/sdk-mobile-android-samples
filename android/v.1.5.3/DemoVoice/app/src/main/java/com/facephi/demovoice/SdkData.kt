package com.facephi.demovoice


import com.facephi.core.data.OperationType
import com.facephi.sdk.data.EnvironmentLicensingData
import com.facephi.voice_component.data.configuration.VoiceConfigurationData

object SdkData {

    // ************** LICENSE **************

    const val LICENSE_ONLINE = false

    val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        url = "https://...",
        apiKey = "..."
    )

    const val LICENSE = "{..."

    // ************** DATA **************

    const val CUSTOMER_ID: String = "demo_voice-android@email.com"
    val OPERATION_TYPE = OperationType.ONBOARDING

    // VOICE DATA
    val voiceConfigurationData = VoiceConfigurationData(
        phrases = arrayOf(
            "Tu nombre completo",
            "Tu DNI con letra",
            "Tu dirección completa"
        )
    )
    val voiceAuthConfigurationData = VoiceConfigurationData(
        phrases = arrayOf(
            "Tu DNI con letra"
        )
    )

}