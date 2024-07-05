package com.facephi.demovoice.repository.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VoiceAuthenticationRequest(
    var audio: String,
    var template: String,
    @SerialName("liveness_threshold")
    var livenessThreshold: Double = 0.5,
    @SerialName("minimum_snr_db")
    var minimumSnrDb: Int = 8,
    @SerialName("minimum_speech_duration_ms")
    var minimumSpeechDurationMs: Int = 1500,
    @SerialName("extra_data")
    var extraData: String
)
