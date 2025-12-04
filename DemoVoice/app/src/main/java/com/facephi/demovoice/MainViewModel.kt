package com.facephi.demovoice

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkResult
import com.facephi.demovoice.repository.VerificationsApi
import com.facephi.demovoice.repository.request.VoiceAuthenticationRequest
import com.facephi.demovoice.repository.request.VoiceEnrollRequest
import com.facephi.demovoice.ui.data.UIComponentResult
import com.facephi.sdk.SDKController
import com.facephi.voice_component.VoiceController
import com.facephi.voice_component.data.configuration.VoiceConfigurationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class MainViewModel : ViewModel() {

    private val _logs = MutableStateFlow("")
    val logs = _logs.asStateFlow()

    private var enrollTokenizedAudios = arrayOf<String>()
    private var authTokenizedAudio = ""
    private var enrollTemplate = ""
    fun initSdk(sdkApplication: SdkApplication) {
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
                is SdkResult.Success -> log("INIT SDK OK")
                is SdkResult.Error -> log("INIT SDK ERROR: ${result.error}")
            }

            /*SDKController.launch(TrackingErrorController {
                log("Tracking Error: ${it.name}")
            })*/
        }
    }

    fun newOperation(onOperationStarted: () -> Unit) {
        viewModelScope.launch {
            val result = SDKController.newOperation(
                operationType = SdkData.OPERATION_TYPE,
                customerId = SdkData.CUSTOMER_ID,
            )
            when (result) {
                is SdkResult.Success -> {
                    log("NEW OPERATION: OK")
                    onOperationStarted.invoke()
                }

                is SdkResult.Error -> log("NEW OPERATION: Error - ${result.error.name}")
            }
        }
    }

    fun launchVoiceEnroll(
        data: VoiceConfigurationData,
        output: (Array<ByteArray>) -> Unit,
        onResult: (UIComponentResult) -> Unit
    ) {
        viewModelScope.launch {
            when (val result =
                SDKController.launch(VoiceController(data))) {
                is SdkResult.Success -> {
                    log("Voice Enroll: OK")
                    if (result.data.audios.isNotEmpty()) {
                        enrollTokenizedAudios = result.data.tokenizedAudios
                        output.invoke(result.data.audios)
                        onResult.invoke(UIComponentResult.OK)
                    }
                }

                is SdkResult.Error -> {
                    log("Voice Enroll: Error - ${result.error.name}")
                    onResult.invoke(UIComponentResult.ERROR)
                }
            }
        }
    }

    fun launchVoiceAuth(
        data: VoiceConfigurationData,
        onResult: (UIComponentResult) -> Unit
    ) {
        Log.d("APP", "Launch Voice Auth")
        viewModelScope.launch {
            when (val result =
                SDKController.launch(VoiceController(data))) {
                is SdkResult.Success -> {
                    log("Voice Auth: OK")
                    Log.d("APP", "Voice Auth OK")
                    if (result.data.tokenizedAudios.isNotEmpty()) {
                        authTokenizedAudio = result.data.tokenizedAudios.first()
                    }
                    onResult.invoke(UIComponentResult.OK)

                }

                is SdkResult.Error -> {
                    log("Voice Auth: Error - ${result.error.name}")
                    Log.d("APP", "Voice Auth Error - ${result.error.name}")
                    onResult.invoke(UIComponentResult.ERROR)
                }
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

        if (SdkData.API_KEY.isEmpty() || SdkData.BASE_URL.isEmpty()) {
            log("DATA is empty")
            return
        }

        val verificationController = VerificationsApi(context, SdkData.API_KEY)

        viewModelScope.launch {
            // If Tracking Component is used
            /*val extraData = when (val result = SDKController.launch(ExtraDataController())) {
                is SdkResult.Success -> result.data
                is SdkResult.Error -> {
                    log("EXTRA_DATA: Error - ${result.error}")
                    ""
                }
            }

            if (extraData.isEmpty()) return@launch

            val operationId = SDKController.launch(GetOperationIdController()).orEmpty()

            if (operationId.isEmpty()) return@launch

            val trackingData = TrackingData(
                extraData = extraData,
                operationId = operationId
            )

             */

            // VOICE ENROLL

            enrollTokenizedAudios.takeIf { it.isNotEmpty() }?.let { enrollAudios ->
                val response = verificationController.voiceEnroll(
                    request = VoiceEnrollRequest(
                        audios = enrollAudios,
                        extraData = ""//extraData
                    ), baseUrl = SdkData.BASE_URL
                )
                log("** voiceEnroll: ${response}\n")
            }

            // VOICE AUTHENTICATION

            enrollTemplate.takeIf { it.isNotEmpty() }?.let { template ->
                authTokenizedAudio.takeIf { it.isNotEmpty() }?.let { tokenizedAudio ->

                    val response = verificationController.voiceAuthentication(
                        request = VoiceAuthenticationRequest(
                            audio = tokenizedAudio,
                            template = template,
                            extraData = ""//extraData
                        ), baseUrl = SdkData.BASE_URL
                    )
                    log("** voiceAuthentication: ${response}\n")
                }
            }


        }
    }

    @OptIn(ExperimentalTime::class)
    private fun formatEpochMillis(epochMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val localDateTime =
            instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${localDateTime.date} ${localDateTime.time}"
    }

}