package com.facephi.onboarding

import com.facephi.core.data.OperationType
import com.facephi.sdk.data.EnvironmentLicensingData
import com.facephi.selphi_component.data.configuration.SelphiConfigurationData
import com.facephi.selphid_component.data.configuration.SelphIDConfigurationData
import com.facephi.selphid_component.data.configuration.SelphIDDocumentSide
import com.facephi.selphid_component.data.configuration.SelphIDDocumentType
import com.facephi.selphid_component.data.configuration.SelphIDScanMode
import com.facephi.selphid_component.data.configuration.SelphIDTimeout

object SdkData {

    // ************** LICENSE **************

    const val LICENSE_ONLINE = false

    val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        url = "...",
        apiKey = "..."
    )

    const val LICENSE = "..."

    // ************** DATA **************

    const val CUSTOMER_ID = "demo_onboarding@email.com"
    val OPERATION_TYPE = OperationType.ONBOARDING
    const val SELPHI_RESOURCES = "resources-selphi-2-0.zip"
    const val SELPHID_RESOURCES = "resources-selphid-2-0.zip"

    // SELPHI DATA
    val selphiConfiguration = SelphiConfigurationData(
        debug = false,
        resourcesPath = SELPHI_RESOURCES
    )

    // SELPHID DATA
    val selphIDConfiguration = SelphIDConfigurationData(
        debug = false,
        wizardMode = true,
        showResultAfterCapture = true,
        showTutorial = false,
        scanMode = SelphIDScanMode.MODE_SEARCH,
        specificData = "ES|<ALL>",
        fullscreen = true,
        tokenImageQuality = 0.5f,
        locale = "",
        documentType = SelphIDDocumentType.ID_CARD,
        documentSide = SelphIDDocumentSide.FRONT,
        timeout = SelphIDTimeout.LONG,
        generateRawImages = false,
        translationsContent = "",
        viewsContent = "",
        documentModels = "",
        resourcesPath = SELPHID_RESOURCES,
        )

}