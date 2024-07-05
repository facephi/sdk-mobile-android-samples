package com.facephi.demovoice.repository

import android.content.Context
import io.github.aakira.napier.Napier
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

@OptIn(ExperimentalSerializationApi::class)
object HttpClientProvider {
    private var applicationContext: Context? = null
    val client: HttpClient by lazy {
        HttpClient {
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
    }

    fun initialize(context: Context?) {
        applicationContext = context?.applicationContext
    }
}
