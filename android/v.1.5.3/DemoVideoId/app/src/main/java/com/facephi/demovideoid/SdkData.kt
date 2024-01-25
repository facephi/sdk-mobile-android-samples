package com.facephi.demovideoid

import com.facephi.core.data.OperationType
import com.facephi.sdk.data.EnvironmentLicensingData
import com.facephi.video_id_component.data.configuration.VideoIdConfigurationData
import com.facephi.video_id_component.data.configuration.VideoIdMode

object SdkData {

    // ************** LICENSE **************

    const val LICENSE_ONLINE = false

    val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        url = "...",
        apiKey = "..."
    )

    const val LICENSE = "..."

    // ************** DATA **************

    const val CUSTOMER_ID = "demo_videoid@email.com"
    val OPERATION_TYPE = OperationType.ONBOARDING

    val videoIdConfiguration = VideoIdConfigurationData(
        showCompletedTutorial = true,
        mode = VideoIdMode.ONLY_FACE
    )
}