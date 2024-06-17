package com.facephi.onboarding.repository.request
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PassiveLivenessTokenRequest(
    val trackingData: TrackingData = TrackingData(),
    val imageBuffer: String
)
