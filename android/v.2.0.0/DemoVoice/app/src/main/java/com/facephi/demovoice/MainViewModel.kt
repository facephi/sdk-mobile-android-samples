package com.facephi.demovoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkResult
import com.facephi.sdk.SDKController
import com.facephi.tracking_component.ExtraDataController
import com.facephi.tracking_component.TrackingErrorController
import com.facephi.voice_component.VoiceController
import io.github.aakira.napier.Napier
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

            SDKController.launch(TrackingErrorController {
                log("Tracking Error: ${it.name}")
            })
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

    fun launchVoiceEnroll(showTutorial: Boolean, output: (Array<ByteArray>) -> Unit) {
        viewModelScope.launch {
            val data = SdkData.voiceConfigurationData
            data.showTutorial = showTutorial
            when (val result =
                SDKController.launch(VoiceController(data))) {
                is SdkResult.Success -> {
                    log("Voice Enroll: OK")
                    if (result.data.audios.isNotEmpty()) {
                        voiceEnroll(result.data.tokenizedAudios)
                        output.invoke(result.data.audios)
                    }
                }
                is SdkResult.Error -> log("Voice Enroll: Error - ${result.error.name}")
            }
        }
    }

    fun launchVoiceAuth(showTutorial: Boolean,) {
        viewModelScope.launch {
            val data = SdkData.voiceAuthConfigurationData
            data.showTutorial = showTutorial
            when (val result =
                SDKController.launch(VoiceController(data))) {
                is SdkResult.Success -> {
                    log("Voice Auth: OK")
                    /* With template

                    VerificationManager.voiceAuthentication(
                        it.data.tokenizedAudios.first(),
                        template
                    ) { result ->
                        logs.add("APP: VOICE: AUTH: $result")
                    }
                    */

                }
                is SdkResult.Error -> log("Voice Auth: Error - ${result.error.name}")
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

    fun voiceEnroll(audios: Array<String>) {
        viewModelScope.launch {
            when (val result =
                SDKController.launch(ExtraDataController())) {
                is SdkResult.Success -> {
                    log("EXTRA_DATA Enroll: OK")

                    if (result.data.isNotEmpty()) {
                        // SERVICE: Voice Enroll audios, extraData.data
                        // Save template
                    }

                }

                is SdkResult.Error -> log("EXTRA_DATA Enroll: Error ${result.error}")
            }


        }
    }

    fun voiceAuthentication(audio: String, template: String) {
        viewModelScope.launch {
            when (val result =
                SDKController.launch(ExtraDataController())) {
                is SdkResult.Success -> {
                    log("EXTRA_DATA Auth: OK")

                    if (result.data.isNotEmpty()) {
                        // SERVICE: voiceAuthentication audio, template, extraData.data
                    }

                }

                is SdkResult.Error -> Napier.d("EXTRA_DATA Auth: Error ${result.error}")
            }
        }

    }

}