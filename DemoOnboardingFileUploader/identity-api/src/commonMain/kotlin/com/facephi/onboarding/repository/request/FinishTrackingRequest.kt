package com.facephi.onboarding.repository.request
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FinishTrackingRequest(
    val family: String,
    val status: String,
    val operationId: String,
    val sessionId: String
)
