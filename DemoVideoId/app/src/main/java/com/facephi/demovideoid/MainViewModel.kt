package com.facephi.demovideoid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkResult
import com.facephi.demovideoid.ui.data.UIComponentResult
import com.facephi.sdk.SDKController
import com.facephi.video_id_component.VideoIdController
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
    fun initSdk(sdkApplication: SdkApplication) {
        viewModelScope.launch {
            SDKController.getAnalyticsEvents { time, componentName, eventType, info ->
                Log.i ("APP", "*** ${formatEpochMillis(time)} - ${componentName.name} -" +
                        " ${eventType.name} -  ${info ?: ""} " )
            }
            if (BuildConfig.DEBUG){
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
                is SdkResult.Error -> {
                    log("NEW OPERATION: Error - ${result.error.name}")
                }
            }
        }
    }

    fun launchVideoId(autoFace: Boolean, onResult: (UIComponentResult) -> Unit) {
        viewModelScope.launch {
            when (val result =
                SDKController.launch(VideoIdController(SdkData.getVideoIdConfiguration(autoFace)))) {
                is SdkResult.Success -> {
                    log("Video ID: OK")
                    onResult.invoke(UIComponentResult.OK)
                }
                is SdkResult.Error -> {
                    log("Video ID: Error - ${result.error.name}")
                    onResult.invoke(UIComponentResult.ERROR)
                }
            }
        }
    }

    private fun log(message: String){
        Log.d("APP", message)
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