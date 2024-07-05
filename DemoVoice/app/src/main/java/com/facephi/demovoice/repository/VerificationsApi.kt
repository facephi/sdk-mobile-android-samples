package com.facephi.demovoice.repository

import android.content.Context
import com.facephi.demovoice.repository.request.VoiceAuthenticationRequest
import com.facephi.demovoice.repository.request.VoiceEnrollRequest


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

    suspend fun voiceEnroll(baseUrl: String, request: VoiceEnrollRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/...")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()


    suspend fun voiceAuthentication(baseUrl: String, request: VoiceAuthenticationRequest): Any =
        HttpClientProvider.client.post {
            url {
                contentType(ContentType.Application.Json)
                url("$baseUrl/...")
                header("x-api-key", apiKey)
                setBody(request)
            }
        }.body()
}
