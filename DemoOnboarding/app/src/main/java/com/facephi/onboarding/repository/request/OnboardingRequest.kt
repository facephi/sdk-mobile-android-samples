package com.facephi.onboarding.repository.request

data class OnboardingRequest(
    val token1: String,
    val bestImageToken: String,
    val method: Int,
)
