package com.facephi.demovideocall

import com.facephi.core.data.OperationType
import com.facephi.sdk.data.EnvironmentLicensingData
import com.facephi.videocall_component.data.configuration.VideoCallConfigurationData

object SdkData {

    // ************** LICENSE **************

    const val LICENSE_ONLINE = false

    val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        url = "...",
        apiKey = "..."
    )

    const val LICENSE = "..."

    // ************** DATA **************

    const val CUSTOMER_ID = "demo_videocall@email.com"
    val OPERATION_TYPE = OperationType.ONBOARDING

    val videoCallConfiguration = VideoCallConfigurationData()
}