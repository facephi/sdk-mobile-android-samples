package com.facephi.demophingers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkResult
import com.facephi.phingers_component.PhingersController
import com.facephi.phingers_component.data.configuration.CaptureOrientation
import com.facephi.phingers_component.data.configuration.PhingersConfigurationData
import com.facephi.sdk.SDKController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _logs = MutableStateFlow("")
    val logs = _logs.asStateFlow()
    fun initSdk(sdkApplication: SdkApplication) {
        viewModelScope.launch {
            SDKController.enableDebugMode()

            val sdkConfig = SdkData.getInitConfiguration(sdkApplication)
            when (val result = SDKController.initSdk(sdkConfig)) {
                is SdkResult.Success -> log("INIT SDK OK")
                is SdkResult.Error -> log("INIT SDK ERROR: ${result.error}")
            }

            /*SDKController.launch(TrackingErrorController {
                log("Tracking Error: ${it.name}")
            })*/
        }
    }

    fun newOperation() {
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

    fun launchPhingers(showTutorial: Boolean, captureOrientation: CaptureOrientation) {
        viewModelScope.launch {
            val data = PhingersConfigurationData(
                showTutorial = showTutorial,
                reticleOrientation = captureOrientation
            )
            when (val result =
                SDKController.launch(PhingersController(data))) {
                is SdkResult.Success -> {
                    log("PHINGERS: OK")
                }
                is SdkResult.Error -> log("PHINGERS Error - ${result.error.name}")
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
        }

    }

}