package com.facephi.onboarding.repository.request
import kotlinx.serialization.Serializable

@Serializable
data class PreviredVerifyRequest(
    val scanReference: String
)
