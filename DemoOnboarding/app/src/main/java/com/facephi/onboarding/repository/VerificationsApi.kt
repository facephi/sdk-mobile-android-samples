package com.facephi.onboarding.repository

import android.content.Context
import com.facephi.onboarding.repository.request.AuthenticateFacialRequest
import com.facephi.onboarding.repository.request.OnboardingRequest
import com.facephi.onboarding.repository.request.PassiveLivenessTokenRequest

import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType

class VerificationsApi(context: Context? = null, private val apiKey: String) {
    init {
        HttpClientProvider.initialize(context)
    }

    /**
     *  This service performs a liveness check using the best tokenized image obtained during the user's selfie capture.
     *  To generate the tokenized image, the native function of the Selphi widget is used, with the bestImage parameter
     *  (open image) generated by the widget.
     */

    suspend fun passiveLivenessToken(baseUrl: String, request: PassiveLivenessTokenRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/services/evaluatePassiveLivenessToken")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()

    /**
     *  This service performs facial validation between two faces, including both open images and biometric templates.
     *  The service can be used for the following validations (method param value):
     *
     * 1 -> Facial authentication between two open images, generated or not by FacePhi widgets.
     * 2 -> Facial authentication between two biometric templates, requires integration of
     *      FacePhi Selphi Mobile or Web widget.
     * 3 -> Facial authentication between an open image and a biometric template, requires integration
     *      of FacePhi Selphi Mobile or Web widget.
     * 4 -> Facial authentication between the face present in the identity document photo (TokenFaceImage)
     *      and an open image, requires integration of FacePhi SelphID Mobile widget.
     * 5 -> Facial authentication between the face present in the identity document photo (TokenFaceImage)
     *      and a biometric template, requires integration of FacePhi Selphi Mobile and SelphID Mobile widgets.
     */

    suspend fun authenticateFacial(baseUrl: String, request: AuthenticateFacialRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/services/authenticateFacial")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()

    /**
     *  This service performs both the liveness test validation and facial validation between the two faces,
     *  whether they are open images or tokenized parameters.
     */

    suspend fun onboarding(baseUrl: String, request: OnboardingRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/onboarding/v2/identity")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()
}
