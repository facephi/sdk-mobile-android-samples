package com.facephi.onboarding.repository.request
import kotlinx.serialization.Serializable

@Serializable
data class PassiveLivenessTokenRequest(
    val tracking: TrackingData = TrackingData(),
    val imageBuffer: String?,
    //val sessionID: String?,
    //val operationId: String?,
    //val tenantID: String?
)
