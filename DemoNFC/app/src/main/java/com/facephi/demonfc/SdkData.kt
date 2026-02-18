package com.facephi.demonfc

import com.facephi.core.data.OperationType
import com.facephi.core.data.SdkApplication
import com.facephi.demonfc.model.DocumentType
import com.facephi.nfc_component.data.configuration.NfcConfigurationData
import com.facephi.nfc_component.data.configuration.NfcDocumentType
import com.facephi.nfc_component.data.configuration.ReadingProgressStyle
import com.facephi.sdk.data.EnvironmentLicensingData
import com.facephi.sdk.data.LicensingOffline
import com.facephi.sdk.data.LicensingOnline
import com.facephi.sdk.data.SdkConfigurationData
import com.facephi.selphid_component.data.configuration.SelphIDConfigurationData
import com.facephi.selphid_component.data.configuration.SelphIDDocumentSide
import com.facephi.selphid_component.data.configuration.SelphIDDocumentType
import com.facephi.selphid_component.data.configuration.SelphIDScanMode


object SdkData {

    // ************** LICENSE **************
    const val LICENSE_ONLINE = false

    val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        apiKey = "..."
    )

    const val LICENSE = ""

    // ************** DATA **************

    const val CUSTOMER_ID: String = "nfc-demo@email.com"
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

    fun getSelphIdConfig(docType: DocumentType): SelphIDConfigurationData {
        return when (docType) {
            DocumentType.ID_CARD -> idCardConfig
            DocumentType.PASSPORT -> passportConfig
            DocumentType.FOREIGN_CARD -> foreignCardConfig
        }
    }

    fun getNfcDocType(docType: DocumentType): NfcDocumentType {
        return when (docType) {
            DocumentType.ID_CARD -> NfcDocumentType.ID_CARD
            DocumentType.PASSPORT -> NfcDocumentType.PASSPORT
            DocumentType.FOREIGN_CARD -> NfcDocumentType.FOREIGN_CARD
        }
    }

    private val idCardConfig = SelphIDConfigurationData(
        documentType = SelphIDDocumentType.ID_CARD,
        showResultAfterCapture = false,
        resourcesPath = SELPHID_RESOURCES,
        wizardMode = false,
        documentSide = SelphIDDocumentSide.BACK,
        specificData = "ES|<ALL>",
        generateRawImages = true,
        scanMode = SelphIDScanMode.MODE_SEARCH
    )

    private val passportConfig = SelphIDConfigurationData(
        documentType = SelphIDDocumentType.PASSPORT,
        showResultAfterCapture = false,
        resourcesPath = SELPHID_RESOURCES,
        wizardMode = false,
        documentSide = SelphIDDocumentSide.FRONT,
        generateRawImages = true,
        scanMode = SelphIDScanMode.MODE_GENERIC
    )

    private val foreignCardConfig = SelphIDConfigurationData(
        documentType = SelphIDDocumentType.FOREIGN_CARD,
        showResultAfterCapture = false,
        resourcesPath = SELPHID_RESOURCES,
        wizardMode = false,
        documentSide = SelphIDDocumentSide.BACK,
        specificData = "ES|<ALL>",
        generateRawImages = true,
        scanMode = SelphIDScanMode.MODE_SEARCH
    )

    fun getNfcConfig(support: String,
                     birthDate: String,
                     expirationDate: String,
                     showPreviousTip: Boolean,
                     showTutorial: Boolean,
                     showDiagnostic: Boolean,
                     skipPACE: Boolean,
                     extractFace: Boolean,
                     extractSignature: Boolean,
                     docType: DocumentType,
                     readingProgressStyle: ReadingProgressStyle
    ): NfcConfigurationData {
        return NfcConfigurationData(
            documentNumber = support, // Num soport.
            birthDate = birthDate, // "dd/MM/yyyy"
            expirationDate = expirationDate, // "dd/MM/yyyy",
            skipPACE = skipPACE,
            showTutorial = showTutorial,
            showDiagnostic = showDiagnostic,
            showPreviousTip = showPreviousTip,
            extractSignatureImage = extractSignature,
            extractFacialImage = extractFace,
            documentType = getNfcDocType(docType),
            readingProgressStyle = readingProgressStyle
        )
    }

}