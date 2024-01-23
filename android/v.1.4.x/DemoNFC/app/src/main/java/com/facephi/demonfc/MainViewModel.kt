package com.facephi.demonfc

import androidx.lifecycle.ViewModel
import com.facephi.core.results.enums.FinishStatus
import com.facephi.demonfc.model.DocumentType
import com.facephi.nfc_component.NfcController
import com.facephi.nfc_component.data.NfcConfigurationData

import com.facephi.sdk.SDKController
import com.facephi.selphid_component.SelphIDController
import io.github.aakira.napier.Napier

class MainViewModel: ViewModel() {

    fun newOperation(debugLogs: (String) -> Unit){
        SDKController.newOperation(
            operationType = SdkData.OPERATION_TYPE,
            customerId = SdkData.CUSTOMER_ID,
        ) {
            when (it.finishStatus) {
                FinishStatus.STATUS_OK -> {
                    Napier.d("APP: NEW OPERATION OK")
                    debugLogs("NEW OPERATION: OK")
                }

                FinishStatus.STATUS_ERROR -> {
                    Napier.d("APP: NEW OPERATION ERROR: ${it.errorType.name}")
                    debugLogs("NEW OPERATION: KO - ${it.errorType.name}")
                }
            }
        }
    }

    fun launchSelphidAndNfc(
        docType: DocumentType,
        debugLogs: (String) -> Unit
    ) {
        SDKController.launch(
            SelphIDController(SdkData.getSelphIdConfig(docType)) {
                when (it.finishStatus) {
                    FinishStatus.STATUS_ERROR ->  debugLogs("SelphID: KO - ${it.errorType.name}")
                    FinishStatus.STATUS_OK -> {
                        debugLogs("SelphID: OK")
                        debugLogs("SelphID: Issuer :${it.data?.personalData?.issuer}")
                        debugLogs("SelphID: Nationality :${it.data?.personalData?.nationality}")
                        debugLogs("SelphID: DocumentType :${docType.name}")

                        val birthDate = it.data?.personalData?.birthDate.orEmpty()
                        val expirationDate = it.data?.personalData?.expiryDate.orEmpty()
                        val nfcKey = it.data?.personalData?.nfcKey.orEmpty()

                        if (birthDate.isNotEmpty() && expirationDate.isNotEmpty() && nfcKey.isNotEmpty()){
                            launchNfc(
                                nfcConfigurationData = NfcConfigurationData(
                                    documentNumber = nfcKey, // Num support.
                                    birthDate = birthDate, // "dd/MM/yyyy"
                                    expirationDate = expirationDate, // "dd/MM/yyyy",
                                ),
                            debugLogs = debugLogs
                            )
                        } else {
                            debugLogs("NFC: Data from SelphID NOT ENOUGH")
                        }

                    }
                }
            })

    }


    fun launchNfc(
        nfcConfigurationData: NfcConfigurationData,
        debugLogs: (String) -> Unit
    ){
        SDKController.launch(
            NfcController(componentData = nfcConfigurationData,
                state = { state ->
                    Napier.d("APP: NFC  State: ${state.name}")
                    debugLogs("NFC: State: ${state.name}")
                }) {
                when (it.finishStatus) {
                    FinishStatus.STATUS_OK -> {
                        Napier.d("APP: NFC OK")
                        debugLogs("NFC: OK")
                        debugLogs("VALIDATIONS: ${it.data?.nfcValidations}")
                    }

                    FinishStatus.STATUS_ERROR -> {
                        Napier.d("APP: NFC ERROR - ${it.errorType.name}")
                        debugLogs("NFC: ERROR - ${it.errorType.name}")
                    }
                }
            }
        )
    }
}