package com.facephi.onboarding

import com.facephi.capture_component.data.configuration.FileUploaderConfigurationData
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
import com.facephi.video_recording_component.data.configuration.VideoRecordingConfigurationData

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
    const val SELPHI_RESOURCES = "resources-selphi-2-0.zip"
    const val SELPHID_RESOURCES = "resources-selphid-2-0.zip"

    fun getInitConfiguration(sdkApplication: SdkApplication) = SdkConfigurationData(
        sdkApplication = sdkApplication,
        licensing = if (LICENSE_ONLINE) {
            LicensingOnline(environmentLicensingData)
        } else {
            LicensingOffline(LICENSE)
        },
        trackingController = TrackingController() // or null
    )

    // VideoRecording DATA
    fun getVideoRecording() = VideoRecordingConfigurationData()

    // SELPHI DATA
    fun getSelphiConfiguration(
        showTutorial: Boolean,
        showPreviousTip: Boolean,
        showDiagnostic: Boolean,
    ) = SelphiConfigurationData(
        showTutorial = showTutorial,
        showPreviousTip = showPreviousTip,
        showDiagnostic = showDiagnostic,
        resourcesPath = SELPHI_RESOURCES
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

    // FILE UPLOADER DATA
    fun getFileUploaderDConfiguration(
        allowGallery: Boolean,
        showPreviousTip: Boolean,
        showDiagnostic: Boolean,
        maxScannedDocs: Int,
    ) = FileUploaderConfigurationData(
        allowGallery = allowGallery,
        showPreviousTip = showPreviousTip,
        showDiagnostic = showDiagnostic,
        maxScannedDocs = maxScannedDocs
    )
}