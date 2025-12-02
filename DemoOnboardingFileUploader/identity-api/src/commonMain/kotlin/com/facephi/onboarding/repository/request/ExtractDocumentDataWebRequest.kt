package com.facephi.onboarding.repository.request
import kotlinx.serialization.Serializable

@Serializable
data class ExtractDocumentDataWebRequest(
    val tokenFrontDocument: String?,
    val tokenBackDocument: String?,
    val countryCode: String,
    val decompose: Boolean? = null,
    val tracking: TrackingData? = null
)
