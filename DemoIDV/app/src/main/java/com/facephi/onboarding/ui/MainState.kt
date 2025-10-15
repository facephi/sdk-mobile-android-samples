package com.facephi.onboarding.ui

import com.facephi.sdk.data.IntegrationFlowData

data class MainState(
    val flowList : List<IntegrationFlowData> = listOf(),
    val sdkReady: Boolean = false,
    val flowActive: Boolean = false,
    val logs: String = "",
)
