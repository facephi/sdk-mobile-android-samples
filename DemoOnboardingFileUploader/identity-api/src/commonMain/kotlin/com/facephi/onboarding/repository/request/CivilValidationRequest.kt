package com.facephi.onboarding.repository.request
import kotlinx.serialization.Serializable

@Serializable
data class CivilValidationRequest(
    val operation: String,
    val platform: String,
    val documentNumber: String? = null,
    val imageFrontDocument: String? = null,
    val imageBackDocument: String? = null,
    val countryCode: String? = null,
    val tokenOcr: String? = null,
    val bestImage: String? = null,
    val gender: String? = null,
    val templateRaw: String? = null,
    val documentCode: String? = null,
    val documentValidation: Boolean? = null,
    val returnPII: Boolean? = null,
    val tracking: TrackingData? = null
)
