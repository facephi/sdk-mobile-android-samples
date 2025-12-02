package com.facephi.onboarding.repository

import com.facephi.onboarding.repository.request.AuthenticateFacialRequest
import com.facephi.onboarding.repository.request.AuthenticateUserDeleteRequest
import com.facephi.onboarding.repository.request.AuthenticateUserRequest
import com.facephi.onboarding.repository.request.CivilValidationRequest
import com.facephi.onboarding.repository.request.DetokenizeRequest
import com.facephi.onboarding.repository.request.DigitalSignRequest
import com.facephi.onboarding.repository.request.EvaluatePassiveLivenessRequest
import com.facephi.onboarding.repository.request.ExtractDocumentDataRequest
import com.facephi.onboarding.repository.request.ExtractDocumentDataWebRequest
import com.facephi.onboarding.repository.request.ExtractFacialRequest
import com.facephi.onboarding.repository.request.FinishTrackingRequest
import com.facephi.onboarding.repository.request.IdentityRequest
import com.facephi.onboarding.repository.request.OnboardingRequest
import com.facephi.onboarding.repository.request.PassiveLivenessTokenRequest
import com.facephi.onboarding.repository.request.PreviredVerifyRequest
import com.facephi.onboarding.repository.request.UpdateUserTemplateRequest
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType

class IdentityApi(private val apiKey: String) {

    suspend fun evaluatePassiveLiveness(baseUrl: String, request: EvaluatePassiveLivenessRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/services/evaluatePassiveLiveness")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()

    suspend fun evaluatePassiveLivenessToken(baseUrl: String, request: PassiveLivenessTokenRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/services/evaluatePassiveLivenessToken")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()

    suspend fun passiveLivenessToken(baseUrl: String, request: PassiveLivenessTokenRequest): Any =
        evaluatePassiveLivenessToken(baseUrl, request)

    suspend fun authenticateFacial(baseUrl: String, request: AuthenticateFacialRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/services/authenticateFacial")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()

    suspend fun onboarding(baseUrl: String, request: OnboardingRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/onboarding/v2/identity")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()

    suspend fun finishOperation(baseUrl: String, request: FinishTrackingRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/services/finishTracking")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()

    suspend fun identity(baseUrl: String, request: IdentityRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/onboarding/v2/identity")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()

    suspend fun extractDocumentData(baseUrl: String, request: ExtractDocumentDataRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/services/extractDocumentData")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()

    suspend fun extractDocumentDataWeb(baseUrl: String, request: ExtractDocumentDataWebRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/services/extractDocumentDataWeb")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()

    suspend fun authenticateUser(baseUrl: String, request: AuthenticateUserRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/services/authenticateUser")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()

    suspend fun deleteAuthenticateUser(baseUrl: String, request: AuthenticateUserDeleteRequest): Any =
        HttpClientProvider.client.delete {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/services/authenticateUser")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()

    suspend fun traspasosVerify(
        baseUrl: String,
        serviceTransactionId: String,
        request: PreviredVerifyRequest
    ): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/services/previred/$serviceTransactionId/verify")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()

    suspend fun updateUserTemplate(baseUrl: String, request: UpdateUserTemplateRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/services/updateUserTemplate")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()

    suspend fun detokenize(baseUrl: String, request: DetokenizeRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/services/detokenize")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()

    suspend fun digitalSign(baseUrl: String, request: DigitalSignRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/services/digitalSign")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()

    suspend fun extractFacial(baseUrl: String, request: ExtractFacialRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/services/extractFacial")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()

    suspend fun civilValidation(baseUrl: String, request: CivilValidationRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/services/civilValidation")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()
}
