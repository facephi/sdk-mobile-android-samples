package com.facephi.onboarding

import com.facephi.core.data.OperationType
import com.facephi.core.data.SdkApplication
import com.facephi.sdk.data.EnvironmentLicensingData
import com.facephi.sdk.data.LicensingOffline
import com.facephi.sdk.data.LicensingOnline
import com.facephi.sdk.data.SdkConfigurationData
import com.facephi.selphid_component.data.configuration.SelphIDConfigurationData
import com.facephi.selphid_component.data.configuration.SelphIDDocumentSide
import com.facephi.selphid_component.data.configuration.SelphIDDocumentType
import com.facephi.selphid_component.data.configuration.SelphIDScanMode
import com.facephi.selphid_component.data.configuration.SelphIDTimeout

object SdkData {

    // ************** LICENSE **************

    const val LICENSE_ONLINE = true

    val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        apiKey = "..."
    )

    const val LICENSE = "..."

    // ************** DATA **************

    const val CUSTOMER_ID = "demo_onboarding@email.com"
    val OPERATION_TYPE = OperationType.ONBOARDING
    private const val SELPHID_RESOURCES = "resources-selphid-2-0.zip"

    fun getInitConfiguration(sdkApplication: SdkApplication) = SdkConfigurationData(
        sdkApplication = sdkApplication,
        licensing = if (LICENSE_ONLINE) {
            LicensingOnline(environmentLicensingData)
        } else {
            LicensingOffline(LICENSE)
        },
        trackingController = null // or TrackingController()
    )

    // SELPHID DATA
    fun getSelphIDConfiguration(
        showTutorial: Boolean,
        showPreviousTip: Boolean,
        showDiagnostic: Boolean,
        wizardMode: Boolean,
        showResultAfterCapture: Boolean,
        scanMode: SelphIDScanMode,
        specificData: String,
        fullscreen: Boolean,
        documentType: SelphIDDocumentType,
        documentSide: SelphIDDocumentSide,
        generateRawImages: Boolean,
    ) = SelphIDConfigurationData(
        showTutorial = showTutorial,
        showPreviousTip = showPreviousTip,
        showDiagnostic = showDiagnostic,
        wizardMode = wizardMode,
        showResultAfterCapture = showResultAfterCapture,
        scanMode = scanMode,
        specificData = specificData,
        fullscreen = fullscreen,
        documentType = documentType,
        documentSide = documentSide,
        timeout = SelphIDTimeout.LONG,
        generateRawImages = generateRawImages,
        resourcesPath = SELPHID_RESOURCES,
    )


}