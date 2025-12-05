package com.facephi.demophingers


import com.facephi.core.data.OperationType
import com.facephi.core.data.SdkApplication
import com.facephi.phingers_tf_component.data.configuration.CaptureOrientation
import com.facephi.phingers_tf_component.data.configuration.FingerFilter
import com.facephi.phingers_tf_component.data.configuration.PhingersConfigurationData
import com.facephi.sdk.data.EnvironmentLicensingData
import com.facephi.sdk.data.LicensingOffline
import com.facephi.sdk.data.LicensingOnline
import com.facephi.sdk.data.SdkConfigurationData


object SdkData {
    // ************** LICENSE **************

    const val LICENSE_ONLINE = false

    val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        apiKey = "..."
    )

    const val LICENSE = ""

    // ************** DATA **************

    const val CUSTOMER_ID: String = "demo-phingers-android@email.com"
    val OPERATION_TYPE = OperationType.ONBOARDING

    fun getInitConfiguration(sdkApplication: SdkApplication) = SdkConfigurationData(
        sdkApplication = sdkApplication,
        licensing = if (LICENSE_ONLINE) {
            LicensingOnline(environmentLicensingData)
        } else {
            LicensingOffline(LICENSE)
        },
        trackingController = null // or TrackingController()
    )

    fun getConfiguration(
        showPreviousTip: Boolean,
        showDiagnostic: Boolean,
        liveness: Boolean,
        captureOrientation: CaptureOrientation,
        fingerFilter: FingerFilter,
    ) = PhingersConfigurationData(
        showPreviousTip = showPreviousTip,
        useLiveness = liveness,
        showDiagnostic = showDiagnostic,
        reticleOrientation = captureOrientation,
        fingerFilter = fingerFilter
    )

}