package com.facephi.onboarding.repository.request
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticateFacialRequest(
    val trackingData: TrackingData = TrackingData(),
    val token1: String,
    val token2: String,
    val method: Int,
)

