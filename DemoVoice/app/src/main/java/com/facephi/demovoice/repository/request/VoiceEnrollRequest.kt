package com.facephi.demovoice.repository.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VoiceEnrollRequest(
    var audios: Array<String>,
    @SerialName("check_liveness")
    var checkLiveness: Boolean = true,
    @SerialName("liveness_threshold")
    var livenessThreshold: Double = 0.5,
    @SerialName("minimum_snr_db")
    var minimumSnrDb: Int = 8,
    @SerialName("minimum_speech_duration_ms")
    var minimumSpeechDurationMs: Int = 1500,
    @SerialName("minimum_speech_relative_length")
    var minimumSpeechRelativeLenght: Float? = null,
    @SerialName("maximum_multiple_speakers_detector_score")
    var maximumMultipleSpeakersDetectorScore: Int? = null,
    @SerialName("extra_data")
    var extraData: String
)
