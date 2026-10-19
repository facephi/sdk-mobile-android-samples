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
import com.facephi.selphid.extractorcore.DocumentExtractor
import com.facephi.selphid.extractorv1.DocumentExtractorV1
import com.facephi.selphid.extractorv3.DocumentExtractorV3
import com.facephi.selphid_component.data.configuration.SelphIDCaptureMode
import com.facephi.selphid_component.data.configuration.SelphIDConfigurationData
import com.facephi.selphid_component.data.configuration.SelphIDDocumentType
import com.facephi.selphid_component.data.configuration.SelphIDExtractionConfiguration


object SdkData {

    // ************** LICENSE **************
    const val LICENSE_ONLINE = true

    val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        apiKey = "..."
    )

    const val LICENSE = ""

    // ************** DATA **************

    const val CUSTOMER_ID: String = "nfc-demo@email.com"
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

    // documentSide=BACK (reads the MRZ) maps to captureMode=Mrz; FRONT maps to captureMode=Front
    private val idCardConfig = SelphIDConfigurationData(
        extractionConfiguration = SelphIDExtractionConfiguration(
            captureMode = SelphIDCaptureMode.Mrz,
            documentTypes = listOf(SelphIDDocumentType.ID_CARD),
            documentCountries = listOf("ES"),
            generateRawImages = true,
        )
    )

    private val passportConfig = SelphIDConfigurationData(
        extractionConfiguration = SelphIDExtractionConfiguration(
            captureMode = SelphIDCaptureMode.Front,
            documentTypes = listOf(SelphIDDocumentType.PASSPORT),
            generateRawImages = true,
        )
    )

    private val foreignCardConfig = SelphIDConfigurationData(
        extractionConfiguration = SelphIDExtractionConfiguration(
            captureMode = SelphIDCaptureMode.Mrz,
            documentTypes = listOf(SelphIDDocumentType.FOREIGN_CARD),
            documentCountries = listOf("ES"),
            generateRawImages = true,
        )
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
                     readingProgressStyle: ReadingProgressStyle,
                     maxAttempts: Int = 3,
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
            readingProgressStyle = readingProgressStyle,
            maxAttempts = maxAttempts,
        )
    }

}