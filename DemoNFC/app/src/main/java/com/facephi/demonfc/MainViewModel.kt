package com.facephi.demonfc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkResult
import com.facephi.demonfc.model.DocumentType
import com.facephi.nfc_component.NfcController
import com.facephi.nfc_component.data.configuration.NfcConfigurationData
import com.facephi.sdk.SDKController
import com.facephi.selphid_component.SelphIDController
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    fun initSdk(sdkApplication: SdkApplication) {
        viewModelScope.launch {
            if (BuildConfig.DEBUG){
                SDKController.enableDebugMode()
            }

            val sdkConfig = SdkData.getInitConfiguration(sdkApplication)
            when (val result = SDKController.initSdk(sdkConfig)) {
                is SdkResult.Success -> Napier.d("INIT SDK OK")
                is SdkResult.Error -> Napier.d("INIT SDK ERROR: ${result.error}")
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
        debugLogs: (String) -> Unit
    ) {
        viewModelScope.launch {
            when (val result =
                SDKController.launch(
                    SelphIDController(SdkData.getSelphIdConfig(docType))
                )) {
                is SdkResult.Error -> debugLogs("SelphID: KO - ${result.error}")
                is SdkResult.Success -> {
                    debugLogs("SelphID: OK")
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
                                enableDebugMode = true,
                                showTutorial = true,
                                skipPACE = skipPACE
                            ),
                            debugLogs = debugLogs
                        )
                    } else {
                        debugLogs("NFC: Data from SelphID NOT ENOUGH")
                    }

                }
            }
        }

    }


    fun launchNfc(
        nfcConfigurationData: NfcConfigurationData,
        debugLogs: (String) -> Unit
    ) {
        viewModelScope.launch {
            when (val result =
                SDKController.launch(
                    NfcController(componentData = nfcConfigurationData,
                        debugLogs = {
                            Napier.d("APP: Logs: $it")
                            debugLogs("NFC: Logs: $it")
                        },
                        state = { state ->
                            Napier.d("APP: NFC  State: ${state.name}")
                            debugLogs("NFC: State: ${state.name}")
                        })
                )) {

                is SdkResult.Success -> {
                    Napier.d("APP: NFC OK")
                    debugLogs("NFC: OK")
                    debugLogs("VALIDATIONS: ${result.data.nfcValidations}")
                }

                is SdkResult.Error -> {
                    Napier.d("APP: NFC ERROR - ${result.error}")
                    debugLogs("NFC: ERROR - ${result.error}")
                }
            }
        }
    }
}

