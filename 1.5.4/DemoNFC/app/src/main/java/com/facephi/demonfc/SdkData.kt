package com.facephi.demonfc

import com.facephi.core.data.OperationType
import com.facephi.demonfc.model.DocumentType
import com.facephi.demonfc.model.ShowScreen
import com.facephi.nfc_component.data.configuration.NfcConfigurationData
import com.facephi.sdk.data.EnvironmentLicensingData
import com.facephi.selphid_component.data.configuration.SelphIDConfigurationData
import com.facephi.selphid_component.data.configuration.SelphIDDocumentSide
import com.facephi.selphid_component.data.configuration.SelphIDDocumentType
import com.facephi.selphid_component.data.configuration.SelphIDScanMode


object SdkData {

    // ************** LICENSE **************
    const val LICENSE_ONLINE = false

    val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        url = "https://....dev",
        apiKey = "..."
    )

    const val LICENSE = ""

    // ************** DATA **************

    const val CUSTOMER_ID: String = "email@email.com"
    val OPERATION_TYPE = OperationType.ONBOARDING
    private const val SELPHID_RESOURCES = "resources-selphid-2-0.zip"

    fun getSelphIdConfig(docType: DocumentType): SelphIDConfigurationData {
        return when (docType) {
            DocumentType.ID_CARD -> idCardConfig
            DocumentType.PASSPORT -> passportConfig
            DocumentType.FOREIGN_CARD -> foreignCardConfig
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
                     showScreen: ShowScreen,
                     skipPACE: Boolean
    ): NfcConfigurationData {
        return NfcConfigurationData(
            documentNumber = support, // Num soport.
            birthDate = birthDate, // "dd/MM/yyyy"
            expirationDate = expirationDate, // "dd/MM/yyyy",
            enableDebugMode = true,
            skipPACE = skipPACE,
            showTutorial = when (showScreen) {
                ShowScreen.SHOW_DIAGNOSTIC-> false
                ShowScreen.SHOW_TUTORIAL, ShowScreen.SHOW_TUTORIAL_AND_DIAGNOSTIC -> {
                    true
                }},
        )
    }
}