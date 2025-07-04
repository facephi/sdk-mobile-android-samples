package com.facephi.demovideocall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkResult
import com.facephi.sdk.SDKController
import com.facephi.videocall_component.StopVideoCallController
import com.facephi.videocall_component.VideoCallController
import io.github.aakira.napier.Napier
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
    private lateinit var stopVideoCallController: StopVideoCallController

    fun initSdk(sdkApplication: SdkApplication) {
        SDKController.getAnalyticsEvents { time, componentName, eventType, info ->
            Napier.i { "*** ${formatEpochMillis(time)} - ${componentName.name} -" +
                    " ${eventType.name} -  ${info ?: ""} " }
        }
        stopVideoCallController = StopVideoCallController()
        stopVideoCallController.setOutput {
            log("Screen sharing state: ${it.name}")
        }
        viewModelScope.launch {
            if (BuildConfig.DEBUG){
                SDKController.enableDebugMode()
            }

            val sdkConfig = SdkData.getInitConfiguration(sdkApplication)
            when (val result = SDKController.initSdk(sdkConfig)) {
                is SdkResult.Success -> log("INIT SDK OK")
                is SdkResult.Error -> log("INIT SDK ERROR: ${result.error}")
            }

           /* SDKController.launch(TrackingErrorController {
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

    fun launchVideoCall() {
        viewModelScope.launch {
            when (val result = SDKController.launch(
                VideoCallController(SdkData.videoCallConfiguration))) {
                is SdkResult.Success -> {
                    log("Video Call: OK")
                    if (result.data.sharingScreen){
                        log("Video Call: Activate screen sharing")
                    }
                }
                is SdkResult.Error -> log("Video Call: Error - ${result.error.name}")
            }
        }
    }


    fun stopScreenSharing(){
        log("Video Call: Stop screen sharing")
        viewModelScope.launch {
            SDKController.launch(stopVideoCallController)
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

    @OptIn(ExperimentalTime::class)
    private fun formatEpochMillis(epochMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val localDateTime =
            instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${localDateTime.date} ${localDateTime.time}"
    }

}