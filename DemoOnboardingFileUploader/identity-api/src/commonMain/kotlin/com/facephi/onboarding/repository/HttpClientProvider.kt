package com.facephi.onboarding.repository

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import io.github.aakira.napier.Napier

object HttpClientProvider {
    val client: HttpClient by lazy { createPlatformHttpClient() }
}

expect fun createPlatformHttpClient(): HttpClient

@OptIn(ExperimentalSerializationApi::class)
internal fun io.ktor.client.HttpClientConfig<*>.configureCommonClient() {
    expectSuccess = true
    defaultRequest {
        contentType(ContentType.Application.Json)
    }
    install(HttpTimeout) {
        val timeout = 60000L
        connectTimeoutMillis = timeout
        requestTimeoutMillis = timeout
        socketTimeoutMillis = timeout
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Napier.v(message, tag = "HTTP Client")
            }
        }
        level = LogLevel.ALL
    }
    install(ContentNegotiation) {
        json(Json {
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
            encodeDefaults = true
        })
    }
}
