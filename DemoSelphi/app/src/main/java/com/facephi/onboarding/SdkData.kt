package com.facephi.onboarding

import com.facephi.core.data.OperationType
import com.facephi.core.data.SdkApplication
import com.facephi.sdk.data.EnvironmentLicensingData
import com.facephi.sdk.data.LicensingOffline
import com.facephi.sdk.data.LicensingOnline
import com.facephi.sdk.data.SdkConfigurationData
import com.facephi.selphi_component.data.configuration.SelphiConfigurationData

object SdkData {

    // ************** LICENSE **************

    const val LICENSE_ONLINE = false

    val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        apiKey = "..."
    )

    const val LICENSE = "..."

    // ************** DATA **************

    const val CUSTOMER_ID = "demo_selphi@email.com"
    val OPERATION_TYPE = OperationType.ONBOARDING
    const val SELPHI_RESOURCES = "resources-selphi-2-0.zip"

    const val BASE_URL = ""
    const val API_KEY = ""

    fun getInitConfiguration(sdkApplication: SdkApplication) = SdkConfigurationData(
        sdkApplication = sdkApplication,
        licensing = if (LICENSE_ONLINE) {
            LicensingOnline(environmentLicensingData)
        } else {
            LicensingOffline(LICENSE)
        },
        trackingController = null // or TrackingController()
    )

    // SELPHI DATA
    fun getSelphiConfiguration(
        showTutorial: Boolean,
        showPreviousTip: Boolean,
        showDiagnostic: Boolean,
    ) = SelphiConfigurationData(
        debug = false,
        showTutorial = showTutorial,
        showPreviousTip = showPreviousTip,
        showDiagnostic = showDiagnostic,
        resourcesPath = SELPHI_RESOURCES
    )

}