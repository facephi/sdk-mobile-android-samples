package com.facephi.onboarding.repository.request
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserTemplateRequest(
    val userId: String,
    val oldRegisteredTemplateRaw: String,
    val newRegisteredTemplateRaw: String,
    val tracking: TrackingData? = null
)
