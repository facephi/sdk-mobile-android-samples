package com.facephi.onboarding.repository.request
import kotlinx.serialization.Serializable

@Serializable
data class ExtractDataOCRRequest(
    val tracking: TrackingData,
    val tokenFrontDocument: String?,
    val tokenBackDocument: String?,
    val countryCode: String?,
    val decompose: Boolean
)
