package com.facephi.onboarding.repository.request

data class PassiveLivenessTokenRequest(
    val trackingData: TrackingData = TrackingData(),
    val imageBuffer: String
)
