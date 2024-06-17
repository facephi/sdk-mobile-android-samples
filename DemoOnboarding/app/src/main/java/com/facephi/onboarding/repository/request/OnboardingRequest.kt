package com.facephi.onboarding.repository.request
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OnboardingRequest(
    val token1: String,
    val bestImageToken: String,
    val method: Int,
)
