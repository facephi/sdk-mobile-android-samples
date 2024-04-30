package com.facephi.onboarding

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facephi.core.data.SdkApplication
import com.facephi.core.data.SdkImage
import com.facephi.core.data.SdkResult
import com.facephi.onboarding.utils.toBase64
import com.facephi.sdk.SDKController
import com.facephi.selphi_component.RawTemplateController
import com.facephi.selphi_component.SelphiController
import com.facephi.selphid_component.SelphIDController
import com.facephi.tracking_component.ExtraDataController
import com.facephi.tracking_component.TrackingErrorController
import com.facephi.verifications_component.VerificationController
import com.facephi.verifications_component.data.configuration.LivenessWithImageRequest
import com.facephi.verifications_component.data.configuration.LivenessWithTemplateRequest
import com.facephi.verifications_component.data.configuration.MatchingDocumentWithFaceImageRequest
import com.facephi.verifications_component.data.configuration.MatchingDocumentWithFaceTemplateRequest
import com.facephi.verifications_component.data.result.VerificationsResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel : ViewModel() {
    private val _logs = MutableStateFlow("")
    val logs = _logs.asStateFlow()

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

    fun launchSelphi() {
        viewModelScope.launch {
            when (val result =
                SDKController.launch(SelphiController(SdkData.selphiConfiguration))) {
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

    fun launchSelphId() {
        viewModelScope.launch {
            when (val result =
                SDKController.launch(SelphIDController(SdkData.selphIDConfiguration))) {
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
                is SdkResult.Success -> log("Template generated: ${result.data}")
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
        val verificationController = VerificationController(context)

        viewModelScope.launch {
            val extraData = when (val result = SDKController.launch(ExtraDataController())) {
                is SdkResult.Success -> result.data
                is SdkResult.Error -> ""
            }

            // LIVENESS WITH BASE64 IMAGE

            ImageData.selphiBestImage?.toBase64()?.takeIf { it.isNotBlank() }?.let { bestImageB64 ->
                val response = verificationController.livenessWithImage(
                    LivenessWithImageRequest(
                        image = bestImageB64,
                        extraData = extraData
                    ), baseUrl = SdkData.BASE_URL
                )
                when (response) {

                    is VerificationsResult.Error -> {
                        log("** livenessWithImage: Error - ${response.error}\n")
                    }

                    is VerificationsResult.Success -> {
                        log("** livenessWithImage: OK - ${response.data}\n")
                    }
                }


            }

            // LIVENESS WITH TOKENIZED IMAGE

            ImageData.selphiBestImageTokenized?.takeIf { it.isNotBlank() }?.let { bestImageTokenized ->
                val response = verificationController.livenessWithTemplate(
                    LivenessWithTemplateRequest(
                        tokenImage = bestImageTokenized,
                        extraData = Base64.encodeToString(
                            UUID.randomUUID().toString().toByteArray(), Base64.NO_WRAP
                        )
                    ), baseUrl = SdkData.BASE_URL
                )
                when (response) {
                    is VerificationsResult.Error -> {
                        log("** livenessWithTemplate: Error - ${response.error}\n")
                    }

                    is VerificationsResult.Success -> {
                        log("** livenessWithTemplate: OK - ${response.data}\n")
                    }
                }


            }

            // MATCHING: BASE64 FACE IMAGE AND TOKENIZED DOCUMENT FACE IMAGE

            ImageData.selphiBestImage?.toBase64()?.takeIf { it.isNotBlank() }?.let { bestImageB64 ->
                ImageData.documentTokenFaceImage?.takeIf { it.isNotBlank() }
                    ?.let { documentTokenFaceImage ->
                        val response = verificationController.matchingDocumentWithFaceImage(
                            request = MatchingDocumentWithFaceImageRequest(
                                image = bestImageB64,
                                documentTemplate = documentTokenFaceImage,
                                extraData = Base64.encodeToString(
                                    UUID.randomUUID().toString().toByteArray(), Base64.NO_WRAP
                                )
                            ),
                            baseUrl = SdkData.BASE_URL
                        )
                        when (response) {
                            is VerificationsResult.Error -> {
                                log("** matchingDocumentWithFaceImage: Error - ${response.error}\n")
                            }

                            is VerificationsResult.Success -> {
                                log("** matchingDocumentWithFaceImage: OK - ${response.data}\n")
                            }
                        }
                    }
            }

            // MATCHING: BASE64 FACE IMAGE AND TOKENIZED DOCUMENT FACE IMAGE

            ImageData.selphiBestImageTokenized?.takeIf { it.isNotBlank() }?.let { bestImageTokenized ->
                ImageData.documentTokenFaceImage?.takeIf { it.isNotBlank() }
                    ?.let { documentTokenFaceImage ->
                        val response = verificationController.matchingDocumentWithFaceTemplate(
                            request = MatchingDocumentWithFaceTemplateRequest(
                                faceTemplate = bestImageTokenized,
                                documentTemplate = documentTokenFaceImage,
                                extraData = extraData
                            ),
                            baseUrl = SdkData.BASE_URL
                        )
                        when (response) {
                            is VerificationsResult.Error -> {
                                log("** matchingDocumentWithFaceTemplate: Error - ${response.error}\n")
                            }

                            is VerificationsResult.Success -> {
                                log("** matchingDocumentWithFaceTemplate: OK - ${response.data}\n")
                            }
                        }
                    }
            }
        }

    }

}