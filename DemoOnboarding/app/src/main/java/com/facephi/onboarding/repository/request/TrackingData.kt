package com.facephi.onboarding.repository.request
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackingData(
    val extraData: String = "",
    val operationId: String = "",
)
