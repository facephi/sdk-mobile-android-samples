package com.facephi.onboarding

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facephi.capture_component.FileUploaderController
import com.facephi.capture_component.data.result.FileContent
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkImage
import com.facephi.core.data.SdkResult
import com.facephi.onboarding.ui.data.UIComponentResult
import com.facephi.sdk.SDKController
import com.facephi.selphi_component.RawTemplateController
import com.facephi.selphi_component.SelphiController
import com.facephi.selphid_component.SelphIDController
import com.facephi.video_recording_component.StopVideoRecordingController
import com.facephi.video_recording_component.VideoRecordingController
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
                Log.i ("APP",  "*** ${formatEpochMillis(time)} - ${componentName.name} -" +
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

    fun launchVideoRecording(onResult: (UIComponentResult) -> Unit) {
        viewModelScope.launch {
            when (val result = SDKController.launch(VideoRecordingController(SdkData.getVideoRecording()))) {
                is SdkResult.Success -> {
                    log("VideoRecording: OK")
                    onResult.invoke(UIComponentResult.OK)
                }
                is SdkResult.Error -> {
                    log("VideoRecording: Error - ${result.error.name}")
                    onResult.invoke(UIComponentResult.ERROR)
                }
            }
        }
    }

    fun launchStopVideoRecording() {
        viewModelScope.launch {
            when (val result = SDKController.launch(StopVideoRecordingController())) {
                is SdkResult.Success -> {
                    log("VideoRecording Stop: OK")
                }
                is SdkResult.Error -> {
                    log("VideoRecording Stop: Error - ${result.error.name}")
                }
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
                            showTutorial = showTutorial,
                            showPreviousTip = showPreviousTip,
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

    fun launchSelphId(
        showTutorial: Boolean,
        showPreviousTip: Boolean,
        showDiagnostic: Boolean,
        onResult: (UIComponentResult) -> Unit
    ) {
        viewModelScope.launch {
            when (val result =
                SDKController.launch(
                    SelphIDController(
                        SdkData.getSelphIDConfiguration(
                            showTutorial = showTutorial,
                            showPreviousTip = showPreviousTip,
                            showDiagnostic = showDiagnostic
                        )
                    )
                )) {
                is SdkResult.Success -> {
                    log("SelphID: OK")
                    if (result.data.tokenFaceImage.isNotEmpty()) {
                        ImageData.documentTokenFaceImage = result.data.tokenFaceImage
                    }
                    result.data.faceImage?.bitmap.let {
                        ImageData.documentFace = it
                    }

                    result.data.frontDocumentImage?.bitmap.let {
                        ImageData.documentFront = it
                    }

                    result.data.backDocumentImage?.bitmap.let {
                        ImageData.documentBack = it
                    }
                    onResult.invoke(UIComponentResult.OK)
                }

                is SdkResult.Error -> {
                    log("SelphID: Error - ${result.error.name}")
                    onResult.invoke(UIComponentResult.ERROR)
                }
            }
        }
    }

    fun launchFileUploader(
        allowGallery: Boolean,
        showPreviousTip: Boolean,
        showDiagnostic: Boolean,
        maxScannedDocs: Int,
        onResult: (UIComponentResult) -> Unit
    ) {
        viewModelScope.launch {
            when (val result =
                SDKController.launch(
                    FileUploaderController(
                        SdkData.getFileUploaderDConfiguration(
                            allowGallery = allowGallery,
                            showPreviousTip = showPreviousTip,
                            showDiagnostic = showDiagnostic,
                            maxScannedDocs = maxScannedDocs
                        )
                    )
                )) {
                is SdkResult.Success -> {
                    var imageCount = 0
                    var pdfCount = 0
                    result.data.capturedDocumentList.forEach { documentData ->
                        when (documentData.content) {
                            is FileContent.UploaderDocument -> {
                                pdfCount ++
                            }
                            is FileContent.UploaderImage -> {
                                imageCount ++
                            }
                        }
                    }
                    log("FileUploader: OK - Total files: ${(imageCount+pdfCount)} - Images: $imageCount - PDFs: $pdfCount")
                    onResult.invoke(UIComponentResult.OK)
                }

                is SdkResult.Error -> {
                    log("FileUploader: Error - ${result.error.name}")
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