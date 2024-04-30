package com.facephi.demovoice

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkResult
import com.facephi.sdk.SDKController
import com.facephi.tracking_component.ExtraDataController
import com.facephi.tracking_component.TrackingErrorController
import com.facephi.verifications_component.VerificationController
import com.facephi.verifications_component.data.configuration.VoiceAuthenticationRequest
import com.facephi.verifications_component.data.configuration.VoiceEnrollRequest
import com.facephi.verifications_component.data.result.VerificationsResult
import com.facephi.voice_component.VoiceController
import com.facephi.voice_component.data.configuration.VoiceConfigurationData
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _logs = MutableStateFlow("")
    val logs = _logs.asStateFlow()

    private var enrollTokenizedAudios = arrayOf<String>()
    private var authTokenizedAudio = ""
    private var enrollTemplate = ""
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

    fun launchVoiceEnroll(data: VoiceConfigurationData, output: (Array<ByteArray>) -> Unit) {
        viewModelScope.launch {
            when (val result =
                SDKController.launch(VoiceController(data))) {
                is SdkResult.Success -> {
                    log("Voice Enroll: OK")
                    if (result.data.audios.isNotEmpty()) {
                        enrollTokenizedAudios = result.data.tokenizedAudios
                        output.invoke(result.data.audios)
                    }
                }

                is SdkResult.Error -> log("Voice Enroll: Error - ${result.error.name}")
            }
        }
    }

    fun launchVoiceAuth(data: VoiceConfigurationData,) {
        viewModelScope.launch {
            when (val result =
                SDKController.launch(VoiceController(data))) {
                is SdkResult.Success -> {
                    log("Voice Auth: OK")
                    if (result.data.tokenizedAudios.isNotEmpty()) {
                        authTokenizedAudio = result.data.tokenizedAudios.first()
                    }

                }

                is SdkResult.Error -> log("Voice Auth: Error - ${result.error.name}")
            }
        }
    }

    private fun log(message: String) {
        viewModelScope.launch {
            val data = _logs.value + "\n" + message
            _logs.emit(data)
        }

    }

    fun clearLogs() {
        viewModelScope.launch {
            _logs.emit("")
        }

    }

    fun launchVerifications(context: Context) {
        val verificationController = VerificationController(context)

        viewModelScope.launch {
            val extraData = when (val result = SDKController.launch(ExtraDataController())) {
                is SdkResult.Success -> result.data
                is SdkResult.Error -> {
                    log("EXTRA_DATA: Error - ${result.error}")
                    ""
                }
            }

            if (extraData.isEmpty()) return@launch

            // VOICE ENROLL

            enrollTokenizedAudios.takeIf { it.isNotEmpty() }?.let { enrollAudios ->
                val response = verificationController.voiceEnroll(
                    VoiceEnrollRequest(
                        audios = enrollAudios,
                        extraData = extraData
                    ), baseUrl = SdkData.BASE_URL
                )
                when (response) {

                    is VerificationsResult.Error -> {
                        log("** voiceEnroll: Error - ${response.error}\n")
                    }

                    is VerificationsResult.Success -> {
                        log("** voiceEnroll: OK - ${response.data}\n")
                        response.data.template?.takeIf { it.isNotBlank() }?.let {
                            enrollTemplate = it
                        }
                    }
                }
            }

            // VOICE AUTHENTICATION

            enrollTemplate.takeIf { it.isNotEmpty() }?.let { template ->
                authTokenizedAudio.takeIf { it.isNotEmpty() }?.let { tokenizedAudio ->

                    val response = verificationController.voiceAuthentication(
                        VoiceAuthenticationRequest(
                            audio = tokenizedAudio,
                            template = template,
                            extraData = extraData
                        ), baseUrl = SdkData.BASE_URL
                    )
                    when (response) {

                        is VerificationsResult.Error -> {
                            log("** voiceAuthentication: Error - ${response.error}\n")
                        }

                        is VerificationsResult.Success -> {
                            log("** voiceAuthentication: OK - ${response.data}\n")
                        }
                    }

                }
            }


        }
    }

}