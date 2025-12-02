package com.facephi.onboarding

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facephi.capture_component.FileUploaderController
import com.facephi.capture_component.data.result.FileContent
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkImage
import com.facephi.core.data.SdkResult
import com.facephi.core.data.tracking.Step
import com.facephi.onboarding.repository.IdentityApi
import com.facephi.onboarding.repository.request.ExtractDocumentDataRequest
import com.facephi.onboarding.repository.request.FinishTrackingRequest
import com.facephi.onboarding.repository.request.IdentityRequest
import com.facephi.onboarding.repository.request.TrackingData
import com.facephi.sdk.GetOperationIdController
import com.facephi.sdk.GetSessionIdController
import com.facephi.sdk.SDKController
import com.facephi.selphi_component.RawTemplateController
import com.facephi.selphi_component.SelphiController
import com.facephi.selphid_component.SelphIDController
import com.facephi.tracking_component.ExtraDataController
import com.facephi.tracking_component.TrackingErrorController
import com.facephi.video_recording_component.StopVideoRecordingController
import com.facephi.video_recording_component.VideoRecordingController
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

    fun initSdk(sdkApplication: SdkApplication) {
        viewModelScope.launch {
            SDKController.getAnalyticsEvents { time, componentName, eventType, info ->
                Napier.i {
                    "*** ${formatEpochMillis(time)} - ${componentName.name} -" +
                            " ${eventType.name} -  ${info ?: ""} "
                }
            }

            if (BuildConfig.DEBUG) {
                SDKController.enableDebugMode()
            }

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
                steps = listOf(
                    Step.VIDEO_RECORDING_COMPONENT,
                    Step.SELPHI_COMPONENT,
                    Step.SELPHID_COMPONENT,
                    Step.PHACTURA_CAPTURE_COMPONENT
                )
            )
            when (result) {
                is SdkResult.Success -> log("NEW OPERATION: OK")
                is SdkResult.Error -> log("NEW OPERATION: Error - ${result.error.name}")
            }
        }
    }

    fun launchVideoRecording() {
        viewModelScope.launch {
            when (val result =
                SDKController.launch(VideoRecordingController(SdkData.getVideoRecording()))) {
                is SdkResult.Success -> {
                    log("VideoRecording: OK")
                }

                is SdkResult.Error -> log("VideoRecording: Error - ${result.error.name}")
            }
        }
    }

    fun launchStopVideoRecording() {
        viewModelScope.launch {
            when (val result = SDKController.launch(StopVideoRecordingController())) {
                is SdkResult.Success -> {
                    log("VideoRecording Stop: OK")
                }

                is SdkResult.Error -> log("VideoRecording Stop: Error - ${result.error.name}")
            }
        }
    }

    fun launchSelphi(
        showTutorial: Boolean,
        showPreviousTip: Boolean,
        showDiagnostic: Boolean,
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

                }

                is SdkResult.Error -> log("Selphi: Error - ${result.error.name}")
            }
        }
    }

    fun launchSelphId(
        showTutorial: Boolean,
        showPreviousTip: Boolean,
        showDiagnostic: Boolean,
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
                    if (result.data.tokenOCR.isNotEmpty()) {
                        ImageData.documentTokenOcr = result.data.tokenOCR
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
                }

                is SdkResult.Error -> log("SelphID: Error - ${result.error.name}")
            }
        }
    }

    fun launchFileUploader(
        allowGallery: Boolean,
        showPreviousTip: Boolean,
        showDiagnostic: Boolean,
        maxScannedDocs: Int,
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
                                pdfCount++
                            }

                            is FileContent.UploaderImage -> {
                                imageCount++
                            }
                        }
                    }
                    log("FileUploader: OK - Total files: ${(imageCount + pdfCount)} - Images: $imageCount - PDFs: $pdfCount")
                }

                is SdkResult.Error -> log("FileUploader: Error - ${result.error.name}")
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

    fun launchVerifications(context: Context) {

        if (SdkData.API_KEY.isEmpty() || SdkData.BASE_URL.isEmpty()) {
            log("DATA is empty")
            return
        }

        val identityApi = IdentityApi(SdkData.API_KEY)

        viewModelScope.launch {
            // If Tracking Component is used
            val extraData = when (val result = SDKController.launch(ExtraDataController())) {
                is SdkResult.Success -> result.data.result
                is SdkResult.Error -> {
                    log("EXTRA_DATA: Error - ${result.error}")
                    ""
                }
            }

            //if (extraData.isNull()) return@launch

            val operationId = SDKController.launch(GetOperationIdController()).orEmpty()
            val sessionId = SDKController.launch(GetSessionIdController()).orEmpty()



            if (operationId.isEmpty()) return@launch
            if (sessionId.isEmpty()) return@launch

            val trackingData = TrackingData(
                sessionId = sessionId,
                operationId = operationId,
                tenantId = SdkData.TENANT_ID,
                extraData = extraData.takeIf { it.isNotBlank() }
            )


            // LIVENESS WITH TOKENIZED IMAGE

            /*ImageData.selphiBestImageTokenized?.takeIf { it.isNotBlank() }
                ?.let { bestImageTokenized ->
                    val response = verificationController.passiveLivenessToken(
                        request = PassiveLivenessTokenRequest(
                            imageBuffer = bestImageTokenized,
                            trackingData = trackingData
                        ),
                        baseUrl = SdkData.BASE_URL
                    )

                    log("** passiveLivenessToken: ${response}\n")

                }*/
            /*val response2 = verificationController.passiveLivenessToken(
                request = PassiveLivenessTokenRequest(
                    imageBuffer = ImageData.selphiBestImage?.toBase64(),
                    tracking = TrackingData(
                    sessionId = sessionId,
                    operationId = operationId,
                    tenantId = SdkData.TENANT_ID)
                ),
                baseUrl = SdkData.BASE_URL
            )

            log("** passiveLivenessToken: ${response2}\n")*/
            /*val response4 = verificationController.authenticateFacial(
                request = AuthenticateFacialRequest(
                    tracking = TrackingData(
                        sessionId = sessionId,
                        operationId = operationId,
                        tenantId = SdkData.TENANT_ID),
                    token1 = ImageData.selphiBestImageTokenized,
                    token2 = ImageData.documentTokenFaceImage,
                    method = "3"
                ),
                baseUrl = SdkData.BASE_URL
            )

            log("** passiveLivenessToken: ${response4}\n")*/
            // MATCHING: BASE64 FACE IMAGE AND TOKENIZED DOCUMENT FACE IMAGE

            /* ImageData.selphiBestImage?.toBase64()?.takeIf { it.isNotBlank() }?.let { bestImageB64 ->
                 ImageData.documentTokenFaceImage?.takeIf { it.isNotBlank() }
                     ?.let { documentTokenFaceImage ->
                         val response = verificationController.authenticateFacial(
                             request = AuthenticateFacialRequest(
                                 token1 = bestImageB64,
                                 token2 = documentTokenFaceImage,
                                 method = 4,
                                 trackingData = trackingData
                             ),
                             baseUrl = SdkData.BASE_URL
                         )

                         log("** authenticateFacial (method = 4): ${response}\n")
                     }
             }

             // MATCHING: BASE64 FACE IMAGE AND TOKENIZED DOCUMENT FACE IMAGE

             ImageData.selphiBestImageTokenized?.takeIf { it.isNotBlank() }
                 ?.let { bestImageTokenized ->
                     ImageData.documentTokenFaceImage?.takeIf { it.isNotBlank() }
                         ?.let { documentTokenFaceImage ->
                             val response = verificationController.authenticateFacial(
                                 request = AuthenticateFacialRequest(
                                     token1 = bestImageTokenized,
                                     token2 = documentTokenFaceImage,
                                     method = 5,
                                     trackingData = trackingData
                                 ),
                                 baseUrl = SdkData.BASE_URL
                             )

                             log("** authenticateFacial (method = 5): ${response}\n")
                         }
                 }

             // ONBOARDING: BASE64 FACE IMAGE AND TOKENIZED DOCUMENT FACE IMAGE

             ImageData.selphiBestImageTokenized?.takeIf { it.isNotBlank() }
                 ?.let { bestImageTokenized ->
                     ImageData.documentTokenFaceImage?.takeIf { it.isNotBlank() }
                         ?.let { documentTokenFaceImage ->
                             val response = verificationController.onboarding(
                                 request = OnboardingRequest(
                                     bestImageToken = bestImageTokenized,
                                     token1 = documentTokenFaceImage,
                                     method = 5,
                                     //trackingData = trackingData
                                 ),
                                 baseUrl = SdkData.BASE_URL
                             )

                             log("** onboarding (method = 5): ${response}\n")
                         }
                 }*/
            val response3 = identityApi.identity(
                request = IdentityRequest(
                    tracking = trackingData,
                    bestImageToken = ImageData.selphiBestImageTokenized,
                    method = "5",
                    token1 = ImageData.documentTokenFaceImage,
                ),
                baseUrl = SdkData.BASE_URL
            )
            log("** Identity : ${response3}\n")

            ImageData.documentTokenOcr?.takeIf { it.isNotBlank() }?.let { tokenOcr ->
                val response5 = identityApi.extractDocumentData(
                    request = ExtractDocumentDataRequest(
                        tokenOcr = tokenOcr,
                        tracking = trackingData
                    ),
                    baseUrl = SdkData.BASE_URL
                )
                log("** OCR : ${response5}\n")
            } ?: log("OCR: tokenOcr is empty, skipping extractDocumentData call")


            val response = identityApi.finishOperation(
                request = FinishTrackingRequest(
                    family = "OnBoarding",
                    status = "SUCCEEDED",
                    operationId = operationId,
                    sessionId = sessionId,
                ),
                baseUrl = SdkData.BASE_URL
            )
            log("** Finish Tracking : ${response}\n")
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
