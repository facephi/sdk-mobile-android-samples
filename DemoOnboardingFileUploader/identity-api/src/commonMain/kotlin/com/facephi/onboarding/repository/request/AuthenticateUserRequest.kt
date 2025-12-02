package com.facephi.onboarding.repository.request
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticateUserRequest(
    val userId: String,
    val registeredTemplateRaw: String? = null,
    val image: String? = null,
    val bestImageToken: String?,
    val template: String? = null,
    val merchantReferenceId: String,
    val tracking: TrackingData? = null
)
