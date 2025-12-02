package com.facephi.onboarding.repository.request
import kotlinx.serialization.Serializable

@Serializable
data class DetokenizeRequest(
    val bestImageToken: String,
    val transactionId: String
)
