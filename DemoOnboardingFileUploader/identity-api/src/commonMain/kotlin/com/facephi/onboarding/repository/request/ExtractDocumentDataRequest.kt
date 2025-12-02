package com.facephi.onboarding.repository.request
import kotlinx.serialization.Serializable

@Serializable
data class ExtractDocumentDataRequest(
    val tokenOcr: String?,
    val tracking: TrackingData?
)
