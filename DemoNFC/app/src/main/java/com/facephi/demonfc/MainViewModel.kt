package com.facephi.demonfc

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkResult
import com.facephi.demonfc.model.DocumentType
import com.facephi.demonfc.model.UIComponentResult
import com.facephi.nfc_component.NfcController
import com.facephi.nfc_component.data.configuration.NfcConfigurationData
import com.facephi.nfc_component.data.configuration.ReadingProgressStyle
import com.facephi.nfc_component.data.result.NfcSdkPersonalInformation
import com.facephi.sdk.SDKController
import com.facephi.selphid_component.SelphIDController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class MainViewModel : ViewModel() {

    private val _personalData = MutableStateFlow<NfcSdkPersonalInformation?>(null)
    val personalData: StateFlow<NfcSdkPersonalInformation?> = _personalData.asStateFlow()

    private val _nfcResult = MutableStateFlow(UIComponentResult.PENDING)
    val nfcResult: StateFlow<UIComponentResult> = _nfcResult.asStateFlow()

    fun initSdk(sdkApplication: SdkApplication, onError: (String) -> Unit) {
        viewModelScope.launch {
            SDKController.getAnalyticsEvents { time, componentName, eventType, info ->
                Log.i(
                    "APP", "*** ${formatEpochMillis(time)} - ${componentName.name} -" +
                            " ${eventType.name} -  ${info ?: ""} "
                )
            }

            if (BuildConfig.DEBUG) {
                SDKController.enableDebugMode()
            }

            val sdkConfig = SdkData.getInitConfiguration(sdkApplication)
            when (val result = SDKController.initSdk(sdkConfig)) {
                is SdkResult.Success -> Log.i("APP", "INIT SDK OK")
                is SdkResult.Error -> {
                    Log.i("APP", "INIT SDK ERROR: ${result.error}")
                    onError(result.error.name)
                }
            }

            /*SDKController.launch(TrackingErrorController {
                Napier.d("Tracking Error: ${it.name}")
            })*/

        }
    }

    fun newOperation(debugLogs: (String) -> Unit) {
        viewModelScope.launch {
            val result = SDKController.newOperation(
                operationType = SdkData.OPERATION_TYPE,
                customerId = SdkData.CUSTOMER_ID,
            )
            when (result) {
                is SdkResult.Success -> debugLogs("NEW OPERATION: OK")
                is SdkResult.Error -> debugLogs("NEW OPERATION: Error - ${result.error.name}")
            }
        }

    }

    fun launchSelphidAndNfc(
        skipPACE: Boolean,
        docType: DocumentType,
        extractFace: Boolean,
        extractSignature: Boolean,
        showPreviousTip: Boolean,
        showTutorial: Boolean,
        showDiagnostic: Boolean,
        readingProgressStyle: ReadingProgressStyle,
        debugLogs: (String) -> Unit
    ) {
        viewModelScope.launch {
            returnLogWithDate("APP: Launch SelphID", debugLogs)
            val result = SDKController.launch(SelphIDController(SdkData.getSelphIdConfig(docType)))
            when (result) {
                is SdkResult.Error -> {
                    returnLogWithDate("SelphID: KO - ${result.error}", debugLogs)
                    _nfcResult.update { UIComponentResult.ERROR }
                }

                is SdkResult.Success -> {
                    returnLogWithDate("SelphID: OK", debugLogs)
                    debugLogs("SelphID: Issuer :${result.data.personalData?.issuer}")
                    debugLogs("SelphID: Nationality :${result.data.personalData?.nationality}")
                    debugLogs("SelphID: DocumentType :${docType.name}")

                    val birthDate = result.data.personalData?.birthDate.orEmpty()
                    val expirationDate = result.data.personalData?.expiryDate.orEmpty()
                    val nfcKey = result.data.personalData?.nfcKey.orEmpty()

                    if (birthDate.isNotEmpty() && expirationDate.isNotEmpty() && nfcKey.isNotEmpty()) {
                        launchNfc(
                            nfcConfigurationData = NfcConfigurationData(
                                documentNumber = nfcKey, // Num support.
                                birthDate = birthDate, // "dd/MM/yyyy"
                                expirationDate = expirationDate, // "dd/MM/yyyy",
                                showTutorial = showTutorial,
                                showDiagnostic = showDiagnostic,
                                showPreviousTip = showPreviousTip,
                                skipPACE = skipPACE,
                                extractSignatureImage = extractSignature,
                                extractFacialImage = extractFace,
                                documentType = SdkData.getNfcDocType(docType),
                                readingProgressStyle = readingProgressStyle
                            ),
                            debugLogs = debugLogs,

                            )
                    } else {
                        _nfcResult.update { UIComponentResult.ERROR }
                        returnLogWithDate("NFC: Data from SelphID NOT ENOUGH", debugLogs)
                    }

                }
            }
        }
    }

    fun launchNfc(
        nfcConfigurationData: NfcConfigurationData,
        debugLogs: (String) -> Unit
    ) {
        returnLogWithDate("APP: Launch NFC", debugLogs)
        debugLogs("NFC - Configuration: Extract Facial image :${nfcConfigurationData.extractFacialImage}")
        debugLogs("NFC - Configuration: Extract Signature image :${nfcConfigurationData.extractSignatureImage}")
        debugLogs("NFC - Configuration: SkipPace :${nfcConfigurationData.skipPACE}")
        viewModelScope.launch {
            val result = SDKController.launch(
                NfcController(
                    componentData = nfcConfigurationData,
                    debugLogs = {
                        returnLogWithDate(it, debugLogs)
                    },
                    state = { state ->
                        Log.d("APP", "NFC  State: ${state.name}")
                        returnLogWithDate("NFC: State: ${state.name}", debugLogs)
                    })
            )
            when (result) {
                is SdkResult.Success -> {
                    Log.d("APP", " NFC OK")
                    returnLogWithDate("NFC finish OK", debugLogs)
                    debugLogs("VALIDATIONS: ${result.data.nfcValidations}")
                    result.data.nfcDocumentInformation?.type?.let {
                        debugLogs("TYPE: $it")
                    }
                    result.data.nfcDocumentInformation?.issuer?.let {
                        debugLogs("ISSUER: $it")
                    }
                    result.data.nfcPersonalInformation?.nationality?.let {
                        debugLogs("NATIONALITY: $it")
                    }

                    result.data.nfcImages?.facialImage?.let {
                        Images.faceImage = it
                        debugLogs("FACIAL image extracted")
                    } ?: run {
                        debugLogs("FACIAL image NULL")
                    }

                    result.data.nfcImages?.signatureImage?.let {
                        Images.signatureImage = it
                        debugLogs("SIGNATURE image extracted")
                    } ?: run {
                        debugLogs("SIGNATURE image NULL")
                    }

                    _personalData.value = result.data.nfcPersonalInformation
                    _nfcResult.update { UIComponentResult.OK }

                }

                is SdkResult.Error -> {
                    Log.d("APP", " NFC ERROR - ${result.error}")
                    returnLogWithDate("NFC: ERROR - ${result.error}", debugLogs)
                    _nfcResult.update { UIComponentResult.ERROR }
                }
            }
        }
    }

    fun clearData() {
        _personalData.value = null
        _nfcResult.value = UIComponentResult.PENDING
        Images.clear()
    }

    @OptIn(ExperimentalTime::class)
    fun returnLogWithDate(msg: String, debugLogs: (String) -> Unit) {
        debugLogs("${formatEpochMillis()} $msg")
    }

    @OptIn(ExperimentalTime::class)
    fun formatEpochMillis(epochMillis: Long = Clock.System.now().toEpochMilliseconds()): String {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${localDateTime.date} ${localDateTime.time}" // "YYYY-MM-DD HH:MM:SS"
    }
}

