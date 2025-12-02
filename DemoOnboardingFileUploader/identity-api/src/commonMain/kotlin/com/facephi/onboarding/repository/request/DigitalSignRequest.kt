package com.facephi.onboarding.repository.request
import kotlinx.serialization.Serializable

@Serializable
data class DigitalSignRequest(
    val endUserContactInfo: String,
    val file: String,
    val signingData: SigningData,
    val signingLocation: SigningLocation,
    val signingType: String,
    val signatureData: SignatureData? = null
)

@Serializable
data class SigningData(
    val facialAuthenticationHash: String,
    val serviceTransactionId: String
)

@Serializable
data class SigningLocation(
    val city: String,
    val country: String,
    val geoLocationPosition: String? = null,
    val ipAddress: String? = null
)

@Serializable
data class SignatureData(
    val handSignature: String? = null,
    val pageNumber: Int? = null,
    val signatureFieldPosition: String? = null
)
