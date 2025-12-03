package com.facephi.onboarding

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkImage
import com.facephi.core.data.SdkResult
import com.facephi.onboarding.ui.data.UIComponentResult
import com.facephi.sdk.SDKController
import com.facephi.selphi_component.RawTemplateController
import com.facephi.selphi_component.SelphiController
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
                Log.i ( "APP","*** ${formatEpochMillis(time)} - ${componentName.name} -" +
                        " ${eventType.name} -  ${info ?: ""} " )
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

    fun launchSelphi(
        showTutorial: Boolean,
        showPreviousTip: Boolean,
        showDiagnostic: Boolean,
        onResult: (UIComponentResult) -> Unit
    ) {
        viewModelScope.launch {
            when (val result =
                SDKController.launch(
                    SelphiController(
                        SdkData.getSelphiConfiguration(
                            showPreviousTip = showPreviousTip,
                            showTutorial = showTutorial,
                            showDiagnostic = showDiagnostic
                        )

                    )
                )) {
                is SdkResult.Success -> {
                    log("Selphi: OK")
                    result.data.bestImage?.bitmap?.let {
                        ImageData.selphiBestImage = it
                    }
                    result.data.bestImageTokenized?.let {
                        ImageData.selphiBestImageTokenized = it
                    }
                    onResult.invoke(UIComponentResult.OK)

                }

                is SdkResult.Error -> {
                    log("Selphi: Error - ${result.error.name}")
                    onResult.invoke(UIComponentResult.ERROR)
                }
            }
        }
    }

    fun generateTemplateRawFromBitmap(bitmap: Bitmap) {
        viewModelScope.launch {
            when (val result = SDKController.launch(RawTemplateController(SdkImage(bitmap)))) {
                is SdkResult.Success -> log("Template generated. Length: ${result.data.result.length}")
                is SdkResult.Error -> log("Template: Error - ${result.error.name}")
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
            ImageData.clear()
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