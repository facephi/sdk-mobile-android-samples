package com.facephi.onboarding

import com.facephi.core.data.OperationType
import com.facephi.core.data.SdkApplication
import com.facephi.sdk.data.EnvironmentLicensingData
import com.facephi.sdk.data.LicensingOffline
import com.facephi.sdk.data.LicensingOnline
import com.facephi.sdk.data.SdkConfigurationData
import com.facephi.selphid.extractorcore.DocumentExtractor
import com.facephi.selphid.extractorv1.DocumentExtractorV1
import com.facephi.selphid.extractorv3.DocumentExtractorV3
import com.facephi.selphid_component.data.configuration.SelphIDCaptureMode
import com.facephi.selphid_component.data.configuration.SelphIDConfigurationData
import com.facephi.selphid_component.data.configuration.SelphIDDocumentType
import com.facephi.selphid_component.data.configuration.SelphIDExtractionConfiguration
import com.facephi.selphid_component.data.configuration.SelphIDTimeout
import com.facephi.selphid_component.data.configuration.SelphIDUIConfiguration

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

    // SelphID extractors: defaults match SelphIDExtractorSelectionConfiguration's preferredVendors (V3, V1)
    val selphIdExtractors: List<DocumentExtractor> = listOf(DocumentExtractorV3(), DocumentExtractorV1())

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
        showDocumentSelector: Boolean,
        showResultAfterCapture: Boolean,
        documentType: SelphIDDocumentType,
        captureMode: SelphIDCaptureMode,
        generateRawImages: Boolean,
        maxAttempts: Int,
    ) = SelphIDConfigurationData(
        timeout = SelphIDTimeout.LONG.milliseconds,
        maxAttempts = maxAttempts,
        uiConfiguration = SelphIDUIConfiguration(
            showTutorial = showTutorial,
            showPreviousTip = showPreviousTip,
            showDiagnostic = showDiagnostic,
            showAfterCapture = showResultAfterCapture,
            showDocumentSelector = showDocumentSelector,
            // required so the selector's continue button isn't stuck disabled, see migration-selphid.md 2.4
            defaultCountry = "ES",
            defaultDocumentType = documentType,
        ),
        extractionConfiguration = SelphIDExtractionConfiguration(
            captureMode = captureMode,
            documentTypes = listOf(documentType),
            generateRawImages = generateRawImages,
        ),
    )

}