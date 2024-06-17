package com.facephi.onboarding.repository.request

data class AuthenticateFacialRequest(
    val trackingData: TrackingData = TrackingData(),
    val token1: String,
    val token2: String,
    val method: Int,
)

