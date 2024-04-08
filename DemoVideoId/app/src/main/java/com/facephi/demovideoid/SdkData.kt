package com.facephi.demovideoid


import com.facephi.core.request.EnvironmentLicensingData
import com.facephi.core.request.enums.OperationType
import com.facephi.video_id_component.data.configuration.EnvironmentVideoIdData
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

    val videoIdConfiguration = EnvironmentVideoIdData(
        showCompletedTutorial = true,
        mode = VideoIdMode.ONLY_FACE
    )
}