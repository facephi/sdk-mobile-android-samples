package com.facephi.onboarding.repository.request
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticateFacialRequest(
    val tracking: TrackingData = TrackingData(),
    val token1: String?,
    val token2: String?,
    val method: String?,
)

