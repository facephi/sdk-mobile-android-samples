package com.facephi.onboarding

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkImage
import com.facephi.core.data.SdkResult
import com.facephi.onboarding.repository.VerificationsApi
import com.facephi.onboarding.repository.request.AuthenticateFacialRequest
import com.facephi.onboarding.repository.request.OnboardingRequest
import com.facephi.onboarding.repository.request.PassiveLivenessTokenRequest
import com.facephi.onboarding.utils.toBase64
import com.facephi.sdk.SDKController
import com.facephi.selphi_component.RawTemplateController
import com.facephi.selphi_component.SelphiController
import com.facephi.selphid_component.SelphIDController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _logs = MutableStateFlow("")
    val logs = _logs.asStateFlow()

    fun initSdk(sdkApplication: SdkApplication) {
        viewModelScope.launch {
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

    fun generateTemplateRawFromBitmap(bitmap: Bitmap) {
        viewModelScope.launch {
            when (val result = SDKController.launch(RawTemplateController(SdkImage(bitmap)))) {
                is SdkResult.Success -> log("Template generated. Length: ${result.data.length}")
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


            // LIVENESS WITH TOKENIZED IMAGE

            ImageData.selphiBestImageTokenized?.takeIf { it.isNotBlank() }
                ?.let { bestImageTokenized ->
                    val response = verificationController.passiveLivenessToken(
                        request = PassiveLivenessTokenRequest(
                            imageBuffer = bestImageTokenized,
                            //trackingData = trackingData
                        ),
                        baseUrl = SdkData.BASE_URL
                    )

                    log("** passiveLivenessToken: ${response}\n")

                }

            // MATCHING: BASE64 FACE IMAGE AND TOKENIZED DOCUMENT FACE IMAGE

            ImageData.selphiBestImage?.toBase64()?.takeIf { it.isNotBlank() }?.let { bestImageB64 ->
                ImageData.documentTokenFaceImage?.takeIf { it.isNotBlank() }
                    ?.let { documentTokenFaceImage ->
                        val response = verificationController.authenticateFacial(
                            request = AuthenticateFacialRequest(
                                token1 = bestImageB64,
                                token2 = documentTokenFaceImage,
                                method = 4,
                                //trackingData = trackingData
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
                                    //trackingData = trackingData
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
                }
        }

    }

}