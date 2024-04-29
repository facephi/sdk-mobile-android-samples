package com.facephi.onboarding


import android.util.Base64
import android.util.Log
import com.facephi.verifications_component.VerificationController
import com.facephi.verifications_component.data.configuration.LivenessWithImageRequest
import com.facephi.verifications_component.data.configuration.LivenessWithTemplateRequest
import com.facephi.verifications_component.data.configuration.MatchingDocumentWithFaceImageRequest
import com.facephi.verifications_component.data.configuration.MatchingDocumentWithFaceTemplateRequest
import com.facephi.verifications_component.data.result.VerificationsResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID


object VerificationManager {
    private val verificationsController = VerificationController()
    private val VOICE_BASE_URL = ""
    private val BASE_URL = ""
    fun matchingFacialWithTokens(
        selphiFace: String,
        tokenFaceImage: String,
        output: (String) -> Unit
    ) {
        // MATCHING FACIAL SERVICE

        urlList.forEach { url ->
            CoroutineScope(Dispatchers.IO).launch {
                Log.i("APP", "MATCHING TOKEN LAUNCHED $url")

                val result = verificationsController.matchingDocumentWithFaceTemplate(
                    request = MatchingDocumentWithFaceTemplateRequest(
                        faceTemplate = selphiFace,
                        documentTemplate = tokenFaceImage,
                        extraData = Base64.encodeToString(
                            UUID.randomUUID().toString().toByteArray(),Base64.NO_WRAP
                        )
                    ),
                    baseUrl = url
                )
                when (result) {
                    is VerificationsResult.Error -> {
                        Log.i("APP", "MATCHING TOKEN: Error ${result.error}")
                        output("\n***MATCHING TOKEN: URL:  $url - Error ${result.error}")
                    }

                    is VerificationsResult.Success -> {
                        Log.i("APP", "MATCHING TOKEN: URL:  $url - RESPONSE:  ${result.data}.")
                        output("\n***MATCHING TOKEN: URL:  $url - RESPONSE:  ${result.data}.")
                    }
                }


            }
        }

    }

    fun matchingFacial(
        selphiFace: String,
        tokenFaceImage: String,
        urlList: ArrayList<String>,
        output: (String) -> Unit
    ) {
        // MATCHING FACIAL SERVICE

        urlList.forEach { url ->
            CoroutineScope(Dispatchers.IO).launch {
                Log.i("APP", "MATCHING LAUNCHED $url")

                val result = verificationsController.matchingDocumentWithFaceImage(
                    request = MatchingDocumentWithFaceImageRequest(
                        image = selphiFace,
                        documentTemplate = tokenFaceImage,
                        extraData = Base64.encodeToString(
                            UUID.randomUUID().toString().toByteArray(),Base64.NO_WRAP
                        )
                    ),
                    baseUrl = url
                )
                when (result) {
                    is VerificationsResult.Error -> {
                        Log.i("APP", "MATCHING: Error ${result.error}")
                        output("\n***MATCHING: URL:  $url - Error ${result.error}")
                    }

                    is VerificationsResult.Success -> {
                        Log.i("APP", "MATCHING: URL:  $url - RESPONSE:  ${result.data}.")
                        output("\n***MATCHING: URL:  $url - RESPONSE:  ${result.data}.")
                    }
                }


            }
        }

    }

    fun liveness(selphiFace: String, urlList: ArrayList<String>, output: (String) -> Unit) {
        // LIVENESS SERVICE

        urlList.forEach { url ->
            CoroutineScope(Dispatchers.IO).launch {
                Log.i("APP", "LIVENESS LAUNCHED $url")
                val result = verificationsController.livenessWithImage(
                    LivenessWithImageRequest(
                        image = selphiFace,
                        extraData = Base64.encodeToString(
                            UUID.randomUUID().toString().toByteArray(),Base64.NO_WRAP
                        )
                    ), baseUrl = url
                )
                when (result) {
                    is VerificationsResult.Error -> {
                        Log.i("APP", "LIVENESS:  URL:  $url - Error ${result.error}")
                        output("\n***LIVENESS: Error ${result.error}")
                    }

                    is VerificationsResult.Success -> {
                        Log.i("APP", "LIVENESS:  URL:  $url - RESPONSE:  ${result.data}")
                        output("\n***LIVENESS:  URL:  $url - RESPONSE:  ${result.data}")
                    }
                }


            }
        }

    }

    fun livenessWithToken(selphiFace: String, urlList: ArrayList<String>, output: (String) -> Unit) {
        // LIVENESS SERVICE

        urlList.forEach { url ->
            CoroutineScope(Dispatchers.IO).launch {
                Log.i("APP", "LIVENESS TOKEN LAUNCHED $url")
                val result = verificationsController.livenessWithTemplate(
                    LivenessWithTemplateRequest(
                        tokenImage = selphiFace,
                        extraData = Base64.encodeToString(
                            UUID.randomUUID().toString().toByteArray(),Base64.NO_WRAP
                        )
                    ), baseUrl = url
                )
                when (result) {
                    is VerificationsResult.Error -> {
                        Log.i("APP", "LIVENESS TOKEN:  URL:  $url - Error ${result.error}")
                        output("\n***LIVENESS TOKEN: Error ${result.error}")
                    }

                    is VerificationsResult.Success -> {
                        Log.i("APP", "LIVENESS TOKEN:  URL:  $url - RESPONSE:  ${result.data}")
                        output("\n***LIVENESS TOKEN:  URL:  $url - RESPONSE:  ${result.data}")
                    }
                }


            }
        }

    }

}