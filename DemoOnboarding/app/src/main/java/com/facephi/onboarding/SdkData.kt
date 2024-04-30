package com.facephi.onboarding

import com.facephi.core.data.OperationType
import com.facephi.core.data.SdkApplication
import com.facephi.sdk.data.EnvironmentLicensingData
import com.facephi.sdk.data.LicensingOffline
import com.facephi.sdk.data.LicensingOnline
import com.facephi.sdk.data.SdkConfigurationData
import com.facephi.selphi_component.data.configuration.SelphiConfigurationData
import com.facephi.selphid_component.data.configuration.SelphIDConfigurationData
import com.facephi.selphid_component.data.configuration.SelphIDDocumentSide
import com.facephi.selphid_component.data.configuration.SelphIDDocumentType
import com.facephi.selphid_component.data.configuration.SelphIDScanMode
import com.facephi.selphid_component.data.configuration.SelphIDTimeout
import com.facephi.tracking_component.TrackingController

object SdkData {

    // ************** LICENSE **************

    const val LICENSE_ONLINE = false

    val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        apiKey = "..."
    )

    const val LICENSE = "..."

    // ************** DATA **************

    const val CUSTOMER_ID = "demo_onboarding@email.com"
    val OPERATION_TYPE = OperationType.ONBOARDING
    const val SELPHI_RESOURCES = "resources-selphi-2-0.zip"
    const val SELPHID_RESOURCES = "resources-selphid-2-0.zip"

    val BASE_URL = ""

    fun getInitConfiguration(sdkApplication: SdkApplication) = SdkConfigurationData(
        sdkApplication = sdkApplication,
        licensing = if (LICENSE_ONLINE) {
            LicensingOnline(environmentLicensingData)
        } else {
            LicensingOffline(LICENSE)
        },
        trackingController = TrackingController() // or null
    )

    // SELPHI DATA
    val selphiConfiguration = SelphiConfigurationData(
        debug = false,
        showTutorial = true,
        showDiagnostic = true,
        resourcesPath = SELPHI_RESOURCES
    )

    // SELPHID DATA
    val selphIDConfiguration = SelphIDConfigurationData(
        debug = false,
        showTutorial = true,
        showDiagnostic = true,
        wizardMode = true,
        showResultAfterCapture = true,
        scanMode = SelphIDScanMode.MODE_SEARCH,
        specificData = "ES|<ALL>",
        fullscreen = true,
        documentType = SelphIDDocumentType.ID_CARD,
        documentSide = SelphIDDocumentSide.FRONT,
        timeout = SelphIDTimeout.LONG,
        generateRawImages = false,
        resourcesPath = SELPHID_RESOURCES,
        )

}