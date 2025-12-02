package com.facephi.onboarding.repository.request
import kotlinx.serialization.Serializable

@Serializable
data class TrackingData(
    val sessionId: String = "",
    val operationId: String = "",
    val tenantId: String = "",
    val extraData: String? = null
)
