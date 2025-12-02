package com.facephi.onboarding.repository.request
import kotlinx.serialization.Serializable

@Serializable
data class IdentityRequest(
    val tracking: TrackingData,
    val bestImageToken: String?,
    val method: String?,
    val token1: String?
)
