package com.facephi.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkImage
import com.facephi.core.data.SdkResult
import com.facephi.onboarding.utils.toBase64
import com.facephi.sdk.FlowController
import com.facephi.sdk.SDKController
import com.facephi.sdk.data.flow.FlowKeys
import com.facephi.selphi_component.RawTemplateController
import com.facephi.selphi_component.SelphiController
import com.facephi.selphi_component.data.result.SelphiResult
import com.facephi.selphi_component.data.result.getSelphiError
import com.facephi.selphi_component.data.result.getSelphiResult
import com.facephi.selphid_component.SelphIDController
import com.facephi.selphid_component.data.result.SelphIDResult
import com.facephi.selphid_component.data.result.getSelphIDError
import com.facephi.selphid_component.data.result.getSelphIDResult
import com.facephi.tracking_component.TrackingErrorController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val flowController = FlowController(SdkData.flowConfigurationData)

    private val _logs = MutableStateFlow("")
    val logs = _logs.asStateFlow()
    fun initSdk(sdkApplication: SdkApplication) {
        SDKController.enableDebugMode()
        viewModelScope.launch {
            val sdkConfig = SdkData.getInitConfiguration(sdkApplication)
            when (val result = SDKController.initSdk(sdkConfig)) {
                is SdkResult.Success -> {
                    log("INIT SDK OK")
                    setStateFlow()
                }
                is SdkResult.Error -> log("INIT SDK ERROR: ${result.error}")
            }

            SDKController.launch(TrackingErrorController {
                log("Tracking Error: ${it.name}")
            })
        }
    }

    fun newOperation() {
        clearLogs()
        viewModelScope.launch {
            val result = SDKController.newOperation(
                operationType = SdkData.OPERATION_TYPE,
                customerId = SdkData.CUSTOMER_ID,
            )
            when (result) {
                is SdkResult.Success -> log("NEW OPERATION: OK")
                is SdkResult.Error -> log("NEW OPERATION: Error - ${result.error.name}")
            }
        }
    }

    fun launchSelphi() {
        viewModelScope.launch {
            when (val result =
                SDKController.launch(SelphiController(SdkData.selphiConfiguration))) {
                is SdkResult.Success -> {
                    log("Selphi: OK")
                    saveSelphiData(result.data)
                }
                is SdkResult.Error -> log("Selphi: Error - ${result.error.name}")
            }
        }
    }

    fun launchSelphId() {
        viewModelScope.launch {
            when (val result =
                SDKController.launch(SelphIDController(SdkData.selphIDConfiguration))) {
                is SdkResult.Success -> {
                    log("SelphID: OK")
                    saveSelphIdData(result.data)
                }
                is SdkResult.Error -> log("SelphID: Error - ${result.error.name}")
            }
        }
    }

    fun generateTemplateRawFromBitmap(){
        viewModelScope.launch {
            ImageData.selphiBestImage?.let {
                when (val result = SDKController.launch(RawTemplateController(SdkImage(it)))){
                    is SdkResult.Success -> log("Template generated. Length: ${result.data.length}")
                    is SdkResult.Error -> log("Template: Error - ${result.error.name}")
                }
            }
        }
    }

    private fun log(message: String){
        viewModelScope.launch {
            val data = _logs.value + "\n" + message
            _logs.emit(data)
        }

    }

    fun clearLogs(){
        viewModelScope.launch {
            _logs.emit("")
            ImageData.clear()
        }

    }

    fun launchFlow() {
        clearLogs()
        viewModelScope.launch {
            SDKController.launch(flowController)
        }
    }

    private fun setStateFlow(){
        viewModelScope.launch {
            flowController.stateFlow.collect { flowResult ->
                when (flowResult.step?.key) {
                    FlowKeys.SELPHID_COMPONENT.name -> {
                        when (val sdkResult = flowResult.result) {
                            is SdkResult.Error -> {
                                log("FLOW - SelphID ERROR: ${sdkResult.error.getSelphIDError().name}")
                            }

                            is SdkResult.Success -> {
                                log("FLOW - SelphID OK")
                                val result = sdkResult.data.getSelphIDResult()
                                saveSelphIdData(result)
                            }
                        }
                    }
                    FlowKeys.SELPHI_COMPONENT.name -> {
                        when (val sdkResult = flowResult.result) {
                            is SdkResult.Error -> {
                                log("FLOW - Selphi ERROR: ${sdkResult.error.getSelphiError().name}")
                            }

                            is SdkResult.Success -> {
                                log("FLOW - Selphi OK")
                                val result = sdkResult.data.getSelphiResult()
                                saveSelphiData(result)
                            }
                        }
                    }
                    FlowKeys.EXTERNAL_STEP.name -> {
                        log("FLOW - EXTERNAL STEP")
                    }
                    else -> {
                        when (val sdkResult = flowResult.result) {
                            is SdkResult.Error -> log("FLOW - ERROR: ${sdkResult.error}")
                            is SdkResult.Success -> log("FLOW - ${sdkResult.data}")
                        }
                    }
                }

                if (flowResult.flowFinish){
                    log("FLOW - FINISH")
                }
            }
        }

    }

    fun launchNextFlowStep() {
        viewModelScope.launch {
            flowController.launchNextStep()
        }
    }

    private fun saveSelphiData(selphiResult: SelphiResult){
        selphiResult.bestImage?.bitmap?.let {
            ImageData.selphiBestImage = it
            ImageData.selphiBestImageB64 = it.toBase64() ?: ""
        }
    }

    private fun saveSelphIdData(selphIDResult: SelphIDResult){
        if (selphIDResult.tokenFaceImage.isNotEmpty()) {
            ImageData.tokenFaceImage = selphIDResult.tokenFaceImage
        }

        selphIDResult.faceImage?.bitmap .let {
            ImageData.documentFace = it
        }

        selphIDResult.frontDocumentImage?.bitmap.let {
            ImageData.documentFront = it
        }

        selphIDResult.backDocumentImage?.bitmap.let {
            ImageData.documentBack = it
        }
    }

}