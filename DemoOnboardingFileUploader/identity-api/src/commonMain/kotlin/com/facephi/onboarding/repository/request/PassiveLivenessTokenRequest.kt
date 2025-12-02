package com.facephi.onboarding.repository.request
import kotlinx.serialization.Serializable

@Serializable
data class PassiveLivenessTokenRequest(
    val imageBuffer: String?,
    val tracking: TrackingData? = null
)
