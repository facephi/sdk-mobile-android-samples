package com.facephi.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkResult
import com.facephi.onboarding.ui.MainState
import com.facephi.sdk.FlowController
import com.facephi.sdk.SDKController
import com.facephi.sdk.data.OperationResult
import com.facephi.tracking_component.TrackingErrorController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class MainViewModel : ViewModel() {
    private val _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()

    fun initSdk(sdkApplication: SdkApplication) {
        viewModelScope.launch {
            SDKController.getAnalyticsEvents { time, componentName, eventType, info ->
                Log.i ("APP",
                    "*** ${formatEpochMillis(time)} - ${componentName.name} -" +
                            " ${eventType.name} -  ${info ?: ""} "
                )
            }

            if (BuildConfig.DEBUG) {
                SDKController.enableDebugMode()
            }

            val sdkConfig = SdkData.getInitConfiguration(sdkApplication)
            when (val result = SDKController.initSdk(sdkConfig)) {
                is SdkResult.Success -> {
                    _mainState.update {
                        it.copy(
                            sdkReady = true,
                            flowList = SDKController.getFlowIntegrationData(),
                            logs = log("INIT SDK OK")
                        )
                    }
                }

                is SdkResult.Error -> {
                    _mainState.update {
                        it.copy(logs = log("INIT SDK ERROR: ${result.error}"))
                    }

                }
            }

            SDKController.launch(TrackingErrorController { error ->
                _mainState.update {
                    it.copy(logs = log("Tracking Error: ${error.name}"))
                }

            })
        }
    }

    fun start(flowId: String?) {
        if (flowId == null) {
            _mainState.update {
                it.copy(logs = log("No flow selected"))
            }
            return
        }

        _mainState.update {
            it.copy(
                flowActive = true,
                logs = log("Launch flow $flowId")
            )
        }
        val controller = FlowController(
            SdkData.getIDVFlowConfigurationData(flowId)
        )
        viewModelScope.launch {
            controller.stateFlow.collect { flowResult ->
                // Flow info
                flowResult.step?.key?.let { key ->
                    _mainState.update {
                        it.copy(logs = log("New Step: $key"))
                    }
                }

                // Operation ID
                val sdkResult = flowResult.result

                if (sdkResult is SdkResult.Success && sdkResult.data is OperationResult) {
                    _mainState.update {
                        it.copy(logs = log("New Operation: ${(sdkResult.data as OperationResult).operationId}"))
                    }
                }

                // Flow finished flag
                if (flowResult.flowFinish) {
                    _mainState.update {
                        it.copy(
                            flowActive = false,
                            logs = log("Flow Finish")
                        )
                    }
                }

            }
        }
        viewModelScope.launch {
            SDKController.launch(controller)
        }
    }


    private fun log(message: String): String {
        val data = _mainState.value.logs + "\n" + message
        Log.i ("APP", message )
        return data

    }

    fun clearLogs() {
        viewModelScope.launch {
            _mainState.update {
                it.copy(logs = "")
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