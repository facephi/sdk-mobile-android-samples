package com.facephi.demovideocall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkResult
import com.facephi.sdk.SDKController
import com.facephi.videocall_component.VideoCallController
import com.facephi.videocall_component.process.recording.VideoCallScreenSharingManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import io.github.aakira.napier.Napier

class MainViewModel : ViewModel() {

    private val _logs = MutableStateFlow("")
    val logs = _logs.asStateFlow()
    private lateinit var videoCallScreenSharingManager: VideoCallScreenSharingManager

    fun initSdk(sdkApplication: SdkApplication) {
        SDKController.getAnalyticsEvents { time, componentName, eventType, info ->
            Napier.i { "*** ${formatEpochMillis(time)} - ${componentName.name} -" +
                    " ${eventType.name} -  ${info ?: ""} " }
        }
        videoCallScreenSharingManager = VideoCallScreenSharingManager()
        videoCallScreenSharingManager.setOutput {
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
                        activateScreenSharing()
                    }
                }
                is SdkResult.Error -> log("Video Call: Error - ${result.error.name}")
            }
        }
    }

    private fun activateScreenSharing(){
        log("Video Call: Activate screen sharing")
        videoCallScreenSharingManager.startScreenSharingService()
    }

    fun stopScreenSharing(){
        log("Video Call: Stop screen sharing")
        videoCallScreenSharingManager.stopScreenSharingService()
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

    private fun formatEpochMillis(epochMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val localDateTime =
            instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${localDateTime.date} ${localDateTime.time}"
    }

}