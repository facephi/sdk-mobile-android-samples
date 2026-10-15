package com.facephi.onboarding

import com.facephi.core.data.OperationType
import com.facephi.core.data.SdkApplication
import com.facephi.sdk.data.EnvironmentLicensingData
import com.facephi.sdk.data.LicensingOffline
import com.facephi.sdk.data.LicensingOnline
import com.facephi.sdk.data.SdkConfigurationData
import com.facephi.selphi_component.data.configuration.SelphiCamera
import com.facephi.selphi_component.data.configuration.SelphiCameraConfiguration
import com.facephi.selphi_component.data.configuration.SelphiConfigurationData
import com.facephi.selphi_component.data.configuration.SelphiFacialConfiguration
import com.facephi.selphi_component.data.configuration.SelphiFaceLivenessMode
import com.facephi.selphi_component.data.configuration.SelphiIadConfigurationData

object SdkData {

    // ************** LICENSE **************

    const val LICENSE_ONLINE = true

    val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        apiKey = "..."
    )

    const val LICENSE = "..."

    // ************** DATA **************

    const val CUSTOMER_ID = "demo_selphi@email.com"
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

    // SELPHI DATA
    fun getSelphiConfiguration(
        showTutorial: Boolean,
        showPreviousTip: Boolean,
        showDiagnostic: Boolean,
        cameraPreferred: SelphiCamera,
        livenessMode: SelphiFaceLivenessMode,
        maxAttempts: Int,
        iadEnabled: Boolean,
    ) = SelphiConfigurationData(
        livenessMode = livenessMode,
        maxAttempts = maxAttempts,
        facialConfiguration = SelphiFacialConfiguration(
            selphiCameraConfiguration = SelphiCameraConfiguration(
                cameraPreferred = cameraPreferred,
                showTutorial = showTutorial,
                showPreviousTip = showPreviousTip,
                showDiagnostic = showDiagnostic,
            )
        ),
        iadConfiguration = SelphiIadConfigurationData(enabled = iadEnabled)
    )

}