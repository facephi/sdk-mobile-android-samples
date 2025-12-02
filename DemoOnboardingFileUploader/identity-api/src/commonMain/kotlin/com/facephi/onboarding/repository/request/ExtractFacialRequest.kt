package com.facephi.onboarding.repository.request
import kotlinx.serialization.Serializable

@Serializable
data class ExtractFacialRequest(
    val tokenBuffer: String
)
