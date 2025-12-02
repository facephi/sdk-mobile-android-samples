package com.facephi.onboarding.repository.request
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticateUserDeleteRequest(
    val userIds: List<String>
)
