package com.facephi.demovoice


import com.facephi.core.data.OperationType
import com.facephi.core.data.SdkApplication
import com.facephi.sdk.data.EnvironmentLicensingData
import com.facephi.sdk.data.LicensingOffline
import com.facephi.sdk.data.LicensingOnline
import com.facephi.sdk.data.SdkConfigurationData
import com.facephi.tracking_component.TrackingController
import com.facephi.voice_component.data.configuration.VoiceConfigurationData

object SdkData {

    // ************** LICENSE **************

    const val LICENSE_ONLINE = false

    val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        apiKey = "..."
    )

    const val LICENSE = "{..."

    // ************** DATA **************

    const val CUSTOMER_ID: String = "demo_voice-android@email.com"
    val OPERATION_TYPE = OperationType.ONBOARDING

    fun getInitConfiguration(sdkApplication: SdkApplication) = SdkConfigurationData(
        sdkApplication = sdkApplication,
        licensing = if (LICENSE_ONLINE) {
            LicensingOnline(environmentLicensingData)
        } else {
            LicensingOffline(LICENSE)
        },
        trackingController = TrackingController() // or null
    )

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